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
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class SignageController {

    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();

    private final Facade facade = App.getSingleton().getFacade();


    @FXML
    private VBox signageBodyLeft;
    @FXML
    private HBox signageHeader;


    private final PFXIcon iconUp = new PFXIcon(MaterialSymbols.ARROW_UPWARD);
    private final PFXIcon iconDown = new PFXIcon(MaterialSymbols.ARROW_DOWNWARD);
    private final PFXIcon iconLeft = new PFXIcon(MaterialSymbols.ARROW_BACK);
    private final PFXIcon iconRight = new PFXIcon(MaterialSymbols.ARROW_FORWARD);
    private final PFXIcon iconHere = new PFXIcon(MaterialSymbols.DISTANCE);
    private final PFXIcon iconTime = new PFXIcon(MaterialSymbols.SCHEDULE);
    private final HBox signageHeaderLeft = new HBox();
    private final HBox signageHeaderRight = new HBox();
    private final Label signageDateTime = new Label();


    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        signageDateTime.setText(formatter.format(date));
    }

    private void initHeader() {
        // Set up left header
        signageHeaderLeft.getStyleClass().add("signage-header-left");
        signageHeaderLeft.getChildren().add(iconHere);

        // Set up right header
        signageHeaderRight.getStyleClass().add("signage-header-right");
        HBox.setHgrow(signageHeaderRight, Priority.ALWAYS);
        signageHeaderRight.getChildren().add(iconTime);
        signageDateTime.getStyleClass().add("signage-text-DateTime");
        updateTime();
        signageHeaderRight.getChildren().add(signageDateTime);

        // Combine left and right header in signageHeader HBox
        signageHeader.getChildren().add(signageHeaderLeft);
        signageHeader.getChildren().add(signageHeaderRight);
        signageHeader.getStyleClass().add("signage-header");
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
        iconHere.setOutlined(true);
        iconHere.getStyleClass().add("signage-icon-header");
        iconTime.setOutlined(true);
        iconTime.getStyleClass().add("signage-icon-header");
    }

    private void configTimer(final long interuptPeriodMill) {
        long currTime = System.currentTimeMillis();
        long startDelay = interuptPeriodMill - (currTime % interuptPeriodMill);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    updateTime();
                });
            }
        }, startDelay, interuptPeriodMill);
    }

    @FXML
    private void initialize() {
        configTimer(1000);
        initIcons();
        initHeader();

        Signage newSignageD = new Signage("Location Name Down 0", Signage.DirectionType.DOWN);
        Signage newSignageU = new Signage("Location Name Up 0", Signage.DirectionType.UP);
        Signage newSignageD1 = new Signage("Location Name Down 1", Signage.DirectionType.DOWN);
        Signage newSignageHere = new Signage("Location Name Here", Signage.DirectionType.HERE);
        facade.saveSignage(newSignageD);
        facade.saveSignage(newSignageU);
        facade.saveSignage(newSignageD1);
        facade.saveSignage(newSignageHere);
        buildSignage(loadSignage());

        facade.deleteSignage(newSignageD);
        facade.deleteSignage(newSignageU);
        facade.deleteSignage(newSignageD1);
        facade.deleteSignage(newSignageHere);

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

    private Map<Signage.DirectionType, List<String>> loadSignage() {
        Map<Signage.DirectionType, List<String>> signageMap = new HashMap<>();
        Map<String, Signage> allSignage =  facade.getAllSignage();
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
                    locLabel.getStyleClass().add("signage-label-Here");
                    signageHeaderLeft.getChildren().add(locLabel);
                    continue;
                }
            }
            signageBodyLeft.getChildren().add(signageHB);
            signageBodyLeft.getStyleClass().add("signage-body-left");
            Separator separator = new Separator();
            signageBodyLeft.getChildren().add(separator);
        }
        signageBodyLeft.getChildren().remove(signageBodyLeft.getChildren().size() - 1);  // remove the last separator
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
