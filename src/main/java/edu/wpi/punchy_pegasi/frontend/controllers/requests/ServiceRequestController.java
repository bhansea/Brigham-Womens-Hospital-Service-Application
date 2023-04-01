package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class ServiceRequestController extends RequestController {

    @FXML
    MFXButton backButton;

    @FXML
    public void init() {
        backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    }
}
