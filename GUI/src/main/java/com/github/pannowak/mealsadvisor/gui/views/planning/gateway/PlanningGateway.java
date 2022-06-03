package com.github.pannowak.mealsadvisor.gui.views.planning.gateway;

import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.planning.service.PlanningService;
import com.github.pannowak.mealsadvisor.gui.Gateway;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Gateway
public class PlanningGateway {

    private final PlanningService planningService;
    private final ObservableList<Integer> weekDays;

    PlanningGateway(PlanningService planningService) {
        this.planningService = planningService;
        this.weekDays = FXCollections.observableArrayList();
    }

    @PostConstruct
    public void init() {
        IntStream.rangeClosed(1, 7).forEach(weekDays::add);
    }

    public ObservableList<Integer> getAllWeekDays() {
        return FXCollections.unmodifiableObservableList(weekDays);
    }

    public String getDayOfWeekDisplayName(int dayOfWeek) {
        return DayOfWeek.of(dayOfWeek)
                .getDisplayName(TextStyle.FULL, Locale.forLanguageTag("pl"))
                .transform(StringUtils::capitalize);
    }

    public Observable<DayPlan> draw(int firstDay, int lastDay) {
        return Flowable.fromCallable(() -> planningService.draw(firstDay, lastDay))
                .observeOn(Schedulers.computation())
                .flatMap(entities -> entities)
                .observeOn(JavaFxScheduler.platform())
                .toObservable();
    }

    public Single<List<GroceryItem>> generateShoppingList(Collection<Long> mealIds) {
        return Flowable.just(mealIds)
                .observeOn(Schedulers.computation())
                .flatMap(planningService::generateShoppingList)
                .observeOn(JavaFxScheduler.platform())
                .toList();
    }
}
