package com.github.pannowak.mealsadvisor.core.meals.repository;

import com.github.pannowak.mealsadvisor.core.meals.model.MealTypeEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MealTypeRepository extends CrudRepository<MealTypeEntity, Long> {

    @Override
    List<MealTypeEntity> findAll();
}
