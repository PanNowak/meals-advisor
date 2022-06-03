package com.github.pannowak.mealsadvisor.web.mapper;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.web.api.model.DayPlanDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealSummaryDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealTypeDTO;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
class DayPlanConverter extends AbstractConverter<DayPlanDTO, DayPlan> {

    private final MealSummaryConverter mealSummaryConverter;
    private final ModelMapper modelMapper;

    DayPlanConverter(MealSummaryConverter mealSummaryConverter) {
        this.mealSummaryConverter = mealSummaryConverter;
        this.modelMapper = new ModelMapper();
    }

    @Override
    protected DayPlan convert(DayPlanDTO source) {
        Map<MealType, MealSummary> mealsByType = convertMealInfo(source);
        return new DayPlan(source.getDayOfWeek(), mealsByType);
    }

    private Map<MealType, MealSummary> convertMealInfo(DayPlanDTO source) {
        return source.getMealsByType().entrySet().stream()
                .map(this::transformEntry)
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    private Entry<MealType, MealSummary> transformEntry(Entry<MealTypeDTO, MealSummaryDTO> entry) {
        var mealType = modelMapper.map(entry.getKey(), MealType.class);
        var mealSummary = mealSummaryConverter.convert(entry.getValue());
        return new AbstractMap.SimpleEntry<>(mealType, mealSummary);
    }
}
