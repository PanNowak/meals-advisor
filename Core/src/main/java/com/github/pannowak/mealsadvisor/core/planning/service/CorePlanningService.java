package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.api.meals.model.Ingredient;
import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.planning.service.PlanningService;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.core.meals.service.CoreMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.GroupedFlux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.pannowak.mealsadvisor.core.planning.service.RandomMealsDrawer.DrawnMealInfo;

@Slf4j
@Service
public class CorePlanningService implements PlanningService {

    private final CoreMealService mealService;
    private final ConsecutiveWeekDaysGenerator weekDaysGenerator;
    private final RandomMealsDrawer randomMealsDrawer;

    CorePlanningService(CoreMealService mealService,
                        ConsecutiveWeekDaysGenerator weekDaysGenerator,
                        RandomMealsDrawer randomMealsDrawer) {
        this.mealService = mealService;
        this.weekDaysGenerator = weekDaysGenerator;
        this.randomMealsDrawer = randomMealsDrawer;
    }

    @Override
    public Flux<DayPlan> draw(int firstDay, int lastDay) {
        Flux<Integer> consecutiveWeekDays = weekDaysGenerator.generate(firstDay, lastDay);
        Flux<Map<MealType, MealSummary>> mealPlans = drawMealPlans();
        return createDayPlans(consecutiveWeekDays, mealPlans);
    }

    @Override
    public Flux<GroceryItem> generateShoppingList(Collection<Long> mealIds) {
        return getRequestedMeals(mealIds)
                .publishOn(Schedulers.parallel())
                .transform(this::groupIngredientsByProduct)
                .flatMap(this::createGroceryItem);
    }

    private Flux<Map<MealType, MealSummary>> drawMealPlans() {
        return mealService.getAllTypes()
                .map(this::drawMealsOfTheSameType)
                .collectList()
                .flatMapMany(this::correlateMealsForTheSameDay);
    }

    private Flux<DrawnMealInfo> drawMealsOfTheSameType(MealType mealType) {
        return mealService.getAllByType(mealType)
                .collectList()
                .publishOn(Schedulers.parallel())
                .flatMapMany(sameTypeMeals -> randomMealsDrawer.draw(mealType, sameTypeMeals));
    }

    private Flux<Map<MealType, MealSummary>> correlateMealsForTheSameDay(
            List<Flux<DrawnMealInfo>> sameTypeMealsPublishers) {
        return Flux.zip(sameTypeMealsPublishers, this::collectInMap);
    }

    private Map<MealType, MealSummary> collectInMap(Object[] sameDayMeals) {
        return Arrays.stream(sameDayMeals)
                .map(DrawnMealInfo.class::cast)
                .collect(Collectors.toMap(DrawnMealInfo::getMealType, DrawnMealInfo::getMealSummary));
    }

    private Flux<DayPlan> createDayPlans(Flux<Integer> consecutiveWeekDays,
                                         Flux<Map<MealType, MealSummary>> mealPlans) {
        return consecutiveWeekDays
                .zipWith(mealPlans, DayPlan::new)
                .subscribeOn(Schedulers.parallel());
    }

    private Flux<Meal> getRequestedMeals(Collection<Long> mealIds) {
        return groupMealIdsByCount(mealIds)
                .flatMap(this::getMealsById);
    }

    private Flux<MealIdCountSummary> groupMealIdsByCount(Collection<Long> mealIds) {
        return mealIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(MealIdCountSummary::fromEntry)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Flux::fromIterable));
    }

    private Flux<Meal> getMealsById(MealIdCountSummary idCountSummary) {
        return mealService.getById(idCountSummary.id)
                .flatMapIterable(meal -> Collections.nCopies(idCountSummary.numberOfOccurences, meal));
    }

    private Flux<GroupedFlux<Product, Ingredient>> groupIngredientsByProduct(Flux<Meal> meals) {
        return meals
                .flatMapIterable(Meal::getIngredients)
                .groupBy(Ingredient::getProduct);
    }

    private Mono<GroceryItem> createGroceryItem(GroupedFlux<Product, Ingredient> ingredientsByProduct) {
        return Mono.justOrEmpty(ingredientsByProduct.key())
                .flatMap(product -> createGroceryItem(product, ingredientsByProduct));
    }

    private Mono<GroceryItem> createGroceryItem(Product product, Flux<Ingredient> sameProductIngredients) {
        return calculateTotalNumberOfPrimaryUnits(sameProductIngredients)
                .map(numberOfUnits ->
                    new GroceryItem(product.toSummary(), numberOfUnits, product.getPrimaryUnit()));
    }

    private Mono<BigDecimal> calculateTotalNumberOfPrimaryUnits(Flux<Ingredient> sameProductIngredients) {
        return sameProductIngredients
                .map(NumberOfPrimaryUnitsCalculator::calculate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static final class MealIdCountSummary {

        static MealIdCountSummary fromEntry(Map.Entry<Long, Long> idToCount) {
            return new MealIdCountSummary(idToCount.getKey(), idToCount.getValue().intValue());
        }

        private final Long id;
        private final Integer numberOfOccurences;

        MealIdCountSummary(Long id, Integer numberOfOccurences) {
            this.id = id;
            this.numberOfOccurences = numberOfOccurences;
        }
    }
}
