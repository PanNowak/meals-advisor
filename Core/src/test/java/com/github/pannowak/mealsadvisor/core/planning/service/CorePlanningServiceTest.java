package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.api.exception.ClientException;
import com.github.pannowak.mealsadvisor.api.meals.model.Ingredient;
import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.core.MockClientException;
import com.github.pannowak.mealsadvisor.core.meals.service.CoreMealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CorePlanningServiceTest {

    private static final MealType BREAKFAST = createFakeMealType(1L, "Breakfast");
    private static final MealType LUNCH = createFakeMealType(2L, "Lunch");

    private static final List<MealType> TEST_MEAL_TYPES = List.of(BREAKFAST, LUNCH);

    private static final List<MealSummary> TEST_MEAL_SUMMARIES = List.of(
            new MealSummary(1L, "TestMeal1"),
            new MealSummary(2L, "TestMeal2"),
            new MealSummary(3L, "TestMeal3"),
            new MealSummary(4L, "TestMeal4"),
            new MealSummary(5L, "TestMeal5")
    );

    private static final List<MealSummary> BREAKFASTS = List.of(
            TEST_MEAL_SUMMARIES.get(0), TEST_MEAL_SUMMARIES.get(2), TEST_MEAL_SUMMARIES.get(4)
    );

    private static final List<MealSummary> LUNCHES = List.of(
            TEST_MEAL_SUMMARIES.get(1), TEST_MEAL_SUMMARIES.get(2),
            TEST_MEAL_SUMMARIES.get(3), TEST_MEAL_SUMMARIES.get(4)
    );

    private static final Unit TEST_UNIT1 = createFakeUnit("TestUnit1");
    private static final Unit TEST_UNIT2 = createFakeUnit("TestUnit2");

    private static final Product TEST_PRODUCT = createFakeProduct();

    private static final Meal TEST_MEAL1 = createFakeMealWithIngredientWithPrimaryUnit();
    private static final Meal TEST_MEAL2 = createFakeMealWithIngredientWithSecondaryUnit();

    @Mock
    private CoreMealService mealService;

    @Mock
    private ConsecutiveWeekDaysGenerator weekDaysGenerator;

    @Mock
    private RandomMealsDrawer randomMealsDrawer;

    private CorePlanningService planningService;

    @BeforeEach
    void setUp() {
        planningService = new CorePlanningService(mealService, weekDaysGenerator, randomMealsDrawer);
    }

    @Test
    void givenSmallRangeOfValidWeekDaysShouldReturnOrderedAndUniqueDayPlans() {
        int firstDay = 2;
        int lastDay = 4;
        Integer[] expectedDays = {2, 3, 4};
        mockWeekDayGeneration(firstDay, lastDay, expectedDays);
        prepareMockMealData();

        List<DayPlan> dayPlans = generateDayPlans(firstDay, lastDay);

        assertAll(
                () -> assertOrderedDays(dayPlans, expectedDays),
                () -> assertUniqueDayPlans(dayPlans)
        );
    }

    @Test
    void givenBigRangeOfValidWeekDaysShouldReturnOrderedAndSometimesRepeatableDayPlans() {
        int firstDay = 1;
        int lastDay = 6;
        Integer[] expectedDays = {1, 2, 3, 4, 5, 6};
        mockWeekDayGeneration(firstDay, lastDay, expectedDays);
        prepareMockMealData();

        List<DayPlan> dayPlans = generateDayPlans(firstDay, lastDay);

        assertAll(
                () -> assertOrderedDays(dayPlans, expectedDays),
                () -> assertAtMostTwoDuplicates(dayPlans)
        );
    }

    @Test
    void givenValidRangeOfWeekDaysFromTwoWeeksShouldReturnOrderedDayPlans() {
        int firstDay = 5;
        int lastDay = 2;
        Integer[] expectedDays = {5, 6, 7, 1, 2};
        mockWeekDayGeneration(firstDay, lastDay, expectedDays);
        prepareMockMealData();

        List<DayPlan> dayPlans = generateDayPlans(firstDay, lastDay);

        assertOrderedDays(dayPlans, expectedDays);
    }

    @Test
    void givenOnlyOneMealForAnyOfTheTypesShouldReturnThatMealsRepeatedly() {
        int firstDay = 2;
        int lastDay = 4;
        Integer[] expectedDays = {2, 3, 4};
        mockWeekDayGeneration(firstDay, lastDay, expectedDays);
        prepareSingleMockMealData();

        List<DayPlan> dayPlans = generateDayPlans(firstDay, lastDay);

        assertAll(
                () -> assertOrderedDays(dayPlans, expectedDays),
                () -> assertOneMealForEveryType(dayPlans)
        );
    }

    @Test
    void givenZeroMealsForAnyOfTheTypesShouldThrow() {
        int firstDay = 2;
        int lastDay = 4;
        Integer[] expectedDays = {2, 3, 4};
        mockWeekDayGeneration(firstDay, lastDay, expectedDays);
        prepareEmptyMockMealDataForOneOfTheTypes();

        StepVerifier.create(planningService.draw(firstDay, lastDay))
                .expectError(ClientException.class)
                .verify();
    }

    @Test
    void givenWeekDaysGeneratorEmitsErrorShouldPropagateIt() {
        given(mealService.getAllTypes()).willReturn(Flux.fromIterable(TEST_MEAL_TYPES));
        given(weekDaysGenerator.generate(anyInt(), anyInt()))
                .willReturn(Flux.error(new MockClientException()));

        StepVerifier.create(planningService.draw(1, 4))
                .expectError(ClientException.class)
                .verify();
    }

    @Test
    void givenIdOfMealWithPrimaryUnitIngredientShouldGenerateShoppingList() {
        given(mealService.getById(eq(1L))).willReturn(Mono.just(TEST_MEAL1));

        Flux<GroceryItem> shoppingList = planningService.generateShoppingList(List.of(1L));

        assertShoppingListContains(shoppingList,
                new GroceryItem(TEST_PRODUCT.toSummary(), BigDecimal.valueOf(3), TEST_UNIT1));
    }

    @Test
    void givenIdOfMealWithSecondaryUnitIngredientShouldGenerateShoppingList() {
        given(mealService.getById(eq(2L))).willReturn(Mono.just(TEST_MEAL2));

        Flux<GroceryItem> shoppingList = planningService.generateShoppingList(List.of(2L));

        assertShoppingListContains(shoppingList,
                new GroceryItem(TEST_PRODUCT.toSummary(), BigDecimal.valueOf(4), TEST_UNIT1));
    }

    @Test
    void givenIdOfDifferentMealsShouldGenerateShoppingList() {
        given(mealService.getById(eq(1L))).willReturn(Mono.just(TEST_MEAL1));
        given(mealService.getById(eq(2L))).willReturn(Mono.just(TEST_MEAL2));

        Flux<GroceryItem> shoppingList = planningService.generateShoppingList(List.of(1L, 2L));

        assertShoppingListContains(shoppingList,
                new GroceryItem(TEST_PRODUCT.toSummary(), BigDecimal.valueOf(7), TEST_UNIT1));
    }

    @Test
    void givenIdOfMealThatDoesNotExistShouldThrow() {
        given(mealService.getById(eq(3L)))
                .willReturn(Mono.error(new MockClientException()));

        StepVerifier.create(planningService.generateShoppingList(List.of(3L)))
                .expectError(ClientException.class)
                .verify();
    }

    private static MealType createFakeMealType(long id, String name) {
        var mealType = new MealType();
        mealType.setId(id);
        mealType.setName(name);
        return mealType;
    }

    private static Meal createFakeMealWithIngredientWithPrimaryUnit() {
        var meal = new Meal();
        meal.setId(1L);
        meal.setName("PrimaryUnitMeal");

        var ingredient = new Ingredient(TEST_PRODUCT, BigDecimal.valueOf(3), TEST_UNIT1);
        ingredient.setMeal(meal);
        meal.addIngredient(ingredient);
        return meal;
    }

    private static Meal createFakeMealWithIngredientWithSecondaryUnit() {
        var meal = new Meal();
        meal.setId(1L);
        meal.setName("SecondaryUnitMeal");

        var ingredient = new Ingredient(TEST_PRODUCT, BigDecimal.valueOf(2), TEST_UNIT2);
        ingredient.setMeal(meal);
        meal.addIngredient(ingredient);
        return meal;
    }

    private static Unit createFakeUnit(String name) {
        var unit = new Unit(name);
        unit.setId((long) (Math.random() * 100));
        return unit;
    }

    private static Product createFakeProduct() {
        var product = new Product();
        product.setId(151L);
        product.setName("TestProduct");
        product.setPrimaryUnit(TEST_UNIT1);
        product.setSecondaryUnits(Set.of(new SecondaryUnitInfo(TEST_UNIT2,  BigDecimal.valueOf(0.5))));
        return product;
    }

    private void mockWeekDayGeneration(int firstDay, int lastDay, Integer[] expectedDays) {
        given(weekDaysGenerator.generate(firstDay, lastDay)).willReturn(Flux.fromArray(expectedDays));
    }

    private void prepareMockMealData() {
        given(mealService.getAllTypes()).willReturn(Flux.fromIterable(TEST_MEAL_TYPES));
        given(mealService.getAllByType(eq(BREAKFAST))).willReturn(Flux.fromIterable(BREAKFASTS));
        given(mealService.getAllByType(eq(LUNCH))).willReturn(Flux.fromIterable(LUNCHES));
    }

    private void prepareSingleMockMealData() {
        given(mealService.getAllTypes()).willReturn(Flux.fromIterable(TEST_MEAL_TYPES));
        given(mealService.getAllByType(eq(BREAKFAST))).willReturn(Flux.fromIterable(BREAKFASTS).take(1));
        given(mealService.getAllByType(eq(LUNCH))).willReturn(Flux.fromIterable(LUNCHES).take(1));
    }

    private void prepareEmptyMockMealDataForOneOfTheTypes() {
        given(mealService.getAllTypes()).willReturn(Flux.fromIterable(TEST_MEAL_TYPES));
        given(mealService.getAllByType(eq(BREAKFAST))).willReturn(Flux.fromIterable(BREAKFASTS));
        given(mealService.getAllByType(eq(LUNCH))).willReturn(Flux.empty());
    }

    private List<DayPlan> generateDayPlans(int firstDay, int lastDay) {
        return planningService.draw(firstDay, lastDay)
                .collectList()
                .blockOptional(Duration.ofSeconds(2))
                .orElse(Collections.emptyList());
    }

    private void assertOrderedDays(List<DayPlan> dayPlans, Integer... expectedDays) {
        List<Integer> weekDays = dayPlans.stream()
                .map(DayPlan::getDayOfWeek)
                .collect(Collectors.toList());
        assertThat(weekDays).containsExactly(expectedDays);
    }

    private void assertUniqueDayPlans(List<DayPlan> dayPlans) {
        Map<MealType, List<MealSummary>> mealsByTypes = extractMealsByTypes(dayPlans);
        assertUniqueMealsInEveryType(mealsByTypes);
    }

    private Map<MealType, List<MealSummary>> extractMealsByTypes(List<DayPlan> dayPlans) {
        return dayPlans.stream()
                .map(DayPlan::getMealsByType)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    private void assertUniqueMealsInEveryType(Map<MealType, List<MealSummary>> mealsByTypes) {
        mealsByTypes.values()
                .forEach(meals -> assertThat(meals).doesNotHaveDuplicates());
    }

    private void assertAtMostTwoDuplicates(List<DayPlan> dayPlans) {
        Map<MealType, List<MealSummary>> mealsByTypes = extractMealsByTypes(dayPlans);
        mealsByTypes.values().forEach(this::assertAtMostTwoDuplicatesInEveryType);
    }

    private void assertAtMostTwoDuplicatesInEveryType(List<MealSummary> meals) {
        for (MealSummary uniqueMeal : new HashSet<>(meals)) {
            long mealCount = getNumberOfDuplicates(uniqueMeal, meals);
            assertThat(mealCount).as(uniqueMeal.toString()).isLessThanOrEqualTo(2);
        }
    }

    private long getNumberOfDuplicates(MealSummary uniqueMeal, List<MealSummary> meals) {
        return meals.stream()
                .filter(uniqueMeal::equals)
                .count();
    }

    private void assertOneMealForEveryType(List<DayPlan> dayPlans) {
        Map<MealType, List<MealSummary>> mealsByTypes = extractMealsByTypes(dayPlans);
        mealsByTypes.values().forEach(this::assertOneMealInEveryType);
    }

    private void assertOneMealInEveryType(List<MealSummary> meals) {
        var uniqueMeals = new HashSet<>(meals);
        assertThat(uniqueMeals).hasSize(1);
    }

    private void assertShoppingListContains(Flux<GroceryItem> actualShoppingList,
                                            GroceryItem... expectedItems) {
        StepVerifier.create(actualShoppingList)
                .expectNext(expectedItems)
                .expectComplete()
                .verify();
    }
}
