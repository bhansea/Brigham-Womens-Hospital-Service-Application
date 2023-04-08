package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.LoginAttempt;
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
        LoginAttempt loginAttempt = new LoginAttempt(usernameBox.getText(), passwordBox.getText());
        App.getSingleton().navigate(Screen.HOME);
    }

    public void usernameEnter(ActionEvent event) {

    }

    public void passwordEnter(ActionEvent event) {
    }
}
