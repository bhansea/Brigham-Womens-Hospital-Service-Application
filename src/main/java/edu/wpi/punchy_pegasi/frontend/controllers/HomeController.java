package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    MFXButton navigateButton;

    @FXML
    public void initialize() {
        navigateButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUEST));
    }
}
