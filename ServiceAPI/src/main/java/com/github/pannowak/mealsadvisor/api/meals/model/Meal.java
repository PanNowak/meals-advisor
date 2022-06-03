package com.github.pannowak.mealsadvisor.api.meals.model;

import com.github.pannowak.mealsadvisor.api.Summarizable;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
public class Meal implements Summarizable<MealSummary> {

    private Long id;

    @EqualsAndHashCode.Include
    private String name;
    private Collection<Ingredient> ingredients = new ArrayList<>();
    private Set<MealType> mealTypes = new HashSet<>();

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setMeal(this);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setMeal(null);
    }

    public void addType(MealType type) {
        mealTypes.add(type);
    }

    public void removeType(MealType type) {
        mealTypes.remove(type);
    }

    public MealSummary toSummary() {
        return new MealSummary(id, name);
    }
}
