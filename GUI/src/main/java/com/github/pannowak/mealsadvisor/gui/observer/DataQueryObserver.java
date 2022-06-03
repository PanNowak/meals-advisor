package com.github.pannowak.mealsadvisor.gui.observer;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager.hideCurrentProgressIndicator;
import static com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager.showCurrentProgressIndicator;

@Slf4j
public final class DataQueryObserver<T> implements SingleObserver<T> {

    private final Consumer<T> queriedItemConsumer;
    private final Consumer<Throwable> errorConsumer;

    public DataQueryObserver(Consumer<T> queriedItemConsumer, Consumer<Throwable> errorConsumer) {
        this.queriedItemConsumer = queriedItemConsumer;
        this.errorConsumer = errorConsumer;
    }

    @Override
    public void onSubscribe(Disposable d) {
        showCurrentProgressIndicator();
    }

    @Override
    public void onSuccess(T queriedItem) {
        queriedItemConsumer.accept(queriedItem);
        hideCurrentProgressIndicator();
    }

    @Override
    public void onError(Throwable e) {
        errorConsumer.accept(e);
        hideCurrentProgressIndicator();
        log.error(e.getMessage(), e);
    }
}
