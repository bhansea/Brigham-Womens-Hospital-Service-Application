package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXDropdown;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Account;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.Signage;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.*;

public class SignageController {

    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();

    private final Facade facade = App.getSingleton().getFacade();
    private final PFXIcon iconUp = new PFXIcon(MaterialSymbols.ARROW_UPWARD);
    private final PFXIcon iconDown = new PFXIcon(MaterialSymbols.ARROW_DOWNWARD);
    private final PFXIcon iconLeft = new PFXIcon(MaterialSymbols.ARROW_BACK);
    private final PFXIcon iconRight = new PFXIcon(MaterialSymbols.ARROW_FORWARD);
    private final PFXIcon iconHere = new PFXIcon(MaterialSymbols.DISTANCE);
    private final PFXIcon iconTime = new PFXIcon(MaterialSymbols.SCHEDULE);
    private final HBox signageHeaderLeft = new HBox();
    private final HBox signageHeaderRight = new HBox();
    private final Label signageDateTime = new Label();


    @FXML
    private VBox headerEdit;
    @FXML
    private VBox headerNormal;

    @FXML
    private HBox viewEdit;
    @FXML
    private HBox viewNormal;
    @FXML
    private VBox signageBodyLeft;
    @FXML
    private HBox signageHeader;
    @FXML
    private HBox signageHeaderEdit;

    private static String prefSignageName = "";

    public static void tableHeightHelper(TableView<?> table, int rowHeight, int headerHeight, int margin) {
        table.prefHeightProperty().bind(Bindings.max(2, Bindings.size(table.getItems()))
                .multiply(rowHeight)
                .add(headerHeight)
                .add(margin));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        signageDateTime.setText(formatter.format(date));
    }

    private void initIcons() {
        iconUp.getStyleClass().add("signage-icon");
        iconDown.getStyleClass().add("signage-icon");
        iconLeft.getStyleClass().add("signage-icon");
        iconRight.getStyleClass().add("signage-icon");
        iconHere.getStyleClass().add("signage-icon-header");
        iconTime.getStyleClass().add("signage-icon-header");
        iconTime.setOutlined(true);
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
        prefSignageName = "a";
        var admin = App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel();
        configTimer(1000);
        initIcons();
        if (!admin) {
            initHeader();
        } else {
            initHeaderEdit();
        }
        buildSignage();

        viewEdit.setVisible(admin);
        viewEdit.setManaged(admin);
        viewNormal.setVisible(!admin);
        viewNormal.setManaged(!admin);
        headerEdit.setVisible(admin);
        headerEdit.setManaged(admin);
        headerNormal.setVisible(!admin);
        headerNormal.setManaged(!admin);

        Platform.runLater(() -> {
            switchTheme(true);
            App.getSingleton().getPrimaryStage().setFullScreen(true);
            App.getSingleton().getLayout().showLeftLayout(false);
            App.getSingleton().getLayout().showTopLayout(false);
        });
        myScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                switchTheme(false);
                App.getSingleton().getPrimaryStage().setFullScreen(false);
                App.getSingleton().getLayout().showLeftLayout(true);
                App.getSingleton().getLayout().showTopLayout(true);
            }
        });
    }

    private void initHeader() {
        // Set up left header
        signageHeaderLeft.getStyleClass().add("signage-header-left");
        signageHeaderLeft.getChildren().add(iconHere);
        var headerLeft = facade.getAllAsListSignage()
                .filtered(signage -> signage.getDirectionType().equals(Signage.DirectionType.HERE));
        var headerVbox = getSignageTableView(headerLeft);
        headerVbox.getStyleClass().add("signage-label-Here");
        signageHeaderLeft.getChildren().add(headerVbox);

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

    private void initHeaderEdit() {
        // setting up left side of header
        HBox signageHeaderLeftEdit = new HBox();
        PFXIcon iconSignageLocation = new PFXIcon(MaterialSymbols.WHERE_TO_VOTE);
        iconSignageLocation.getStyleClass().add("signage-icon-header");
        var signagePrefList = facade.getAllAsListSignage()
                .filtered(signage -> signage.getDirectionType().equals(Signage.DirectionType.HERE));
        var signageHere = getSignageTableView(signagePrefList);
        signageHere.getStyleClass().add("signage-label-Here");
        signageHeaderLeftEdit.getChildren().add(iconSignageLocation);
        signageHeaderLeftEdit.getChildren().add(signageHere);

        HBox signageHeaderRightEdit = new HBox();



        signageHeaderEdit.getChildren().add(signageHeaderLeftEdit);
        signageHeaderEdit.getChildren().add(signageHeaderRightEdit);
    }

    @NotNull
    private static VBox getSignageTableView(ObservableList<Signage> rightList) {
        var vBox = new VBox();
        rightList.addListener((ListChangeListener<? super Signage>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Signage signage : c.getAddedSubList()) {
                        if (!signage.getSignName().equals(prefSignageName)) {
                            continue;
                        }
                        var hbox = new HBox(new Label(signage.getLongName()));
                        hbox.setId(signage.getUuid().toString());
                        Platform.runLater(() ->
                                vBox.getChildren().add(hbox)
                        );
                    }
                }
                if (c.wasRemoved()) {
                    for (Signage signage : c.getRemoved()) {
                        if (!signage.getSignName().equals(prefSignageName)) {
                            continue;
                        }
                        Platform.runLater(() ->
                                vBox.getChildren().removeIf(node -> node.getId().equals(signage.getUuid().toString()))
                        );
                    }
                }
            }
        });
        //init the vBox
        for (Signage signage : rightList) {
            if (!signage.getSignName().equals(prefSignageName)) {
                continue;
            }
            var hbox = new HBox(new Label(signage.getLongName()));
            hbox.setId(signage.getUuid().toString());
            vBox.getChildren().add(hbox);
        }
        return vBox;
    }

    private void buildSignage() {
        for (var direction : Signage.DirectionType.values()) {
            var signageList = facade.getAllAsListSignage()
                    .filtered(signage -> signage.getDirectionType() == direction && signage.getSignName().equals(prefSignageName));
//            var prefSignList = signageList.filtered(signage -> signage.getSignName().equals(prefSignageName));
//            Label locLabel = new Label(String.join("\n", signageList.stream().map(Signage::getLongName).toList()));
            var table = getSignageTableView(signageList);
            table.getStyleClass().add("signage-label");
            HBox signageHB = new HBox();
            signageHB.visibleProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
            signageHB.managedProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
            signageHB.getStyleClass().add("signage-list");
            switch (direction) {
                case UP -> {
                    signageHB.getChildren().add(iconUp);
                    signageHB.getChildren().add(table);
                }
                case DOWN -> {
                    signageHB.getChildren().add(iconDown);
                    signageHB.getChildren().add(table);
                }
                case LEFT -> {
                    signageHB.getChildren().add(iconLeft);
                    signageHB.getChildren().add(table);
                }
                case RIGHT -> {
                    signageHB.getChildren().add(iconRight);
                    signageHB.getChildren().add(table);
                }
                case HERE -> {
//                    locLabel.getStyleClass().add("signage-label-Here");
//                    signageHeaderLeft.getChildren().add(locLabel);
                    continue;
                }
            }
            signageBodyLeft.getChildren().add(signageHB);
            signageBodyLeft.getStyleClass().add("signage-body-left");
            Separator separator = new Separator();
            separator.visibleProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
            separator.managedProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
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
