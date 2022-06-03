package com.github.pannowak.mealsadvisor.gui.views.planning.controller;

import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.api.units.model.Unit;
import com.github.pannowak.mealsadvisor.gui.display.SimpleCellValueFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleTableCellFactory;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Controller
@FxmlView("shopping-list-view.fxml")
public class ShoppingListViewController {

    @FXML
    private TableView<GroceryItem> groceryItemsTable;

    @FXML
    private TableColumn<GroceryItem, ProductSummary> productColumn;

    @FXML
    private TableColumn<GroceryItem, BigDecimal> amountColumn;

    @FXML
    private TableColumn<GroceryItem, Unit> unitColumn;

    private final Router router;

    ShoppingListViewController(Router router) {
        this.router = router;
    }

    @FXML
    public void initialize() {
        setUpColumns();
        gatherGroceryData();
    }

    private void setUpColumns() {
        productColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(GroceryItem::getProductSummary));
        productColumn.setCellFactory(SimpleTableCellFactory.getFactory(ProductSummary::getName));
        productColumn.setComparator(Comparator.comparing(ProductSummary::getName));

        NumberFormat format = NumberFormat.getNumberInstance(Locale.forLanguageTag("pl"));
        amountColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(GroceryItem::getNumberOfUnits));
        amountColumn.setCellFactory(SimpleTableCellFactory.getFactory(format::format));

        unitColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(GroceryItem::getUnit));
        unitColumn.setCellFactory(SimpleTableCellFactory.getFactory(Unit::getName));
        unitColumn.setComparator(Comparator.comparing(Unit::getName));
    }

    private void gatherGroceryData() {
        List<GroceryItem> groceryItems = router.getData();
        var sortedItems = getSortedByName(groceryItems);
        groceryItemsTable.setItems(sortedItems);
    }

    private ObservableList<GroceryItem> getSortedByName(List<GroceryItem> groceryItems) {
        return FXCollections.observableList(groceryItems)
                .sorted(Comparator.comparing(GroceryItem::getProductSummary,
                        Comparator.comparing(ProductSummary::getName)));
    }

    @FXML
    public void switchToPlanningView() {
        router.switchViewClearingly(ShoppingListViewController.class, PlanningViewController.class);
    }
}
