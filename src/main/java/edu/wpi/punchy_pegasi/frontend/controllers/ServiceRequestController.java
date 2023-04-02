package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;

public class ServiceRequestController {

    @FXML
    MFXTextField servSearchBar;
    @FXML
    void showReq(){

    }

    @FXML
    public void initialize() {
        servSearchBar.setOnKeyPressed(event -> Navigation.navigate(Screen.HOME));
    }
}
