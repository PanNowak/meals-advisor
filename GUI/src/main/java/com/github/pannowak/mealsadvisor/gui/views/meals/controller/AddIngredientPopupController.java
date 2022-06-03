package com.github.pannowak.mealsadvisor.gui.views.meals.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.Ingredient;
import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.controls.TextFormatterFactory;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.embedded.EmbeddedController;
import com.github.pannowak.mealsadvisor.gui.observer.DataQueryObserver;
import com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.ProductGateway;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.PopOver;
import org.fxmisc.easybind.EasyBind;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@EmbeddedController
@FxmlView("add-ingredient-popup.fxml")
public class AddIngredientPopupController {

    @FXML
    private VBox mainPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private PopOver popOver;

    @FXML
    private ComboBox<ProductSummary> productComboBox;

    @FXML
    private TextField amountField;

    @FXML
    private ComboBox<Unit> unitComboBox;

    @FXML
    private Button addButton;

    private Product currentlySelectedProduct;

    private final ProductGateway productGateway;
    private final List<ProductSummary> remainingProducts;
    private final ObservableList<Ingredient> chosenIngredients;

    AddIngredientPopupController(ProductGateway productGateway,
                                 List<ProductSummary> remainingProducts,
                                 ObservableList<Ingredient> chosenIngredients) {
        this.productGateway = productGateway;
        this.remainingProducts = remainingProducts;
        this.chosenIngredients = chosenIngredients;
    }

    @FXML
    public void initialize() {
        ProgressIndicatorManager.setCurrentProgressIndicator(progressIndicator);
        popOver.setOnHiding(e -> ProgressIndicatorManager.removeCurrentProgressIndicator());

        setUpProductComboBox();
        setUpTextField();
        setUpUnitComboBox();
        setUpButton();
        mainPane.disableProperty().bind(progressIndicator.visibleProperty());
    }

    @FXML
    public void addIngredient() {
        BigDecimal numberOfUnits = (BigDecimal) amountField.getTextFormatter().getValue();
        Unit selectedUnit = unitComboBox.getSelectionModel().getSelectedItem();
        chosenIngredients.add(new Ingredient(currentlySelectedProduct, numberOfUnits, selectedUnit));
        popOver.hide();
    }

    private void setUpProductComboBox() {
        productComboBox.getItems().addAll(remainingProducts);
        productComboBox.setCellFactory(SimpleListCellFactory.getFactory(ProductSummary::getName));
        productComboBox.setButtonCell(SimpleListCellFactory.createCell(ProductSummary::getName));
        productComboBox.getSelectionModel().selectedItemProperty().addListener(new SelectedProductListener());
    }

    private void setUpTextField() {
        amountField.setTextFormatter(TextFormatterFactory.create(Locale.forLanguageTag("pl")));
    }

    private void setUpUnitComboBox() {
        unitComboBox.setCellFactory(SimpleListCellFactory.getFactory(Unit::getName));
        unitComboBox.setButtonCell(SimpleListCellFactory.createCell(Unit::getName));
        unitComboBox.disableProperty().bind(productComboBox.getSelectionModel().selectedItemProperty().isNull());
    }

    private void setUpButton() {
        var incompleteInputObservable = prepareIncompleteInputObservable();
        addButton.disableProperty().bind(incompleteInputObservable);
    }

    private ObservableValue<Boolean> prepareIncompleteInputObservable() {
        return EasyBind.combine(
                productComboBox.valueProperty().isNull(),
                isZeroAmount(),
                unitComboBox.valueProperty().isNull(),
                (a, b, c) -> a || b || c);
    }

    private ObservableValue<Boolean> isZeroAmount() {
        var valueConverter = amountField.getTextFormatter().getValueConverter();
        return EasyBind.map(amountField.textProperty(), valueConverter::fromString)
                .map(BigDecimal.class::cast)
                .map(amount -> BigDecimal.ZERO.compareTo(amount) == 0);
    }

    private final class SelectedProductListener implements ChangeListener<ProductSummary> {

        @Override
        public void changed(ObservableValue<? extends ProductSummary> observable,
                            ProductSummary oldValue, ProductSummary newValue) {
            productGateway.getById(newValue.getId())
                    .subscribe(new DataQueryObserver<>(this::processProduct,
                            new AlertFactory()::showErrorAlert)); //TODO
        }

        private void processProduct(Product product) {
            currentlySelectedProduct = product;

            Set<Unit> associatedUnits = product.getAllSupportedUnits();
            unitComboBox.getItems().setAll(associatedUnits);
            unitComboBox.getSelectionModel().clearSelection();
            unitComboBox.getSelectionModel().selectFirst();
        }
    }
}
