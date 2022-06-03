package com.github.pannowak.mealsadvisor.gui.display;

import javafx.util.StringConverter;

import java.util.function.Function;

public class SimpleStringConverter<T> extends StringConverter<T> {

    private final Function<T, String> displayValueExtractor;

    public SimpleStringConverter(Function<T, String> displayValueExtractor) {
        this.displayValueExtractor = displayValueExtractor;
    }

    @Override
    public String toString(T object) {
        return object == null ? null : displayValueExtractor.apply(object);
    }

    @Override
    public T fromString(String string) {
        return null;
    }
}
