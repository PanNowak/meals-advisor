package com.github.pannowak.mealsadvisor.web.service;

import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.api.units.service.UnitService;
import com.github.pannowak.mealsadvisor.web.client.UnitClient;
import com.github.pannowak.mealsadvisor.web.mapper.ModelTransformer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RestUnitService implements UnitService {

    private final UnitClient unitClient;
    private final ModelTransformer modelTransformer;

    RestUnitService(UnitClient unitClient, ModelTransformer modelTransformer) {
        this.unitClient = unitClient;
        this.modelTransformer = modelTransformer;
    }

    @Override
    public Flux<Unit> getAll() {
        return unitClient.getAll()
                .map(modelTransformer.to(Unit.class));
    }

    @Override
    public Mono<Unit> getById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mono<Unit> save(Unit entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        throw new UnsupportedOperationException();
    }
}
