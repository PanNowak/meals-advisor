package com.github.pannowak.mealsadvisor.web.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

import static com.fasterxml.jackson.annotation.JsonInclude.Include;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class MealDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    @JsonManagedReference
    private Collection<IngredientDTO> ingredients = new ArrayList<>();

    @JsonProperty
    private Collection<MealTypeDTO> mealTypes = new ArrayList<>();
}
