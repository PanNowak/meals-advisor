package com.github.pannowak.mealsadvisor.api.planning.model;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;

@ToString
@EqualsAndHashCode
public final class DayPlan {

    private final Integer dayOfWeek;
    private final Map<MealType, MealSummary> mealsByType;

    public DayPlan(Integer dayOfWeek, Map<MealType, MealSummary> mealsByType) {
        this.dayOfWeek = dayOfWeek;
        this.mealsByType = mealsByType;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public Optional<MealSummary> getMeal(MealType type) {
        return Optional.ofNullable(mealsByType.get(type));
    }

    public Collection<MealSummary> getAllMeals() {
        return Collections.unmodifiableCollection(mealsByType.values());
    }

    public Map<MealType, MealSummary> getMealsByType() {
        return Collections.unmodifiableMap(mealsByType);
    }
}
