package com.github.pannowak.mealsadvisor.gui.embedded;

import javafx.fxml.FXMLLoader;
import net.rgielen.fxweaver.core.FxmlView;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Predicate;

public class EmbeddedViewProvider<T> {

    public static <T> T getView(Object controller) {
        try {
            return new EmbeddedViewProvider<T>(controller).loadView();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private final Object controller;

    public EmbeddedViewProvider(Object controller) {
        this.controller = controller;
    }

    public T loadView() throws IOException {
        String fxmlReference = buildFxmlReference(controller.getClass());
        return loadView(fxmlReference);
    }

    private String buildFxmlReference(Class<?> c) {
        return Optional.ofNullable(c.getAnnotation(FxmlView.class))
                .map(FxmlView::value)
                .filter(Predicate.not(String::isEmpty))
                .orElse(c.getSimpleName() + ".fxml");
    }

    private T loadView(String fxmlReference) throws IOException {
        var loader = new FXMLLoader(controller.getClass().getResource(fxmlReference));
        loader.setControllerFactory(clazz -> controller);
        return loader.load();
    }
}
