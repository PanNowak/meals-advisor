package com.github.pannowak.mealsadvisor.gui.views.products.controller;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.observer.DataManipulationObserver;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.ProductGateway;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxmlView;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@FxmlView("editing-product-view.fxml")
public class EditingProductViewController {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Unit> unitComboBox;

    @FXML
    private UnitTableViewController unitTableViewController;

    @FXML
    private Button saveButton;

    private Product editedProduct;

    private final ProductGateway productGateway;
    private final AlertFactory alertFactory;
    private final Router router;

    EditingProductViewController(ProductGateway productGateway, AlertFactory alertFactory,
                                 Router router) {
        this.productGateway = productGateway;
        this.alertFactory = alertFactory;
        this.router = router;
    }

    @FXML
    public void initialize() {
        Pair<Product, List<Unit>> productAndUnits = router.getData();
        ObservableList<Unit> units = FXCollections.observableArrayList(productAndUnits.getValue());
        var remainingPrimaryUnits = new FilteredList<>(units);
        var remainingSecondaryUnits = new FilteredList<>(units);

        setUpControls(remainingPrimaryUnits, remainingSecondaryUnits);
        unitTableViewController.initializeTable(unitComboBox, remainingPrimaryUnits, remainingSecondaryUnits);
        gatherProductData(productAndUnits.getKey());
    }

    @FXML
    public void saveAndSwitch() {
        var productToSave = getProductToSave();

        productGateway.save(productToSave).subscribe(new DataManipulationObserver(
                this::switchToAllProductsViewInternal, alertFactory::showErrorAlert));
    }

    @FXML
    public void switchToAllProductsView() {
        alertFactory.showConfirmationAlert("Czy na pewno chcesz anulować? Stracisz niezapisane zmiany.",
                this::switchToAllProductsViewInternal);
    }

    private void setUpControls(FilteredList<Unit> remainingPrimaryUnits, FilteredList<Unit> remainingSecondaryUnits) {
        setUpUnitComboBox(remainingPrimaryUnits, remainingSecondaryUnits);
        saveButton.disableProperty().bind(incompleteMandatoryInput());
    }

    private void setUpUnitComboBox(FilteredList<Unit> remainingPrimaryUnits,
                                   FilteredList<Unit> remainingSecondaryUnits) {
        unitComboBox.setItems(new SortedList<>(remainingPrimaryUnits, Comparator.comparing(Unit::getName)));
        unitComboBox.setCellFactory(SimpleListCellFactory.getFactory(Unit::getName));
        unitComboBox.setButtonCell(SimpleListCellFactory.createCell(Unit::getName));
        unitComboBox.getSelectionModel().selectedItemProperty()
                .addListener(new UnitComboBoxChangeListener(remainingSecondaryUnits));
    }

    private ObservableValue<Boolean> incompleteMandatoryInput() {
        return EasyBind.combine(
                nameField.textProperty().isEmpty(),
                unitComboBox.valueProperty().isNull(),
                Boolean::logicalOr
        );
    }

    private void gatherProductData(Product editedProduct) {
        this.editedProduct = editedProduct;
        nameField.setText(editedProduct.getName());
        unitComboBox.getSelectionModel().select(editedProduct.getPrimaryUnit());
        unitTableViewController.setSecondaryUnits(editedProduct.getSecondaryUnits());
    }

    private Product getProductToSave() {
        Product productToSave = new Product();
        productToSave.setId(editedProduct.getId());
        productToSave.setName(nameField.getText());
        productToSave.setPrimaryUnit(unitComboBox.getSelectionModel().getSelectedItem());
        unitTableViewController.getSecondaryUnits().forEach(productToSave::addSecondaryUnit);
        return productToSave;
    }

    private void switchToAllProductsViewInternal() {
        router.switchViewClearingly(EditingProductViewController.class,
                AllProductsViewController.class);
    }

    private final class UnitComboBoxChangeListener implements ChangeListener<Unit> {

        private final FilteredList<Unit> remainingSecondaryUnits;

        UnitComboBoxChangeListener(FilteredList<Unit> remainingSecondaryUnits) {
            this.remainingSecondaryUnits = remainingSecondaryUnits;
        }

        @Override
        public void changed(ObservableValue<? extends Unit> observable, Unit oldValue, Unit newValue) {
            var secondaryUnitsPredicate = createSecondaryUnitsPredicate(newValue);
            remainingSecondaryUnits.setPredicate(secondaryUnitsPredicate);
        }

        private Predicate<Unit> createSecondaryUnitsPredicate(Unit selectedPrimaryUnit) {
            Set<Unit> allSelectedUnits = gatherAlreadySelectedUnits(selectedPrimaryUnit);
            return Predicate.not(allSelectedUnits::contains);
        }

        private Set<Unit> gatherAlreadySelectedUnits(Unit selectedPrimaryUnit) {
            Set<Unit> allSelectedUnits = new HashSet<>();
            Optional.ofNullable(selectedPrimaryUnit).ifPresent(allSelectedUnits::add);
            allSelectedUnits.addAll(getChosenSecondaryUnits());
            return allSelectedUnits;
        }

        private Set<Unit> getChosenSecondaryUnits() {
            return unitTableViewController.getSecondaryUnits().stream()
                    .map(SecondaryUnitInfo::getUnit)
                    .collect(Collectors.toSet());
        }
    }
}
