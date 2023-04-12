package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.LoginAttempt;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LoginController {

    @FXML
    public Label invalidText;
    @FXML
    public MFXTextField usernameEnter;
    @FXML
    private Button logInButton;
    @FXML
    private Label usernameBox;
    @FXML
    private MFXPasswordField passwordBox;

    public void logIn(ActionEvent event) {
        invalidText.setVisible(false);
        String username = usernameEnter.getText();
        String password = passwordBox.getText();
        LoginAttempt loginAttempt = new LoginAttempt(username, password);
        if(loginAttempt.isLoginSuccess()) {;
            App.getSingleton().navigate(Screen.HOME);
        } else {
            invalidText.setVisible(true);
            usernameEnter.setStyle("-fx-border-color: red; -fx-text-fill: #000000;");
            passwordBox.setStyle("-fx-border-color: red; -fx-text-fill: #000000;");
        }
    }

    public void usernameEnter(ActionEvent event) {

    }

    public void passwordEnter(ActionEvent event) {
    }
}
