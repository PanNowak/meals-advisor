package com.github.pannowak.mealsadvisor.web.utils.model;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;

import java.util.HashMap;
import java.util.Map;

public final class DayPlanBuilder {

    public static MealInfoBuilder createDayPlan(Integer dayOfWeek) {
        return new MealInfoBuilder(dayOfWeek);
    }

    public static final class MealInfoBuilder {

        private final Integer dayOfWeek;
        private final Map<MealType, MealSummary> mealsByType;

        private MealInfoBuilder(Integer dayOfWeek) {
            this.dayOfWeek = dayOfWeek;
            this.mealsByType = new HashMap<>();
        }

        public MealInfoBuilder withMealInfo(TestMealType mealTypeEnum, String mealName) {
            var mealType = mealTypeEnum.mealType;
            mealsByType.put(mealType, new MealSummary(mealType.getId(), mealName));
            return this;
        }

        public DayPlan build() {
            return new DayPlan(dayOfWeek, mealsByType);
        }
    }

    public enum TestMealType {

        BREAKFAST(createMealType(1L, "Breakfast")),
        LUNCH(createMealType(2L, "Lunch")),
        DINNER(createMealType(3L, "Dinner"));

        private final MealType mealType;

        TestMealType(MealType mealType) {
            this.mealType = mealType;
        }

        private static MealType createMealType(Long id, String name) {
            var mealType = new MealType();
            mealType.setId(id);
            mealType.setName(name);
            return mealType;
        }
    }

    private DayPlanBuilder() {}
}
