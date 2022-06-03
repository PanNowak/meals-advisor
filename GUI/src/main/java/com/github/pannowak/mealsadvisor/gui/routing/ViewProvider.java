package com.github.pannowak.mealsadvisor.gui.routing;

import javafx.scene.Node;
import javafx.scene.image.Image;

public final class ViewProvider {

    private final String description;
    private final Class<?> controllerType;
    private final ViewFactory viewFactory;
    private final Image image;

    ViewProvider(String description, Class<?> controllerType,
                 ViewFactory viewFactory, Image image) {
        this.description = description;
        this.controllerType = controllerType;
        this.viewFactory = viewFactory;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public Class<?> getControllerType() {
        return controllerType;
    }

    public Node getView() {
        return viewFactory.getView(controllerType);
    }

    public Image getImage() {
        return image;
    }
}
