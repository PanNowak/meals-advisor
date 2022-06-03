package com.github.pannowak.mealsadvisor.gui.views.planning.controller;

import com.github.pannowak.mealsadvisor.api.meals.model.MealSummary;
import com.github.pannowak.mealsadvisor.api.meals.model.MealType;
import com.github.pannowak.mealsadvisor.api.planning.model.DayPlan;
import com.github.pannowak.mealsadvisor.api.planning.model.GroceryItem;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleCellValueFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleTableCellFactory;
import com.github.pannowak.mealsadvisor.gui.observer.DataQueryObserver;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.meals.gateway.MealGateway;
import com.github.pannowak.mealsadvisor.gui.views.planning.gateway.PlanningGateway;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxmlView;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Controller;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@FxmlView("planning-view.fxml")
public class PlanningViewController {

    @FXML
    private ComboBox<Integer> startWeekDayComboBox;

    @FXML
    private ComboBox<Integer> endWeekDayComboBox;

    @FXML
    private Button drawButton;

    @FXML
    private TableColumn<DayPlan, Integer> weekColumn;

    @FXML
    private TableView<DayPlan> drawnMealsTable;

    @FXML
    private Button shoppingListButton;

    private final MealGateway mealGateway;
    private final PlanningGateway planningGateway;
    private final AlertFactory alertFactory;
    private final Router router;

    PlanningViewController(MealGateway mealGateway, PlanningGateway planningGateway,
                           AlertFactory alertFactory, Router router) {
        this.mealGateway = mealGateway;
        this.planningGateway = planningGateway;
        this.alertFactory = alertFactory;
        this.router = router;
    }

    @FXML
    public void initialize() {
        setUpControls();
        loadMealTypes();
    }

    @FXML
    public void drawMeals() {
        var firstDay = startWeekDayComboBox.getSelectionModel().getSelectedItem();
        var lastDay = endWeekDayComboBox.getSelectionModel().getSelectedItem();
        planningGateway.draw(firstDay, lastDay).subscribe(new DayPlanObserver());
    }

    @FXML
    public void generateShoppingList() {
        List<Long> mealIds = gatherDrawnMealsIds();
        planningGateway.generateShoppingList(mealIds)
                .subscribe(new DataQueryObserver<>(this::switchToShoppingListView, alertFactory::showErrorAlert));
    }

    private void setUpControls() {
        setUpColumns();
        setUpWeekDayComboBoxes();
        setUpButtons();
    }

    private void setUpColumns() {
        weekColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(DayPlan::getDayOfWeek));
        weekColumn.setCellFactory(SimpleTableCellFactory.getFactory(planningGateway::getDayOfWeekDisplayName));
    }

    private void setUpWeekDayComboBoxes() {
        startWeekDayComboBox.setItems(planningGateway.getAllWeekDays());
        startWeekDayComboBox.setCellFactory(SimpleListCellFactory.getFactory(planningGateway::getDayOfWeekDisplayName));
        startWeekDayComboBox.setButtonCell(SimpleListCellFactory.createCell(planningGateway::getDayOfWeekDisplayName));

        endWeekDayComboBox.setItems(planningGateway.getAllWeekDays());
        endWeekDayComboBox.setCellFactory(SimpleListCellFactory.getFactory(planningGateway::getDayOfWeekDisplayName));
        endWeekDayComboBox.setButtonCell(SimpleListCellFactory.createCell(planningGateway::getDayOfWeekDisplayName));
    }

    private void setUpButtons() {
        drawButton.disableProperty().bind(EasyBind.combine(
                startWeekDayComboBox.valueProperty().isNull(),
                endWeekDayComboBox.valueProperty().isNull(),
                Boolean::logicalOr));

        shoppingListButton.disableProperty().bind(
                new SimpleListProperty<>(drawnMealsTable.getItems()).emptyProperty());
    }

    private void loadMealTypes() {
        mealGateway.getAllTypes()
                .toList()
                .subscribe(new DataQueryObserver<>(this::addAllMealTypesColumns, alertFactory::showErrorAlert));
    }

    private void addAllMealTypesColumns(List<MealType> mealTypes) {
        for (MealType type : mealTypes) {
            var mealColumn = createNextMealTypeColumn(type);
            drawnMealsTable.getColumns().add(mealColumn);
        }
    }

    private TableColumn<DayPlan, MealSummary> createNextMealTypeColumn(MealType type) {
        var mealColumn = new TableColumn<DayPlan, MealSummary>(type.getName());
        mealColumn.setCellValueFactory(SimpleCellValueFactory.getFactory(
                dailyMeals -> dailyMeals.getMeal(type).orElse(null)));
        mealColumn.setCellFactory(SimpleTableCellFactory.getFactory(MealSummary::getName));
        mealColumn.setReorderable(false);
        return mealColumn;
    }

    private List<Long> gatherDrawnMealsIds() {
        return drawnMealsTable.getItems().stream()
                .map(DayPlan::getAllMeals)
                .flatMap(Collection::stream)
                .map(MealSummary::getId)
                .collect(Collectors.toList());
    }

    private void switchToShoppingListView(List<GroceryItem> groceryItems) {
        router.switchView(PlanningViewController.class, ShoppingListViewController.class,
                groceryItems);
    }

    private final class DayPlanObserver implements Observer<DayPlan> {

        @Override
        public void onSubscribe(Disposable d) {
            drawnMealsTable.getItems().clear();
        }

        @Override
        public void onNext(DayPlan dayPlan) {
            drawnMealsTable.getItems().add(dayPlan);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            alertFactory.showErrorAlert(e);
        }

        @Override
        public void onComplete() { }
    }
}
