//package com.github.pannowak.mealsadvisor.gui.controls;
//
//import com.github.pannowak.mealsadvisor.gui.display.SimpleCellValueFactory;
//import com.github.pannowak.mealsadvisor.gui.display.SimpleTableCellFactory;
//import javafx.scene.control.TableColumn;
//
//import java.util.function.Function;
//
//public class TableColumnUtils {
//
//    public static <S, T> void setDisplayLogic(TableColumn<S, T> column,
//                                              Function<S, T> columnItemExtractor,
//                                              Function<T, String> displayValueExtractor) {
//        column.setCellValueFactory(SimpleCellValueFactory.getFactory(columnItemExtractor));
//        column.setCellFactory(SimpleTableCellFactory.getFactory(displayValueExtractor));
//    }
//}
