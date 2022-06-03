package com.github.pannowak.mealsadvisor.web.controller;

import com.github.pannowak.mealsadvisor.api.planning.service.PlanningService;
import com.github.pannowak.mealsadvisor.web.api.model.DayPlanDTO;
import com.github.pannowak.mealsadvisor.web.api.model.GroceryItemDTO;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping(path = "/planning")
public class PlanningController {

    private final PlanningService planningService;
    private final ModelTransformer modelTransformer;

    PlanningController(PlanningService planningService, ModelTransformer modelTransformer) {
        this.planningService = planningService;
        this.modelTransformer = modelTransformer;
    }

    @ApiOperation("Returns random meal plan for amount of days between 'first-day' " +
            "and 'last-day' parameters (range closed).")
    @ApiResponse(responseCode = "200", description = "Day plans queried successfully")
    @GetMapping(path = "/draw", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<DayPlanDTO> draw(@RequestParam(name = "first-day") int firstDay,
                                 @RequestParam(name = "last-day") int lastDay) {
        return planningService.draw(firstDay, lastDay)
                .map(modelTransformer.to(DayPlanDTO.class));
    }

    @ApiOperation("Returns complete shopping list with all the products necessary to prepare " +
            "meals whose ids have been passed")
    @ApiResponse(responseCode = "200", description = "Shopping list queried successfully")
    @GetMapping(path = "/shopping-list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<GroceryItemDTO> generateShoppingList(
            @RequestParam(name = "meal-id") @ApiParam(allowMultiple = true) List<Long> mealIds) {
        return planningService.generateShoppingList(mealIds)
                .map(modelTransformer.to(GroceryItemDTO.class));
    }
}
