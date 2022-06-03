package com.github.pannowak.mealsadvisor.core.units.service;

import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.api.units.service.UnitService;
import com.github.pannowak.mealsadvisor.core.mapper.ModelTransformer;
import com.github.pannowak.mealsadvisor.core.units.model.UnitEntity;
import com.github.pannowak.mealsadvisor.core.units.processor.UnitProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Function;

@Service
public class CoreUnitService implements UnitService {

    private final UnitProcessor unitProcessor;
    private final ModelTransformer modelTransformer;
    private final Scheduler databaseScheduler;

    CoreUnitService(UnitProcessor unitProcessor, ModelTransformer modelTransformer,
                    Scheduler databaseScheduler) {
        this.unitProcessor = unitProcessor;
        this.modelTransformer = modelTransformer;
        this.databaseScheduler = databaseScheduler;
    }

    @Override
    public Flux<Unit> getAll() {
        return Mono.fromCallable(unitProcessor::getAll)
                .subscribeOn(databaseScheduler)
                .flatMapIterable(Function.identity())
                .map(modelTransformer.to(Unit.class));
    }

    @Override
    public Mono<Unit> getById(Long unitId) {
        return Mono.fromCallable(() -> unitProcessor.getById(unitId))
                .subscribeOn(databaseScheduler)
                .map(modelTransformer.to(Unit.class));
    }

    @Override
    public Mono<Unit> save(Unit unitToSave) {
        return Mono.just(unitToSave)
                .map(modelTransformer.to(UnitEntity.class))
                .publishOn(databaseScheduler)
                .map(unitProcessor::save)
                .map(modelTransformer.to(Unit.class));
    }

    @Override
    public Mono<Void> deleteById(Long unitId) {
        return Mono.<Void>fromRunnable(() -> unitProcessor.removeById(unitId))
                .subscribeOn(databaseScheduler);
    }
}
