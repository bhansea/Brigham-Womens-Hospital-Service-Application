package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Signage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;

import java.util.*;

public class SignageController {

    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();

    private final Facade facade = App.getSingleton().getFacade();

    @FXML
    private Label labelUP;
    @FXML
    private Label labelDOWN;
    @FXML
    private Label labelLFT;
    @FXML
    private Label labelRGT;
    @FXML
    private Label labelHERE;

    @FXML
    private PFXIcon iconFWD;
    @FXML
    private PFXIcon iconAFT;
    @FXML
    private PFXIcon iconLFT;
    @FXML
    private PFXIcon iconRGT;
    @FXML
    private PFXIcon iconHERE;

    @FXML
    private void initialize() {
        Signage newSignageD = new Signage("Location Name Down 0", Signage.DirectionType.DOWN);
        Signage newSignageU = new Signage("Location Name Up 0", Signage.DirectionType.UP);
        Signage newSignageD1 = new Signage("Location Name Down 1", Signage.DirectionType.DOWN);
        facade.saveSignage(newSignageD);
        facade.saveSignage(newSignageU);
        facade.saveSignage(newSignageD1);
        updateSignage();

        facade.deleteSignage(newSignageD);
        facade.deleteSignage(newSignageU);
        facade.deleteSignage(newSignageD1);
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

    private void updateSignage() {
        Map<String, Signage> allSignage =  facade.getAllSignage();
        StringBuilder sbUp = new StringBuilder();
        StringBuilder sbDown = new StringBuilder();
        StringBuilder sbLeft = new StringBuilder();
        StringBuilder sbRight = new StringBuilder();
        StringBuilder sbHere = new StringBuilder();
        for (Map.Entry<String, Signage> entry : allSignage.entrySet()) {
            switch (entry.getValue().getDirectionType()) {
                case UP -> {
                    sbUp.append(entry.getKey()).append("\n");
                }
                case DOWN -> {
                    sbDown.append(entry.getKey()).append("\n");
                }
                case LEFT -> {
                    sbLeft.append(entry.getKey()).append("\n");
                }
                case RIGHT -> {
                    sbRight.append(entry.getKey()).append("\n");
                }
                case HERE -> {
                    sbHere.append(entry.getKey()).append("\n");
                }
            }
        }
        labelUP.setText(sbUp.toString());
        labelDOWN.setText(sbDown.toString());
        labelLFT.setText(sbLeft.toString());
        labelRGT.setText(sbRight.toString());
        labelHERE.setText(sbHere.toString());
    }

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
