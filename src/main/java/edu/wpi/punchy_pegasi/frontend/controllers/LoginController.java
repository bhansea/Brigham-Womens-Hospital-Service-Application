package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Account;
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
    private final PdbController pdb = App.getSingleton().getPdb();
    private Facade facade = new Facade(pdb);

    public void logIn(ActionEvent event) {
        invalidText.setVisible(false);
        String username = usernameEnter.getText();
        String password = passwordBox.getText();
        Account.Field[] fields = { Account.Field.USERNAME, Account.Field.PASSWORD };
        Object[] values = { username, password };
        if(facade.getAccount(fields, values).size() > 0) {
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
