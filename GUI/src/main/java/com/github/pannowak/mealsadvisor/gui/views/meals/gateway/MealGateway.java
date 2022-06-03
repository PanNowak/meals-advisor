package com.github.pannowak.mealsadvisor.gui.views.meals.gateway;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.meals.service.MealService;
import com.github.pannowak.mealsadvisor.gui.CrudGateway;
import com.github.pannowak.mealsadvisor.gui.Gateway;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;

@Gateway
public class MealGateway extends CrudGateway<MealSummary, Meal> {

    private final MealService mealService;

    MealGateway(MealService mealService) {
        super(mealService);
        this.mealService = mealService;
    }

    public Observable<MealType> getAllTypes() {
        return Flowable.fromCallable(mealService::getAllTypes)
                .observeOn(Schedulers.computation())
                .flatMap(entities -> entities)
                .observeOn(JavaFxScheduler.platform())
                .toObservable();
    }
}
