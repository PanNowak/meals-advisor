package com.github.pannowak.mealsadvisor.gui.views.products.controller;

import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.display.SimpleCellValueFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleTableCellFactory;
import com.github.pannowak.mealsadvisor.gui.embedded.EmbeddedViewProvider;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.PopOver;
import org.fxmisc.easybind.EasyBind;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Scope(SCOPE_PROTOTYPE)
@Controller
@FxmlView("unit-table-view.fxml")
public class UnitTableViewController {

    @FXML
    private TableColumn<SecondaryUnitInfo, Unit> unitColumn;

    @FXML
    private TableColumn<SecondaryUnitInfo, BigDecimal> toPrimaryUnitRatioColumn;

    @FXML
    private TableView<SecondaryUnitInfo> unitTable;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    public void initializeTable(ComboBox<Unit> unitComboBox, FilteredList<Unit> remainingPrimaryUnits,
                                FilteredList<Unit> remainingSecondaryUnits) {
        setUpUnitTable(unitComboBox, remainingPrimaryUnits);
        setUpButtons(unitComboBox, remainingSecondaryUnits);
    }

    public List<SecondaryUnitInfo> getSecondaryUnits() {
        return unitTable.getItems();
    }

    public void setSecondaryUnits(Collection<SecondaryUnitInfo> secondaryUnits) {
        unitTable.getItems().addAll(secondaryUnits);
    }

    @FXML
    public void removeSecondaryUnit() {
        unitTable.getItems().remove(unitTable.getSelectionModel().getSelectedItem());
    }

    private void setUpUnitTable(ComboBox<Unit> unitComboBox, FilteredList<Unit> remainingPrimaryUnits) {
        unitColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(SecondaryUnitInfo::getUnit));
        unitColumn.setCellFactory(SimpleTableCellFactory.getFactory(Unit::getName));

        NumberFormat format = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl"));
        toPrimaryUnitRatioColumn.setCellValueFactory(
                SimpleCellValueFactory.getFactory(SecondaryUnitInfo::getToPrimaryUnitRatio));
        toPrimaryUnitRatioColumn.setCellFactory(
                SimpleTableCellFactory.getFactory(format::format));

        unitTable.getItems().addListener(new UnitTableChangeListener(unitComboBox, remainingPrimaryUnits));
    }

    private void setUpButtons(ComboBox<Unit> unitComboBox, ObservableList<Unit> remainingSecondaryUnits) {
        addButton.setOnAction(e -> showPopup(remainingSecondaryUnits));
        addButton.disableProperty().bind(unitCondition(unitComboBox, remainingSecondaryUnits));

        deleteButton.disableProperty().bind(unitTable.getSelectionModel().selectedItemProperty().isNull());
    }

    private void showPopup(ObservableList<Unit> remainingSecondaryUnits) {
        var popupController = new AddUnitPopupController(remainingSecondaryUnits, unitTable.getItems());
        PopOver popOver = EmbeddedViewProvider.getView(popupController);
        popOver.show(addButton);
    }

    private ObservableValue<Boolean> unitCondition(ComboBox<Unit> unitComboBox,
                                                   ObservableList<Unit> remainingSecondaryUnits) {
        var observablePrimaryUnit = unitComboBox.valueProperty();
        var observableSecondaryUnits = new SimpleListProperty<>(remainingSecondaryUnits);
        return EasyBind.combine(observablePrimaryUnit.isNull(),
                observableSecondaryUnits.emptyProperty(),
                Boolean::logicalOr);
    }

    private static final class UnitTableChangeListener implements ListChangeListener<SecondaryUnitInfo> {

        private final ComboBox<Unit> unitComboBox;
        private final FilteredList<Unit> remainingPrimaryUnits;

        UnitTableChangeListener(ComboBox<Unit> unitComboBox, FilteredList<Unit> remainingPrimaryUnits) {
            this.unitComboBox = unitComboBox;
            this.remainingPrimaryUnits = remainingPrimaryUnits;
        }

        @Override
        public void onChanged(Change<? extends SecondaryUnitInfo> change) {
            Unit selectedPrimaryUnit = getSelectedPrimaryUnit();
            setPrimaryUnitPredicate(change.getList());
            ensurePrimaryUnitSelectionStaysTheSame(selectedPrimaryUnit);
        }

        private Unit getSelectedPrimaryUnit() {
            return unitComboBox.getSelectionModel().getSelectedItem();
        }

        private void setPrimaryUnitPredicate(ObservableList<? extends SecondaryUnitInfo> chosenSecondaryUnits) {
//            System.out.println("Wszystkie wybrane 2. jednostki: " + chosenSecondaryUnits);
            var primaryUnitsPredicate = createPrimaryUnitsPredicate(chosenSecondaryUnits);
            remainingPrimaryUnits.setPredicate(primaryUnitsPredicate);
        }

        private Predicate<Unit> createPrimaryUnitsPredicate(ObservableList<? extends SecondaryUnitInfo> chosenSecondaryUnits) {
            return chosenSecondaryUnits.stream()
                    .map(SecondaryUnitInfo::getUnit)
                    .collect(Collectors.collectingAndThen(Collectors.toSet(),
                            this::createPrimaryUnitsPredicate));
        }

        private Predicate<Unit> createPrimaryUnitsPredicate(Set<Unit> chosenSecondaryUnits) {
            return Predicate.not(chosenSecondaryUnits::contains);
        }

        private void ensurePrimaryUnitSelectionStaysTheSame(Unit selectedPrimaryUnit) {
            unitComboBox.getSelectionModel().select(selectedPrimaryUnit);
        }
    }
}
