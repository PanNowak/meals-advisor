package com.github.pannowak.mealsadvisor.gui.views.meals.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleStringConverter;
import com.github.pannowak.mealsadvisor.gui.observer.DataManipulationObserver;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.meals.gateway.MealGateway;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.tuple.Triple;
import org.controlsfx.control.CheckComboBox;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Stream;

@Controller
@FxmlView("editing-meal-view.fxml")
public class EditingMealViewController {

    @FXML
    private TextField nameField;

    @FXML
    private CheckComboBox<MealType> mealTypeComboBox;

    @FXML
    private IngredientTableViewController ingredientTableViewController;

    @FXML
    private Button saveButton;

    private Long editedMealId;

    private final MealGateway mealGateway;
    private final AlertFactory alertFactory;
    private final Router router;

    EditingMealViewController(MealGateway mealGateway, AlertFactory alertFactory, Router router) {
        this.mealGateway = mealGateway;
        this.alertFactory = alertFactory;
        this.router = router;
    }

    @FXML
    public void initialize() {
        setUpControls();
        gatherMealData();
    }

    @FXML
    public void saveAndSwitch() {
        var mealToSave = getMealToSave();

        mealGateway.save(mealToSave).subscribe(
                new DataManipulationObserver(this::switchToAllMealsViewInternal, alertFactory::showErrorAlert));
    }

    @FXML
    public void switchToAllMealsView() {
        alertFactory.showConfirmationAlert("Czy na pewno chcesz anulować? Stracisz niezapisane zmiany.",
                this::switchToAllMealsViewInternal);
    }

    private void setUpControls() {
        Triple<Meal, List<MealType>, List<ProductSummary>> data = router.getData();

        mealTypeComboBox.getItems().addAll(data.getMiddle());
        mealTypeComboBox.titleProperty()
                .bind(SelectedTypesObservableFactory.create(mealTypeComboBox.getCheckModel()));
        mealTypeComboBox.setConverter(new SimpleStringConverter<>(MealType::getName));

        saveButton.disableProperty().bind(anyMandatoryParameterUnset());
    }

    private ObservableValue<Boolean> anyMandatoryParameterUnset() {
        return EasyBind.combine(nameField.textProperty().isEmpty(),
                noMealTypeSelected(), noIngredientAdded(), (a, b, c) -> a || b || c);
    }

    private ObservableValue<Boolean> noMealTypeSelected() {
        return new SimpleListProperty<>(mealTypeComboBox.getCheckModel().getCheckedIndices()).emptyProperty();
    }

    private ObservableValue<Boolean> noIngredientAdded() {
        return new SimpleListProperty<>(ingredientTableViewController.getIngredients()).emptyProperty();
    }

    private void gatherMealData() {
        Triple<Meal, List<MealType>, List<ProductSummary>> data = router.getData();
        Meal editedMeal = data.getLeft();
        editedMealId = editedMeal.getId();

        nameField.setText(editedMeal.getName());
        ingredientTableViewController.setIngredients(editedMeal.getIngredients());

        editedMeal.getMealTypes().forEach(type -> mealTypeComboBox.getCheckModel().check(type));
    }

    private Meal getMealToSave() {
        Meal mealToSave = new Meal();
        mealToSave.setId(editedMealId);
        mealToSave.setName(nameField.getText());
        ingredientTableViewController.getIngredients().forEach(mealToSave::addIngredient);
        getSelectedMealTypes().forEach(mealToSave::addType);
        return mealToSave;
    }

    private Stream<MealType> getSelectedMealTypes() {
        var checkModel = mealTypeComboBox.getCheckModel();
        return checkModel.getCheckedIndices().stream().map(checkModel::getItem);
    }

    private void switchToAllMealsViewInternal() {
        router.switchViewClearingly(EditingMealViewController.class,
                AllMealsViewController.class);
    }
}
