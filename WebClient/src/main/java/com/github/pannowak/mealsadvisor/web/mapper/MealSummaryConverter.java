package com.github.pannowak.mealsadvisor.web.mapper;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.web.api.model.MealSummaryDTO;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

@Component
class MealSummaryConverter extends AbstractConverter<MealSummaryDTO, MealSummary> {

    @Override
    protected MealSummary convert(MealSummaryDTO source) {
        return new MealSummary(source.getId(), source.getName());
    }
}
