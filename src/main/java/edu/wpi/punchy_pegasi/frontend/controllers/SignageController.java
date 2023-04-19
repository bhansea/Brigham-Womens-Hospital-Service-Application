package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import javafx.application.Platform;
import javafx.fxml.FXML;

public class SignageController {
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            App.getSingleton().getPrimaryStage().setFullScreen(true);
        });
    }

}
