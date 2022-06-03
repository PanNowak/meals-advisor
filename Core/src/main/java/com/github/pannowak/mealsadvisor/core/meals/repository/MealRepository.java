package com.github.pannowak.mealsadvisor.core.meals.repository;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.core.meals.model.MealEntity;
import com.github.pannowak.mealsadvisor.core.meals.model.MealTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealRepository extends CrudRepository<MealEntity, Long> {

    List<MealSummary> findAllProjectedBy();

    List<MealSummary> findAllProjectedByMealTypes(MealTypeEntity mealType);
}
