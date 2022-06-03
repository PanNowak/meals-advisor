package com.github.pannowak.mealsadvisor.gui.display;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

import java.util.function.Function;

public class SimpleCellValueFactory<T, R> implements Callback<CellDataFeatures<T, R>, ObservableValue<R>> {

    public static <T, R> SimpleCellValueFactory<T, R> getFactory(Function<T, R> displayValueExtractor) {
        return new SimpleCellValueFactory<>(displayValueExtractor);
    }

    private final Function<T, R> cellValueExtractor;

    private SimpleCellValueFactory(Function<T, R> cellValueExtractor) {
        this.cellValueExtractor = cellValueExtractor;
    }

    @Override
    public ObservableValue<R> call(CellDataFeatures<T, R> param) {
        T rowValue = param.getValue();
        R cellValue = cellValueExtractor.apply(rowValue);
        return new ReadOnlyObjectWrapper<>(cellValue);
    }
}
