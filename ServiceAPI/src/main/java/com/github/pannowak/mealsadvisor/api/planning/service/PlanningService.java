package com.github.pannowak.mealsadvisor.api.planning.service;

import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface PlanningService {

    Flux<DayPlan> draw(int firstDay, int lastDay);

    Flux<GroceryItem> generateShoppingList(Collection<Long> mealIds);
}
