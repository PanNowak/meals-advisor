package com.github.pannowak.mealsadvisor.gui.views.main.controller;

import com.github.pannowak.mealsadvisor.gui.display.SimpleListCellFactory;
import com.github.pannowak.mealsadvisor.gui.routing.ControllerManager;
import com.github.pannowak.mealsadvisor.gui.routing.ViewProvider;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionModel;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import net.rgielen.fxweaver.core.FxmlView;
import org.fxmisc.easybind.EasyBind;
import org.fxmisc.easybind.monadic.MonadicBinding;
import org.springframework.stereotype.Controller;

import java.util.Objects;

import static com.github.pannowak.mealsadvisor.gui.progress.ProgressIndicatorManager.setCurrentProgressIndicator;

@Controller
@FxmlView("main-view.fxml")
public class MainViewController {

    @FXML
    private ListView<ViewProvider> views;

    @FXML
    private ImageView imageView;

    @FXML
    private BorderPane secondView;

    @FXML
    private ProgressIndicator progressIndicator;

    private final ControllerManager controllerManager;

    MainViewController(ControllerManager controllerManager) {
        this.controllerManager = controllerManager;
    }

    @FXML
    public void initialize() {
        setCurrentProgressIndicator(progressIndicator);
        setUpListView();
        setUpSwitchViewLogic();
    }

    private void setUpListView() {
        views.setItems(controllerManager.getAllViewProviders());
        views.setCellFactory(SimpleListCellFactory.getFactory(ViewProvider::getDescription));
    }

    private void setUpSwitchViewLogic() {
        var selectedProvider = selectedProviderObservable(views.getSelectionModel());
        secondView.centerProperty().bind(selectedProvider.map(ViewProvider::getView));
        secondView.disableProperty().bind(progressIndicator.visibleProperty());
        imageView.imageProperty().bind(
                selectedProvider.map(ViewProvider::getImage).orElse(new Image("/pusheen.png")));
    }

    private MonadicBinding<ViewProvider> selectedProviderObservable(SelectionModel<ViewProvider> selectionModel) {
        return EasyBind.monadic(selectionModel.selectedItemProperty())
                .filter(Objects::nonNull);
    }
}
