package com.github.pannowak.mealsadvisor.web.service;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.meals.service.MealService;
import com.github.pannowak.mealsadvisor.web.api.model.MealDTO;
import com.github.pannowak.mealsadvisor.web.client.MealClient;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestMealService implements MealService {

    private final MealClient mealClient;
    private final ModelTransformer modelTransformer;

    RestMealService(MealClient mealClient, ModelTransformer modelTransformer) {
        this.mealClient = mealClient;
        this.modelTransformer = modelTransformer;
    }

    @Override
    public Flux<MealSummary> getAll() {
        return mealClient.getAll()
                .map(modelTransformer.to(MealSummary.class));
    }

    @Override
    public Flux<MealType> getAllTypes() {
        return mealClient.getAllTypes()
                .map(modelTransformer.to(MealType.class));
    }

    @Override
    public Mono<Meal> getById(Long id) {
        return mealClient.getById(id)
                .map(modelTransformer.to(Meal.class));
    }

    @Override
    public Mono<Meal> save(Meal meal) {
        return Mono.just(meal)
                .map(modelTransformer.to(MealDTO.class))
                .transform(mealDto -> saveInternal(meal.getId(), mealDto))
                .map(modelTransformer.to(Meal.class));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return mealClient.deleteById(id);
    }

    private Mono<MealDTO> saveInternal(Long id, Mono<MealDTO> mealDto) {
        if (id == null) {
            return mealClient.create(mealDto);
        } else {
            return mealClient.update(id, mealDto);
        }
    }
}
