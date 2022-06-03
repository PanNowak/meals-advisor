package com.github.pannowak.mealsadvisor.api.meals.service;

import com.github.pannowak.mealsadvisor.api.CrudService;
import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import reactor.core.publisher.Flux;

public interface MealService extends CrudService<MealSummary, Meal> {

    Flux<MealType> getAllTypes();
}
