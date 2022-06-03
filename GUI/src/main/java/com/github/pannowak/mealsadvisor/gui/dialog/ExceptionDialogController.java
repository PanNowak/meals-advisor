package com.github.pannowak.mealsadvisor.gui.dialog;

import com.github.pannowak.mealsadvisor.gui.embedded.EmbeddedController;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.exception.ExceptionUtils;

@EmbeddedController
@FxmlView("exception-dialog.fxml")
public class ExceptionDialogController {

    @FXML
    private TextArea textArea;

    private final Throwable exception;

    ExceptionDialogController(Throwable exception) {
        this.exception = exception;
    }

    public void initialize() {
        String stackTrace = ExceptionUtils.getStackTrace(exception);
        textArea.setText(stackTrace);
    }
}
