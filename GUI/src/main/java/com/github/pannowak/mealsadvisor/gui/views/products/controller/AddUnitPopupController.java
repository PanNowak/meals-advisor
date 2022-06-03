package com.github.pannowak.mealsadvisor.gui.views.products.controller;

import com.github.pannowak.mealsadvisor.api.products.model.SecondaryUnitInfo;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.controls.TextFormatterFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import net.rgielen.fxweaver.core.FxmlView;
import org.controlsfx.control.PopOver;
import org.fxmisc.easybind.EasyBind;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Locale;

@FxmlView("add-unit-popup.fxml")
public class AddUnitPopupController {

    @FXML
    private PopOver popOver;

    @FXML
    private ComboBox<Unit> comboBox;

    @FXML
    private TextField ratioField;

    @FXML
    private Button addButton;

    private final ObservableList<Unit> remainingSecondaryUnits;
    private final ObservableList<SecondaryUnitInfo> chosenSecondaryUnits;

    AddUnitPopupController(ObservableList<Unit> remainingSecondaryUnits,
                           ObservableList<SecondaryUnitInfo> chosenSecondaryUnits) {
        this.remainingSecondaryUnits = remainingSecondaryUnits;
        this.chosenSecondaryUnits = chosenSecondaryUnits;
    }

    public void initialize() {
        setUpComboBox();
        setUpTextField();
        setUpButton();
    }

    @FXML
    public void addUnit() {
        Unit selected = comboBox.getSelectionModel().getSelectedItem();
        BigDecimal ratio = (BigDecimal) ratioField.getTextFormatter().getValue();
        chosenSecondaryUnits.add(new SecondaryUnitInfo(selected, ratio));
        popOver.hide();
    }

    private void setUpComboBox() {
        comboBox.setItems(new SortedList<>(remainingSecondaryUnits, Comparator.comparing(Unit::getName)));
        comboBox.setCellFactory(SimpleListCellFactory.getFactory(Unit::getName));
        comboBox.setButtonCell(SimpleListCellFactory.createCell(Unit::getName));
    }

    private void setUpTextField() {
        ratioField.setTextFormatter(TextFormatterFactory.create(Locale.forLanguageTag("pl")));
    }

    private void setUpButton() {
        var incompleteInputObservable = prepareIncompleteInputObservable();
        addButton.disableProperty().bind(incompleteInputObservable);
    }

    private ObservableValue<Boolean> prepareIncompleteInputObservable() {
        return EasyBind.combine(comboBox.valueProperty().isNull(), isZeroRatio(), Boolean::logicalOr);
    }

    private ObservableValue<Boolean> isZeroRatio() {
        var valueConverter = ratioField.getTextFormatter().getValueConverter();
        return EasyBind.map(ratioField.textProperty(), valueConverter::fromString)
                .map(BigDecimal.class::cast)
                .map(ratio -> BigDecimal.ZERO.compareTo(ratio) == 0);
    }
}
