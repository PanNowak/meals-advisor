package com.github.pannowak.mealsadvisor.gui.observer;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager.hideCurrentProgressIndicator;
import static com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager.showCurrentProgressIndicator;

@Slf4j
public final class DataManipulationObserver implements CompletableObserver {

    private final Runnable successAction;
    private final Consumer<Throwable> errorConsumer;

    public DataManipulationObserver(Runnable successAction, Consumer<Throwable> errorConsumer) {
        this.successAction = successAction;
        this.errorConsumer = errorConsumer;
    }

    @Override
    public void onSubscribe(Disposable d) {
        showCurrentProgressIndicator();
    }

    @Override
    public void onComplete() {
        successAction.run();
        hideCurrentProgressIndicator();
    }

    @Override
    public void onError(Throwable e) {
        errorConsumer.accept(e);
        hideCurrentProgressIndicator();
        log.error(e.getMessage(), e);
    }
}
