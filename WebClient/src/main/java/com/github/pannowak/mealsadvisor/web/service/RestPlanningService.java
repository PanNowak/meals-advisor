package com.github.pannowak.mealsadvisor.web.service;

import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.planning.service.PlanningService;
import com.github.pannowak.mealsadvisor.web.client.PlanningClient;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Service
public class RestPlanningService implements PlanningService {

    private final PlanningClient planningClient;
    private final ModelTransformer modelTransformer;

    RestPlanningService(PlanningClient planningClient, ModelTransformer modelTransformer) {
        this.planningClient = planningClient;
        this.modelTransformer = modelTransformer;
    }

    @Override
    public Flux<DayPlan> draw(int firstDay, int lastDay) {
        return planningClient.draw(firstDay, lastDay)
                .map(modelTransformer.to(DayPlan.class));
    }

    @Override
    public Flux<GroceryItem> generateShoppingList(Collection<Long> mealIds) {
        return planningClient.generateShoppingList(mealIds)
                .map(modelTransformer.to(GroceryItem.class));
    }
}
