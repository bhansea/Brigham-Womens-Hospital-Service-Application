package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.generated.Facade;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.util.Objects;

public class SignageController {

    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();

    private final Facade facade = App.getSingleton().getFacade();

    @FXML
    private Label labelFWD;

    @FXML
    private void initialize() {
        String textFWD = "";
        labelFWD.setText(textFWD);
        Platform.runLater(() -> {
            switchTheme(true);
            App.getSingleton().getPrimaryStage().setFullScreen(true);
            App.getSingleton().getLayout().showLeftLayout(false);
            App.getSingleton().getLayout().showTopLayout(false);
        });
        myScene.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ESCAPE)) {
                switchTheme(false);
                App.getSingleton().getPrimaryStage().setFullScreen(false);
                App.getSingleton().getLayout().showLeftLayout(true);
                App.getSingleton().getLayout().showTopLayout(true);
            }
        });
    }

    @FXML


    private void switchTheme(boolean setDark) {
        if (setDark) {
            myScene.getStylesheets().remove(lightTheme);
            myScene.getStylesheets().add(darkTheme);
        } else {
            myScene.getStylesheets().remove(darkTheme);
            myScene.getStylesheets().add(lightTheme);
        }
    }

}
