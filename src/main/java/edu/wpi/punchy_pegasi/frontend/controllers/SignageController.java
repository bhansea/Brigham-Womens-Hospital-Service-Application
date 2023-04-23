package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Signage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.util.*;

public class SignageController {

    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();

    private final Facade facade = App.getSingleton().getFacade();

//    @FXML
//    private Label labelUP;
//    @FXML
//    private Label labelDOWN;
//    @FXML
//    private Label labelLFT;
//    @FXML
//    private Label labelRGT;
    @FXML
    private Label labelHERE;

    @FXML
    private VBox signageBodyLeft;
//    @FXML
//    private HBox signageUpHB;
//    @FXML
//    private HBox signageDownHB;
//    @FXML
//    private HBox signageLeftHB;
//    @FXML
//    private HBox signageRightHB;
    @FXML
    private HBox signageHereHB;


    private PFXIcon iconUp = new PFXIcon(MaterialSymbols.ARROW_UPWARD);

    private PFXIcon iconDown = new PFXIcon(MaterialSymbols.ARROW_DOWNWARD);

    private PFXIcon iconLeft = new PFXIcon(MaterialSymbols.ARROW_BACK);

    private PFXIcon iconRight = new PFXIcon(MaterialSymbols.ARROW_FORWARD);


    @FXML
    private PFXIcon iconHERE;

    @FXML
    private void initialize() {
        initIcons();
        Signage newSignageD = new Signage("Location Name Down 0", Signage.DirectionType.DOWN);
        Signage newSignageU = new Signage("Location Name Up 0", Signage.DirectionType.UP);
        Signage newSignageD1 = new Signage("Location Name Down 1", Signage.DirectionType.DOWN);
        facade.saveSignage(newSignageD);
        facade.saveSignage(newSignageU);
        facade.saveSignage(newSignageD1);
        buildSignage(loadSignage());

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

    private void initIcons () {
        iconUp.setOutlined(true);
        iconUp.getStyleClass().add("signage-icon");
        iconDown.setOutlined(true);
        iconDown.getStyleClass().add("signage-icon");
        iconLeft.setOutlined(true);
        iconLeft.getStyleClass().add("signage-icon");
        iconRight.setOutlined(true);
        iconRight.getStyleClass().add("signage-icon");
    }

    private Map<Signage.DirectionType, List<String>> loadSignage() {
        Map<Signage.DirectionType, List<String>> signageMap = new HashMap<>();
        Map<String, Signage> allSignage =  facade.getAllSignage();
//        StringBuilder sbUp = new StringBuilder();
//        StringBuilder sbDown = new StringBuilder();
//        StringBuilder sbLeft = new StringBuilder();
//        StringBuilder sbRight = new StringBuilder();
//        StringBuilder sbHere = new StringBuilder();
        for (Map.Entry<String, Signage> entry : allSignage.entrySet()) {
            Signage.DirectionType directionType = entry.getValue().getDirectionType();
            switch (directionType) {
                case UP -> {
                    signageMap.computeIfAbsent(Signage.DirectionType.UP, k -> new ArrayList<>()).add(entry.getKey());
                }
                case DOWN -> {
                    signageMap.computeIfAbsent(Signage.DirectionType.DOWN, k -> new ArrayList<>()).add(entry.getKey());
                }
                case LEFT -> {
                    signageMap.computeIfAbsent(Signage.DirectionType.LEFT, k -> new ArrayList<>()).add(entry.getKey());
                }
                case RIGHT -> {
                    signageMap.computeIfAbsent(Signage.DirectionType.RIGHT, k -> new ArrayList<>()).add(entry.getKey());
                }
                case HERE -> {
                    signageMap.computeIfAbsent(Signage.DirectionType.HERE, k -> new ArrayList<>()).add(entry.getKey());
                }
            }
        }
        return signageMap;
    }

    private void buildSignage(Map<Signage.DirectionType, List<String>> signageMap) {
        for (Map.Entry<Signage.DirectionType, List<String>> entry : signageMap.entrySet()) {
            Label locLabel = new Label(String.join("\n", entry.getValue()));
            locLabel.getStyleClass().add("signage-label");
            HBox signageHB = new HBox();
            signageHB.getStyleClass().add("signage-list");
            switch (entry.getKey()) {
                case UP -> {
                    signageHB.getChildren().add(iconUp);
                    signageHB.getChildren().add(locLabel);
                }
                case DOWN -> {
                    signageHB.getChildren().add(iconDown);
                    signageHB.getChildren().add(locLabel);
                }
                case LEFT -> {
                    signageHB.getChildren().add(iconLeft);
                    signageHB.getChildren().add(locLabel);
                }
                case RIGHT -> {
                    signageHB.getChildren().add(iconRight);
                    signageHB.getChildren().add(locLabel);
                }
                case HERE -> {
                    signageHB.getChildren().add(iconHERE);
                    signageHB.getChildren().add(locLabel);
                }
            }
            signageBodyLeft.getChildren().add(signageHB);
            signageBodyLeft.getStyleClass().add("signage-body-left");
        }
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
