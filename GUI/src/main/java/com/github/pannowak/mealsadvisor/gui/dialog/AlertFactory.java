package com.github.pannowak.mealsadvisor.gui.dialog;

import com.github.pannowak.mealsadvisor.gui.embedded.EmbeddedViewProvider;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import org.fxmisc.easybind.EasyBind;
import org.springframework.stereotype.Component;

import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE;
import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

@Component
public class AlertFactory {

    public void showErrorAlert(Throwable exception) {
        Alert alert = new Alert(ERROR);

        alert.setTitle("");
        alert.setHeaderText("Wystąpił błąd!");
        alert.setContentText(exception.getLocalizedMessage());

        var dialogController = new ExceptionDialogController(exception);
        GridPane expContent = EmbeddedViewProvider.getView(dialogController);
        alert.getDialogPane().setExpandableContent(expContent);

        alert.show();
    }

    public void showConfirmationAlert(String contentText, Runnable onConfirmAction) {
        ButtonType yesButton = new ButtonType("Tak", OK_DONE);
        ButtonType noButton = new ButtonType("Nie", CANCEL_CLOSE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText, yesButton, noButton);

        alert.setTitle("");
        alert.setHeaderText("Wymagane potwierdzenie");
        EasyBind.monadic(alert.resultProperty())
                .map(ButtonType::getButtonData)
                .filter(OK_DONE::equals)
                .addListener((observable, oldValue, newValue) -> onConfirmAction.run());

        alert.show();
    }

}
