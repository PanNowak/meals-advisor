package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class MealSummaryDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;
}
