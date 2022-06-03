package com.github.pannowak.mealsadvisor.gui.views.products.controller;

import com.github.pannowak.mealsadvisor.api.products.model.Product;
import com.github.pannowak.mealsadvisor.api.products.model.ProductSummary;
import com.github.pannowak.mealsadvisor.gui.dialog.AlertFactory;
import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.observer.DataQueryObserver;
import com.github.pannowak.mealsadvisor.gui.routing.Router;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.ProductGateway;
import com.github.pannowak.mealsadvisor.gui.views.products.gateway.UnitGateway;
import io.reactivex.Single;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Controller;

import java.util.Comparator;
import java.util.function.Predicate;

@Controller
@FxmlView("all-products-view.fxml")
public class AllProductsViewController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<ProductSummary> productsListView;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private final ProductGateway productGateway;
    private final UnitGateway unitGateway;
    private final AlertFactory alertFactory;
    private final Router router;

    AllProductsViewController(ProductGateway productGateway, UnitGateway unitGateway,
                              AlertFactory alertFactory, Router router) {
        this.productGateway = productGateway;
        this.unitGateway = unitGateway;
        this.alertFactory = alertFactory;
        this.router = router;
    }

    @FXML
    public void initialize() {
        setUpControls();
        loadProducts();
    }

    @FXML
    public void switchToAddView() {
        switchToEditingProductView(Single.just(new Product()));
    }

    @FXML
    public void switchToEditView() {
        Long selectedId = getSelectedProduct().getId();
        switchToEditingProductView(productGateway.getById(selectedId));
    }

    @FXML
    public void deleteSelectedProduct() {
        ProductSummary selectedProduct = getSelectedProduct();
        String confirmationMessage =
                String.format("Czy jesteś pewien, że chcesz usunąć produkt \"%s\"?", selectedProduct.getName());

        alertFactory.showConfirmationAlert(confirmationMessage, () -> removeProduct(selectedProduct.getId()));
    }

    private void setUpControls() {
        setListDisplayLogic();
        setUpButtons();
    }

    private void setListDisplayLogic() {
        productsListView.setCellFactory(SimpleListCellFactory.getFactory(ProductSummary::getName));
    }

    private void setUpButtons() {
        ObservableBooleanValue noProductSelected =
                productsListView.getSelectionModel().selectedItemProperty().isNull();
        editButton.disableProperty().bind(noProductSelected);
        deleteButton.disableProperty().bind(noProductSelected);
    }

    private void loadProducts() {
        getAllProducts().subscribe(new DataQueryObserver<>(this::loadProducts, alertFactory::showErrorAlert));
    }

    private Single<FilteredList<ProductSummary>> getAllProducts() {
        return productGateway.getAll()
                .toList()
                .map(FXCollections::observableArrayList)
                .map(FilteredList::new);
    }

    private void loadProducts(FilteredList<ProductSummary> products) {
        insertModelInView(products);
        setListSearchLogic(products);
    }

    private void insertModelInView(ObservableList<ProductSummary> products) {
        var sortedProducts = new SortedList<>(products, Comparator.comparing(ProductSummary::getName));
        productsListView.setItems(sortedProducts);
    }

    private void setListSearchLogic(FilteredList<ProductSummary> products) {
        ObservableValue<Predicate<ProductSummary>> searchPredicate =
                EasyBind.map(searchField.textProperty(), this::createSearchPredicate);
        products.predicateProperty().bind(searchPredicate);
    }

    private Predicate<ProductSummary> createSearchPredicate(String searchText) {
        return product -> StringUtils.containsIgnoreCase(product.getName(), searchText);
    }

    private void switchToEditingProductView(Single<Product> productToEdit) {
        Single.zip(productToEdit, unitGateway.getAll().toList(), Pair::new)
                .subscribe(new DataQueryObserver<>(
                        pairs -> router.switchViewClearingly(AllProductsViewController.class,
                                EditingProductViewController.class, pairs),
                        alertFactory::showErrorAlert
                ));
    }

    private ProductSummary getSelectedProduct() {
        return productsListView.getSelectionModel().getSelectedItem();
    }

    private void removeProduct(Long productId) {
        productGateway.removeById(productId)
                .andThen(getAllProducts())
                .subscribe(new DataQueryObserver<>(this::loadProducts, alertFactory::showErrorAlert));
    }
}
