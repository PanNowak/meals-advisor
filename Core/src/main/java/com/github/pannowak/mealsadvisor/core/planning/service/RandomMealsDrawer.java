package com.github.pannowak.mealsadvisor.core.planning.service;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
class RandomMealsDrawer {

    private final ExceptionFactory exceptionFactory;

    RandomMealsDrawer(ExceptionFactory exceptionFactory) {
        this.exceptionFactory = exceptionFactory;
    }

    public Flux<DrawnMealInfo> draw(MealType mealType, List<MealSummary> sameTypeMeals) {
        return Mono.fromCallable(() -> {
            validateNotEmptyMeals(mealType, sameTypeMeals);
            return convertToDrawnMealInfos(mealType, sameTypeMeals);
        }).flatMapIterable(this::createBackingIterable);
    }

    private void validateNotEmptyMeals(MealType mealType, List<MealSummary> sameTypeMeals) {
        if (sameTypeMeals.isEmpty()) {
            throw exceptionFactory.insufficientDataException(mealType);
        } else {
            log.info("Drawing of meals of type '{}' started ({} meals available in total)",
                    mealType.getName(), sameTypeMeals.size());
        }
    }

    private List<DrawnMealInfo> convertToDrawnMealInfos(MealType mealType,
                                                        List<MealSummary> sameTypeMeals) {
        return sameTypeMeals.stream()
                .map(meal -> new DrawnMealInfo(mealType, meal))
                .collect(Collectors.toList());
    }

    private Iterable<DrawnMealInfo> createBackingIterable(List<DrawnMealInfo> drawnMealInfos) {
        if (drawnMealInfos.size() == 1) {
            return () -> new SingleMealInfiniteIterator(drawnMealInfos.get(0));
        }

        return () -> new ShuffledMealsInfiniteIterator(drawnMealInfos);
    }

    private static abstract class InfiniteMealIterator implements Iterator<DrawnMealInfo> {

        @Override
        public final boolean hasNext() {
            return true;
        }
    }

    private static final class SingleMealInfiniteIterator extends InfiniteMealIterator {

        private final DrawnMealInfo backingDrawnMealInfo;

        SingleMealInfiniteIterator(DrawnMealInfo backingDrawnMealInfo) {
            this.backingDrawnMealInfo = backingDrawnMealInfo;
        }

        @Override
        public DrawnMealInfo next() {
            return backingDrawnMealInfo;
        }
    }

    private static final class ShuffledMealsInfiniteIterator extends InfiniteMealIterator {

        private final List<DrawnMealInfo> drawnMealInfos;
        private Iterator<DrawnMealInfo> currentCycleShuffledMealsIterator;

        ShuffledMealsInfiniteIterator(List<DrawnMealInfo> drawnMealInfos) {
            this.drawnMealInfos = drawnMealInfos;
            this.currentCycleShuffledMealsIterator = createShuffledMealsIterator();
        }

        @Override
        public DrawnMealInfo next() {
            createNextCycleIteratorIfNecessary();
            return currentCycleShuffledMealsIterator.next();
        }

        private void createNextCycleIteratorIfNecessary() {
            if (!currentCycleShuffledMealsIterator.hasNext()) {
                currentCycleShuffledMealsIterator = createShuffledMealsIterator();
            }
        }

        private Iterator<DrawnMealInfo> createShuffledMealsIterator() {
            var drawnMealInfos = new ArrayList<>(this.drawnMealInfos);
            Collections.shuffle(drawnMealInfos);
            return drawnMealInfos.iterator();
        }
    }

    @Value
    static class DrawnMealInfo {

        MealType mealType;
        MealSummary mealSummary;
    }
}
