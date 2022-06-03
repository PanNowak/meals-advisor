package com.github.pannowak.mealsadvisor.api.meals.model;

import lombok.Value;

import com.github.pannowak.mealsadvisor.api.Summary;

@Value
public class MealSummary implements Summary {

    Long id;
    String name;
}
