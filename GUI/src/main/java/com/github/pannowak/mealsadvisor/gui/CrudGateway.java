package com.github.pannowak.mealsadvisor.gui;

import com.github.pannowak.mealsadvisor.api.CrudService;
import com.github.pannowak.mealsadvisor.api.Summarizable;
import com.github.pannowak.mealsadvisor.api.Summary;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.context.ApplicationEventPublisher;

public abstract class CrudGateway<S extends Summary, T extends Summarizable<S>> {

    private final CrudService<S, T> service;

    protected CrudGateway(CrudService<S, T> service) {
        this.service = service;
    }

    public Observable<S> getAll() {
        return Flowable.fromCallable(service::getAll)
                .observeOn(Schedulers.computation())
                .flatMap(entities -> entities)
                .observeOn(JavaFxScheduler.platform())
                .toObservable();
    }

    public Single<T> getById(Long id) {
        return Flowable.just(id)
                .observeOn(Schedulers.computation())
                .flatMap(service::getById)
                .observeOn(JavaFxScheduler.platform())
                .singleOrError();
    }

    public Completable save(T entity) {
        return Flowable.just(entity)
                .observeOn(Schedulers.computation())
                .flatMap(service::save)
                .observeOn(JavaFxScheduler.platform())
                .singleOrError()
                .ignoreElement();
    }

    public Completable removeById(Long mealId) {
        return Flowable.just(mealId)
                .observeOn(Schedulers.computation())
                .flatMap(service::deleteById)
                .observeOn(JavaFxScheduler.platform())
                .ignoreElements();
    }
}
