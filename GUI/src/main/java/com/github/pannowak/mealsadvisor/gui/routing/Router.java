package com.github.pannowak.mealsadvisor.gui.routing;

import javafx.beans.value.ObservableValue;

public interface Router {

    void switchView(Class<?> origin, Class<?> destination, Object data);

    default void switchView(Class<?> origin, Class<?> destination) {
        switchView(origin, destination, null);
    }

    void switchViewClearingly(Class<?> origin, Class<?> destination, Object data);

    default void switchViewClearingly(Class<?> origin, Class<?> destination) {
        switchViewClearingly(origin, destination, null);
    }

    <T> T getData();

//    Optional<Long> getRequestedId();
//
//    List<Long> getRequestedIds();

    ObservableValue<Boolean> isViewActive(Class<?> controllerType);
}
