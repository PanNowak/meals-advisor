package com.github.pannowak.mealsadvisor.core.meals.processor;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.core.exception.ExceptionFactory;
import com.github.pannowak.mealsadvisor.core.meals.model.MealEntity;
import com.github.pannowak.mealsadvisor.core.meals.model.MealTypeEntity;
import com.github.pannowak.mealsadvisor.core.meals.repository.MealRepository;
import com.github.pannowak.mealsadvisor.core.meals.repository.MealTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@Slf4j
@Validated
@Component
public class MealProcessor {

    private final MealRepository mealRepository;
    private final MealTypeRepository mealTypeRepository;
    private final ExceptionFactory exceptionFactory;

    MealProcessor(MealRepository mealRepository, MealTypeRepository mealTypeRepository,
                  ExceptionFactory exceptionFactory) {
        this.mealRepository = mealRepository;
        this.mealTypeRepository = mealTypeRepository;
        this.exceptionFactory = exceptionFactory;
    }

    public List<MealSummary> getAll() {
        try {
            return mealRepository.findAllProjectedBy();
        } catch (RuntimeException e) {
            throw exceptionFactory.multiFetchException(MealSummary.class, e);
        }
    }

    public List<MealSummary> getAllByType(MealTypeEntity type) {
        try {
            return mealRepository.findAllProjectedByMealTypes(type);
        } catch (RuntimeException e) {
            throw exceptionFactory.multiFetchException(MealSummary.class, e);
        }
    }

    public List<MealTypeEntity> getAllTypes() {
        try {
            return mealTypeRepository.findAll();
        } catch (RuntimeException e) {
            throw exceptionFactory.multiFetchException(MealType.class, e);
        }
    }

    public MealEntity getById(@Positive Long mealId) {
        return getByIdInternal(mealId).orElseThrow(() ->
                exceptionFactory.entityNotFoundException(Meal.class, mealId));
    }

    public MealEntity save(@Valid MealEntity mealToSave) {
        try {
            return mealRepository.save(mealToSave);
        } catch (RuntimeException e) {
            throw exceptionFactory.savingException(mealToSave, e);
        }
    }

    public void removeById(@Positive Long mealId) {
        try {
            mealRepository.deleteById(mealId);
        } catch (RuntimeException e) {
            handleRemovalException(mealId, e); //TODO
        }
    }

    private Optional<MealEntity> getByIdInternal(Long mealId) {
        try {
            return mealRepository.findById(mealId);
        } catch (RuntimeException e) {
            throw exceptionFactory.singleFetchException(Meal.class, mealId, e);
        }
    }

    private void handleRemovalException(Long mealId, RuntimeException e) {
        if (e instanceof EmptyResultDataAccessException) {
            log.info("Attempt to delete non-existent entity of type {} with id {}", Meal.class.getSimpleName(), mealId);
        } else {
            throw exceptionFactory.removalException(Meal.class, mealId, e);
        }
    }
}
