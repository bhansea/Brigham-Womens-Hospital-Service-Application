package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;

public class SignageController {
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            App.getSingleton().getPrimaryStage().setFullScreen(true);
            App.getSingleton().getLayout().showLeftLayout(false);
            App.getSingleton().getLayout().showTopLayout(false);
        });
        App.getSingleton().getScene().setOnKeyTyped(event -> {
            if(event.getCode() == KeyCode.ESCAPE) {
                App.getSingleton().getPrimaryStage().setFullScreen(false);
                App.getSingleton().getLayout().showLeftLayout(true);
                App.getSingleton().getLayout().showTopLayout(true);
            }
        });
    }

}
