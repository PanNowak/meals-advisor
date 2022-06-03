package com.github.pannowak.mealsadvisor.gui.views.meals.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.Ingredient;
import com.github.pannowak.mealsadvisor.api.meals.model.Meal;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.display.SimpleCellValueFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleTableCellFactory;
import com.github.pannowak.mealsadvisor.gui.embedded.EmbeddedViewProvider;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.ProductGateway;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.tuple.Triple;
import org.controlsfx.control.PopOver;
import org.fxmisc.easybind.EasyBind;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@Controller
@FxmlView("ingredient-table-view.fxml")
public class IngredientTableViewController {

    @FXML
    private TableColumn<Ingredient, Product> productColumn;

    @FXML
    private TableColumn<Ingredient, BigDecimal> amountColumn;

    @FXML
    private TableColumn<Ingredient, Unit> unitColumn;

    @FXML
    private TableView<Ingredient> ingredientTable;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    private final ProductGateway productGateway;
    private final Router router;

    IngredientTableViewController(ProductGateway productGateway, Router router) {
        this.productGateway = productGateway;
        this.router = router;
    }

    @FXML
    public void initialize() {
        Triple<Meal, List<MealType>, List<ProductSummary>> data = router.getData();
        setUpControls(new FilteredList<>(FXCollections.observableArrayList(data.getRight())));
    }

    public ObservableList<Ingredient> getIngredients() {
        return ingredientTable.getItems();
    }

    public void setIngredients(Collection<Ingredient> ingredients) {
        ingredientTable.getItems().addAll(ingredients);
    }

    @FXML
    public void removeIngredient() {
        ingredientTable.getItems().remove(ingredientTable.getSelectionModel().getSelectedItem());
    }

    private void setUpControls(FilteredList<ProductSummary> remainingProducts) {
        setUpIngredientTable(remainingProducts);
        setUpButtons(remainingProducts);
    }

    private void setUpIngredientTable(FilteredList<ProductSummary> remainingProducts) {
        productColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(Ingredient::getProduct));
        productColumn.setCellFactory(SimpleTableCellFactory.getFactory(Product::getName));
        productColumn.setComparator(Comparator.comparing(Product::getName));

        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl"));
        amountColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(Ingredient::getNumberOfUnits));
        amountColumn.setCellFactory(SimpleTableCellFactory.getFactory(formatter::format));

        unitColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(Ingredient::getUnit));
        unitColumn.setCellFactory(SimpleTableCellFactory.getFactory(Unit::getName));
        unitColumn.setComparator(Comparator.comparing(Unit::getName));

        remainingProducts.predicateProperty().bind(productsNotYetChosenObservable());
    }

    private ObservableValue<Predicate<ProductSummary>> productsNotYetChosenObservable() {
        var chosenProducts = new SimpleListProperty<>(ingredientTable.getItems());
        return EasyBind.map(chosenProducts, this::prepareProductsNotYetChosenPredicate);
    }

    private Predicate<ProductSummary> prepareProductsNotYetChosenPredicate(List<Ingredient> chosenIngredients) {
        Set<ProductSummary> allChosenProducts = gatherAlreadyChosenProducts(chosenIngredients);
        return Predicate.not(allChosenProducts::contains);
    }

    private Set<ProductSummary> gatherAlreadyChosenProducts(List<Ingredient> chosenIngredients) {
        return chosenIngredients.stream()
                .map(Ingredient::getProduct)
                .map(Product::toSummary)
                .collect(Collectors.toSet());
    }

    private void setUpButtons(ObservableList<ProductSummary> remainingProducts) {
        var popupController = new AddIngredientPopupController(
                productGateway, remainingProducts, ingredientTable.getItems());
        addButton.setOnAction(e -> showPopup(popupController));
        addButton.disableProperty().bind(productCondition(remainingProducts));

        deleteButton.disableProperty().bind(ingredientTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void showPopup(AddIngredientPopupController popupController) {
        PopOver popOver = EmbeddedViewProvider.getView(popupController);
        popOver.show(addButton);
    }

    private ObservableValue<Boolean> productCondition(ObservableList<ProductSummary> remainingProducts) {
        return new SimpleListProperty<>(remainingProducts).emptyProperty();
    }
}
