package com.github.pannowak.mealsadvisor.api;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrudService<S extends Summary, T extends Summarizable<S>> {

    Flux<S> getAll();

    Mono<T> getById(Long id);

    Mono<T> save(T entity);

    Mono<Void> deleteById(Long id);
}
