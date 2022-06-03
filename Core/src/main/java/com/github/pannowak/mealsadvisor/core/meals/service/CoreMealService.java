package com.github.pannowak.mealsadvisor.core.meals.service;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.meals.service.MealService;
import com.github.pannowak.mealsadvisor.core.mapper.ModelTransformer;
import com.github.pannowak.mealsadvisor.core.meals.model.MealEntity;
import com.github.pannowak.mealsadvisor.core.meals.model.MealTypeEntity;
import com.github.pannowak.mealsadvisor.core.meals.processor.MealProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Function;

@Service
public class CoreMealService implements MealService {

    private final MealProcessor mealProcessor;
    private final ModelTransformer modelTransformer;
    private final Scheduler databaseScheduler;

    CoreMealService(MealProcessor mealProcessor, ModelTransformer modelTransformer,
                    Scheduler databaseScheduler) {
        this.mealProcessor = mealProcessor;
        this.modelTransformer = modelTransformer;
        this.databaseScheduler = databaseScheduler;
    }

    @Override
    public Flux<MealSummary> getAll() {
        return Mono.fromCallable(mealProcessor::getAll)
                .subscribeOn(databaseScheduler)
                .flatMapIterable(Function.identity());
    }

    @Override
    public Flux<MealType> getAllTypes() {
        return Mono.fromCallable(mealProcessor::getAllTypes)
                .subscribeOn(databaseScheduler)
                .flatMapIterable(Function.identity())
                .map(modelTransformer.to(MealType.class));
    }

    public Flux<MealSummary> getAllByType(MealType mealType) {
        return Mono.just(mealType)
                .map(modelTransformer.to(MealTypeEntity.class))
                .publishOn(databaseScheduler)
                .flatMapIterable(mealProcessor::getAllByType);
    }

    @Override
    public Mono<Meal> getById(Long mealId) {
        return Mono.fromCallable(() -> mealProcessor.getById(mealId))
                .subscribeOn(databaseScheduler)
                .map(modelTransformer.to(Meal.class));
    }

    @Override
    public Mono<Meal> save(Meal mealToSave) {
        return Mono.just(mealToSave)
                .map(modelTransformer.to(MealEntity.class))
                .publishOn(databaseScheduler)
                .map(mealProcessor::save)
                .map(modelTransformer.to(Meal.class));
    }

    @Override
    public Mono<Void> deleteById(Long mealId) {
        return Mono.<Void>fromRunnable(() -> mealProcessor.removeById(mealId))
                .subscribeOn(databaseScheduler);
    }
}
