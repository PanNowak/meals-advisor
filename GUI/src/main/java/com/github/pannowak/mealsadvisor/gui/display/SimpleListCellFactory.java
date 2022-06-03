package com.github.pannowak.mealsadvisor.gui.display;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.util.function.Function;

public class SimpleListCellFactory<T> implements Callback<ListView<T>, ListCell<T>> {

    public static <T> SimpleListCellFactory<T> getFactory(Function<T, String> displayValueExtractor) {
        return new SimpleListCellFactory<>(displayValueExtractor);
    }

    public static <T> ListCell<T> createCell(Function<T, String> displayValueExtractor) {
        return new SimpleListCellFactory<>(displayValueExtractor).call(null);
    }

    private final Function<T, String> displayValueExtractor;

    private SimpleListCellFactory(Function<T, String> displayValueExtractor) {
        this.displayValueExtractor = displayValueExtractor;
    }

    @Override
    public ListCell<T> call(ListView<T> param) {
        return new SimpleCell();
    }

    private final class SimpleCell extends ListCell<T> {

        @Override
        protected void updateItem(T item, boolean empty) {
            super.updateItem(item, empty);
            setCellValue(item, empty);
        }

        private void setCellValue(T item, boolean empty) {
            if (isNotPresent(item, empty)) {
                setText(null);
                setGraphic(null);
            } else {
                String displayValue = displayValueExtractor.apply(item);
                setText(displayValue);
            }
        }

        private boolean isNotPresent(T item, boolean empty) {
            return empty || item == null;
        }
    }
}
