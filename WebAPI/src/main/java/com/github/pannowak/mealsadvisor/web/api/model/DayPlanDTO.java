package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.pannowak.mealsadvisor.web.api.transform.MealTypeDTODeserializer;
import com.github.pannowak.mealsadvisor.web.api.transform.MealTypeDTOSerializer;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class DayPlanDTO {

    @JsonProperty
    private Integer dayOfWeek;

    @JsonSerialize(keyUsing = MealTypeDTOSerializer.class)
    @JsonDeserialize(keyUsing = MealTypeDTODeserializer.class)
    @JsonProperty
    private Map<MealTypeDTO, MealSummaryDTO> mealsByType = new HashMap<>();
}
