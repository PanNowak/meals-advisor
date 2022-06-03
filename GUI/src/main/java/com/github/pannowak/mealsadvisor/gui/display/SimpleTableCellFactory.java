package com.github.pannowak.mealsadvisor.gui.display;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.function.Function;

public class SimpleTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

    public static <S, T> SimpleTableCellFactory<S, T> getFactory(Function<T, String> displayValueExtractor) {
        return new SimpleTableCellFactory<>(displayValueExtractor);
    }

    private final Function<T, String> displayValueExtractor;

    private SimpleTableCellFactory(Function<T, String> displayValueExtractor) {
        this.displayValueExtractor = displayValueExtractor;
    }

    @Override
    public TableCell<S, T> call(TableColumn<S, T> param) {
        return new SimpleCell();
    }

    private final class SimpleCell extends TableCell<S, T> {

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
