package com.github.pannowak.mealsadvisor.gui.routing;

import com.github.pannowak.mealsadvisor.gui.views.meals.controller.AllMealsViewController;
import com.github.pannowak.mealsadvisor.gui.views.meals.controller.EditingMealViewController;
import com.github.pannowak.mealsadvisor.gui.views.planning.controller.PlanningViewController;
import com.github.pannowak.mealsadvisor.gui.views.planning.controller.ShoppingListViewController;
import com.github.pannowak.mealsadvisor.gui.views.products.controller.AllProductsViewController;
import com.github.pannowak.mealsadvisor.gui.views.products.controller.EditingProductViewController;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ControllerManager implements Router {

    private static final Map<Class<?>, String> DESCRIPTIONS_BY_CONTROLLER_TYPE = Map.of(
            AllMealsViewController.class, "Wyświetl wszystkie posiłki",
            EditingMealViewController.class, "Dodaj / edytuj posiłek",
            PlanningViewController.class, "Wylosuj posiłki na następny tydzień",
            ShoppingListViewController.class, "Wyświetl listę zakupów",
            AllProductsViewController.class, "Wyświetl wszystkie produkty",
            EditingProductViewController.class, "Dodaj / edytuj produkt"
    );

    private static final Map<Class<?>, String> IMAGE_NAMES_BY_CONTROLLER_TYPE = Map.of(
            AllMealsViewController.class, "/all-meals.png",
            EditingMealViewController.class, "/edit-meal.png",
            PlanningViewController.class, "/week-plan.png",
            ShoppingListViewController.class, "/shopping-list.png",
            AllProductsViewController.class, "/all-products.png",
            EditingProductViewController.class, "/edit-product.png"
    );

    private final ViewFactory viewFactory;
    private final ObservableList<ViewProvider> viewProviders;

    private Object data;

    ControllerManager(ViewFactory viewFactory) {
        this.viewFactory = viewFactory;
        this.viewProviders = List.of(
                AllMealsViewController.class,
                PlanningViewController.class,
                AllProductsViewController.class
        ).stream()
                .map(this::getViewProvider)
                .collect(FXCollections::observableArrayList, List::add, List::addAll);
    }

    @Override
    public void switchViewClearingly(Class<?> origin, Class<?> destination, Object data) {
        viewFactory.removeCachedView(origin);
        switchView(origin, destination, data);
    }

    @Override
    public void switchView(Class<?> origin, Class<?> destination, Object data) {
        this.data = data;
        switchViewInternal(origin, destination);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getData() {
        return (T) data;
    }

//    @Override
//    public Optional<Long> getRequestedId() {
//        return Optional.ofNullable((Long) data);
//    }
//
//    @Override
//    public List<Long> getRequestedIds() {
//        return Collections.singletonList((Long) data);
//    }

    @Override
    public ObservableValue<Boolean> isViewActive(Class<?> controllerType) {
        return EasyBind.monadic(new SimpleListProperty<>(viewProviders))
                .map(d -> d.stream().map(ViewProvider::getControllerType).collect(Collectors.toSet()))
                .map(set -> set.contains(controllerType));
    }

    public ObservableList<ViewProvider> getAllViewProviders() {
        return FXCollections.unmodifiableObservableList(viewProviders);
    }

    private void switchViewInternal(Class<?> origin, Class<?> destination) {
        for (var viewProviderIterator = viewProviders.listIterator(); viewProviderIterator.hasNext(); ) {
            var currentControllerType = viewProviderIterator.next().getControllerType();
            if (currentControllerType == origin) {
                var destinationViewProvider = getViewProvider(destination);
                viewProviderIterator.set(destinationViewProvider);
                break;
            }
        }
    }

    private ViewProvider getViewProvider(Class<?> controllerType) {
        String description = DESCRIPTIONS_BY_CONTROLLER_TYPE.get(controllerType);
        String imageName = IMAGE_NAMES_BY_CONTROLLER_TYPE.get(controllerType);
        return new ViewProvider(description, controllerType, viewFactory, new Image(imageName));
    }
}
