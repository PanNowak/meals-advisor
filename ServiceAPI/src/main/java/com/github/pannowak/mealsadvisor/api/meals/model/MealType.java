package com.github.pannowak.mealsadvisor.api.meals.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
public class MealType {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;
}
