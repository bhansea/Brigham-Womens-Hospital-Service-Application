package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.MFXTextFieldFocusable;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Account;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class LoginController {

    private final PdbController pdb = App.getSingleton().getPdb();
    private final Facade facade = new Facade(pdb);
    @FXML
    private Label invalidText;
    @FXML
    private VBox quicknav;
    @FXML
    private MFXTextFieldFocusable usernameEnter;
    @FXML
    private Button logInButton;
    @FXML
    private Label usernameBox;
    @FXML
    private MFXPasswordField passwordBox;

    @FXML
    private void initialize() {
        App.getSingleton().getLayout().showLeftLayout(false);
        App.getSingleton().getLayout().showTopLayout(false);
        Platform.runLater(() -> {
            usernameEnter.requestFocus();
        });
        Arrays.stream(Screen.values()).forEach(s -> {
            if(s.getShield().getShieldLevel() > Account.AccountType.NONE.getShieldLevel() || s.equals(Screen.LOGIN)) return;
            var button = new PFXButton(s.getReadable());
            button.setOnAction(e -> App.getSingleton().navigate(s));
            quicknav.getChildren().add(button);
        });
    }
    @FXML
    private void logIn(ActionEvent event) {
        invalidText.setVisible(false);
        String username = usernameEnter.getText();
        String password = passwordBox.getText();
        Account.Field[] fields = {Account.Field.USERNAME, Account.Field.PASSWORD};
        Object[] values = {username, password};
        Map<String, Account> map = facade.getAccount(fields, values);

        if (map.size() > 0) {
            App.getSingleton().setAccount(map.values().stream().findFirst().get());
            App.getSingleton().navigate(Screen.HOME);
        } else {
            invalidText.setVisible(true);
            usernameEnter.setStyle("-fx-border-color: red; -fx-text-fill: #000000;");
            passwordBox.setStyle("-fx-border-color: red; -fx-text-fill: #000000;");
        }
    }
}
