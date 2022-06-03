package com.github.pannowak.mealsadvisor.gui.views.meals.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.observer.DataQueryObserver;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.meals.gateway.MealGateway;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.ProductGateway;
import io.reactivex.Single;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.function.Predicate;

@Controller
@FxmlView("all-meals-view.fxml")
public class AllMealsViewController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<MealSummary> mealsView;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private final MealGateway mealGateway;
    private final ProductGateway productGateway;
    private final AlertFactory alertFactory;
    private final Router router;

    AllMealsViewController(MealGateway mealGateway, ProductGateway productGateway,
                           AlertFactory alertFactory, Router router) {
        this.mealGateway = mealGateway;
        this.productGateway = productGateway;
        this.alertFactory = alertFactory;
        this.router = router;
    }

    @FXML
    public void initialize() {
        setUpControls();
        loadMeals();
    }

    @FXML
    public void switchToAddView() {
        switchToEditingMealView(Single.just(new Meal()));
    }

    @FXML
    public void switchToEditView() {
        Long selectedId = getSelectedMeal().getId();
        switchToEditingMealView(mealGateway.getById(selectedId));
    }

    @FXML
    public void deleteSelectedProduct() {
        MealSummary selectedMeal = getSelectedMeal();

        alertFactory.showConfirmationAlert(
                String.format("Czy jesteś pewien, że chcesz usunąć posiłek \"%s\"?", selectedMeal.getName()),
                () -> removeMeal(selectedMeal.getId()));
    }

    private void setUpControls() {
        setListDisplayLogic();
        setUpButtons();
    }

    private void setListDisplayLogic() {
        mealsView.setCellFactory(SimpleListCellFactory.getFactory(MealSummary::getName));
    }

    private void setUpButtons() {
        ObservableBooleanValue noMealSelected =
                mealsView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noMealSelected);
        deleteButton.disableProperty().bind(noMealSelected);
    }

    private void loadMeals() {
        getAllMeals().subscribe(new DataQueryObserver<>(this::loadMeals, alertFactory::showErrorAlert));
    }

    private Single<FilteredList<MealSummary>> getAllMeals() {
        return mealGateway.getAll()
                .toList()
                .map(FXCollections::observableArrayList)
                .map(FilteredList::new);
    }

    private void loadMeals(FilteredList<MealSummary> meals) {
        insertModelInView(meals);
        setListSearchLogic(meals);
    }

    private void insertModelInView(ObservableList<MealSummary> meals) {
        var sortedMeals = new SortedList<>(meals, Comparator.comparing(MealSummary::getName));
        mealsView.setItems(sortedMeals);
    }

    private void setListSearchLogic(FilteredList<MealSummary> meals) {
        ObservableValue<Predicate<MealSummary>> predicateObservable =
                EasyBind.map(searchField.textProperty(), this::createSearchPredicate);
        meals.predicateProperty().bind(predicateObservable);
    }

    private Predicate<MealSummary> createSearchPredicate(String searchText) {
        return meal -> StringUtils.containsIgnoreCase(meal.getName(), searchText);
    }

    private MealSummary getSelectedMeal() {
        return mealsView.getSelectionModel().getSelectedItem();
    }

    private void switchToEditingMealView(Single<Meal> mealToEdit) {
        Single.zip(mealToEdit, mealGateway.getAllTypes().toList(), productGateway.getAll().toList(), Triple::of)
                .subscribe(new DataQueryObserver<>(
                        triple -> router.switchViewClearingly(AllMealsViewController.class,
                                EditingMealViewController.class, triple),
                        alertFactory::showErrorAlert));
    }

    private void removeMeal(Long mealId) {
        mealGateway.removeById(mealId)
                .andThen(getAllMeals())
                .subscribe(new DataQueryObserver<>(this::loadMeals, alertFactory::showErrorAlert));
    }
}
