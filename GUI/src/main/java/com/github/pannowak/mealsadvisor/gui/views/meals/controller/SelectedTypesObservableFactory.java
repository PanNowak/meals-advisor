package com.github.pannowak.mealsadvisor.gui.views.meals.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import org.controlsfx.control.IndexedCheckModel;
import org.fxmisc.easybind.EasyBind;

import java.util.List;

public final class SelectedTypesObservableFactory {

    public static ObservableValue<String> create(IndexedCheckModel<MealType> checkModel) {
        return new SelectedTypesObservableFactory(checkModel).create();
    }

    private final IndexedCheckModel<MealType> checkModel;

    private SelectedTypesObservableFactory(IndexedCheckModel<MealType> checkModel) {
        this.checkModel = checkModel;
    }

    private ObservableValue<String> create() {
        var selectedMealTypes = selectedMealTypes();
        return EasyBind.map(selectedMealTypes, this::concatSelectedMealTypes);
    }

    private ObservableListValue<String> selectedMealTypes() {
        var selectedMealTypesNames = EasyBind.map(checkModel.getCheckedIndices(),
                index -> checkModel.getItem(index).getName());
        return new SimpleListProperty<>(selectedMealTypesNames);
    }

    private String concatSelectedMealTypes(List<String> mealTypes) {
        if (mealTypes.isEmpty()) {
            return "Wybierz typ posiłku...";
        } else {
            return String.join(", ", mealTypes);
        }
    }
}
