package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LoginController {

    @FXML
    private Button logInButton;
    @FXML
    private Label usernameBox;
    @FXML
    private MFXPasswordField passwordBox;

    public void logIn(ActionEvent event) {
        Navigation.navigate(Screen.HOME);
    }

    public void usernameEnter(ActionEvent event) {
    }

    public void passwordEnter(ActionEvent event) {
    }
}
