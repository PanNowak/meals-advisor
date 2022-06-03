package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class IngredientDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private BigDecimal numberOfUnits;

    @JsonProperty
    private ProductDTO product;

    @JsonProperty
    private UnitDTO unit;

    @JsonProperty
    @JsonBackReference
    private MealDTO meal;
}
