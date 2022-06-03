package com.github.pannowak.mealsadvisor.gui.routing;

import javafx.scene.Node;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ViewFactory {

    private final Map<Class<?>, Node> viewsByControllerType;
    private final FxWeaver fxWeaver;

    private ResourceBundle currentBundle;

    ViewFactory(FxWeaver fxWeaver) {
        this.viewsByControllerType = new ConcurrentHashMap<>();
        this.fxWeaver = fxWeaver;
    }

    public Node getView(Class<?> controllerType) {
        return viewsByControllerType.computeIfAbsent(controllerType, this::loadView);
    }

    public void removeCachedView(Class<?> controllerType) {
        viewsByControllerType.remove(controllerType);
    }

    public void setResourceBundle(ResourceBundle resourceBundle) {
        viewsByControllerType.clear();
        this.currentBundle = resourceBundle;
    }

    private Node loadView(Class<?> controllerType) {
        return fxWeaver.loadView(controllerType, currentBundle);
    }
}
