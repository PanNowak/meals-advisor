package com.github.pannowak.mealsadvisor.web.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.service.MealService;
import com.github.pannowak.mealsadvisor.web.api.model.MealDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealSummaryDTO;
import com.github.pannowak.mealsadvisor.web.api.model.MealTypeDTO;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/meals")
public class MealController {

    private final MealService mealService;
    private final ModelTransformer modelTransformer;

    MealController(MealService mealService, ModelTransformer modelTransformer) {
        this.mealService = mealService;
        this.modelTransformer = modelTransformer;
    }

    @ApiOperation("Returns short representations of all the meals")
    @ApiResponse(responseCode = "200", description = "Meals queried successfully")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MealSummaryDTO> getAllMealSummaries() {
        return mealService.getAll()
                .map(modelTransformer.to(MealSummaryDTO.class));
    }

    @ApiOperation("Returns all meal types")
    @ApiResponse(responseCode = "200", description = "Meal types queried successfully")
    @GetMapping(path = "/types", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<MealTypeDTO> getAllMealTypes() {
        return mealService.getAllTypes()
                .map(modelTransformer.to(MealTypeDTO.class));
    }

    @ApiOperation("Returns detailed information about a single meal")
    @ApiResponse(responseCode = "200", description = "Meal queried successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Meal not found")
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<MealDTO> getById(@PathVariable("id") Long mealId) {
        return mealService.getById(mealId)
                .map(modelTransformer.to(MealDTO.class));
    }

    @ApiOperation("Creates a new meal")
    @ApiResponse(responseCode = "201", description = "Meal created successfully")
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MealDTO> save(@RequestBody Mono<MealDTO> mealDTO) {
        return mealDTO
                .map(modelTransformer.to(Meal.class))
                .doOnNext(meal -> meal.setId(null))
                .flatMap(mealService::save)
                .map(modelTransformer.to(MealDTO.class));
    }

    @ApiOperation("Updates/replaces existing meal")
    @ApiResponse(responseCode = "200", description = "Meal updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @ApiResponse(responseCode = "404", description = "Meal not found")
    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<MealDTO> update(@PathVariable("id") Long mealId, @RequestBody Mono<MealDTO> mealDTO) {
        return mealDTO
                .map(modelTransformer.to(Meal.class))
                .doOnNext(meal -> meal.setId(mealId))
                .flatMap(mealService::save)
                .map(modelTransformer.to(MealDTO.class));
    }

    @ApiOperation("Deletes a meal")
    @ApiResponse(responseCode = "204", description = "Meal deleted successfully")
    @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") Long mealId) {
        return mealService.deleteById(mealId);
    }
}
