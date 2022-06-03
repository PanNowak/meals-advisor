package com.github.pannowak.mealsadvisor.web.controller;

import com.github.pannowak.mealsadvisor.api.units.service.UnitService;
import com.github.pannowak.mealsadvisor.web.api.model.UnitDTO;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/units")
public class UnitController {

    private final UnitService unitService;
    private final ModelTransformer modelTransformer;

    UnitController(UnitService unitService, ModelTransformer modelTransformer) {
        this.unitService = unitService;
        this.modelTransformer = modelTransformer;
    }

    @ApiOperation("Returns all units")
    @ApiResponse(responseCode = "200", description = "Units queried successfully")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Flux<UnitDTO> getAllUnits() {
        return unitService.getAll()
                .map(modelTransformer.to(UnitDTO.class));
    }
}
