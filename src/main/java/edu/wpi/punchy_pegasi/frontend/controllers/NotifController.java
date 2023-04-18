package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class NotifController {
    @FXML
    PFXButton closeWindow;
    public void closeNotifWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) closeWindow.getScene().getWindow();

        // Close the stage
        stage.close();
    }
}

