package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
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
    private HBox viewEdit;
    @FXML
    private HBox viewNormal;
    @FXML
    private VBox signageBodyLeft;
    @FXML
    private HBox signageHeader;

    public static void tableHeightHelper(TableView<?> table, int rowHeight, int headerHeight, int margin) {
        table.prefHeightProperty().bind(Bindings.max(2, Bindings.size(table.getItems()))
                .multiply(rowHeight)
                .add(headerHeight)
                .add(margin));
        table.minHeightProperty().bind(table.prefHeightProperty());
        table.maxHeightProperty().bind(table.prefHeightProperty());
    }

    @NotNull
    private static VBox getSignageTableView(ObservableList<Signage> rightList) {
        var vBox = new VBox();
        rightList.addListener((ListChangeListener<? super Signage>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Signage signage : c.getAddedSubList()) {
                        var hbox = new HBox(new Label(signage.getLongName()));
                        hbox.setId(signage.getLongName());
                        Platform.runLater(() ->
                                vBox.getChildren().add(hbox)
                        );
                    }
                }
                if (c.wasRemoved()) {
                    for (Signage signage : c.getRemoved()) {
                        Platform.runLater(() ->
                                vBox.getChildren().removeIf(node -> node.getId().equals(signage.getLongName()))
                        );
                    }
                }
            }
        });
        //init the vBox
        for (Signage signage : rightList) {
            var hbox = new HBox(new Label(signage.getLongName()));
            hbox.setId(signage.getLongName());
            vBox.getChildren().add(hbox);
        }
//        var tableView = new ListView<Signage>();
//        tableView.getStyleClass().add("noheader");
//        tableView.setItems(rightList);
//        var col = new TableColumn<Signage, String>("");
//        col.setCellValueFactory(s -> new SimpleObjectProperty<>(s.getValue().getLongName()));
//        tableView.setCellFactory(c -> new ListCell<>() {
//            @Override
//            protected void updateItem(Signage item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    final var graphic = new Label(item.getLongName());
//                    graphic.setPadding(new Insets(0, 0, 0, 0));
//                    graphic.getStyleClass().add("signage-label");
//                    setGraphic(graphic);
//                }
//            }
//        });
//        tableHeightHelper(tableView, 50, 0, 0);
//        tableView.getColumns().clear();
//        tableView.getColumns().add(col);
        return vBox;
    }

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
        configTimer(1000);
        initIcons();
        initHeader();
        buildSignage();

//        Signage newSignageD = new Signage("Location Name Right 1", Signage.DirectionType.RIGHT);
//        Signage newSignageU = new Signage("Location Name Right 2", Signage.DirectionType.RIGHT);
//        Signage newSignageD1 = new Signage("Location Name Down 1", Signage.DirectionType.DOWN);
//        Signage newSignageHere = new Signage("Location Name Here", Signage.DirectionType.HERE);
//        facade.saveSignage(newSignageD);
//        facade.saveSignage(newSignageU);
//        facade.saveSignage(newSignageD1);
//        facade.saveSignage(newSignageHere);
//        buildSignage(loadSignage());
//        loadSignage();

        var admin = App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel();
        viewEdit.setVisible(admin);
        viewEdit.setManaged(admin);
        viewNormal.setVisible(!admin);
        viewNormal.setManaged(!admin);
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

    private Map<Signage.DirectionType, List<String>> loadSignage() {
//        var rightList = signageList.filtered(signage -> signage.getDirectionType() == Signage.DirectionType.RIGHT);
//        var leftList = signageList.filtered(signage -> signage.getDirectionType() == Signage.DirectionType.LEFT);
//        var upList = signageList.filtered(signage -> signage.getDirectionType() == Signage.DirectionType.UP);
//        var downList = signageList.filtered(signage -> signage.getDirectionType() == Signage.DirectionType.DOWN);
//
//        TableView<Signage> tableView = getSignageTableView(rightList);
//
//        signageBodyLeft.getChildren().add(tableView);
//        var rightItems = new VBox();
//        ObservableList<Signage> rightItemsNodes = FXCollections.observableArrayList();
//
//        rightList.addListener((ListChangeListener.Change<? extends Signage> c) -> {
//            while (c.next()) {
//                if (c.wasAdded()) {
//                    for (Signage signage : c.getAddedSubList()) {
//                        rightItems.getChildren().add(new Label(signage.getLongName()));
//                    }
//                } else if (c.wasRemoved()) {
//                    for (Signage signage : c.getRemoved()) {
//                        rightItems.getChildren().remove(new Label(signage.getLongName()));
//                    }
//                }
//            }
//        });
//        Bindings.bindContentBidirectional(rightItemsNodes, rightList);

//        for (Map.Entry<String, Signage> entry : allSignage.entrySet()) {
//            Signage.DirectionType directionType = entry.getValue().getDirectionType();
//            String locationName = entry.getValue().getLongName();
//            switch (directionType) {
//                case UP -> {
//                    signageMap.computeIfAbsent(Signage.DirectionType.UP, k -> new ArrayList<>()).add(locationName);
//                }
//                case DOWN -> {
//                    signageMap.computeIfAbsent(Signage.DirectionType.DOWN, k -> new ArrayList<>()).add(locationName);
//                }
//                case LEFT -> {
//                    signageMap.computeIfAbsent(Signage.DirectionType.LEFT, k -> new ArrayList<>()).add(locationName);
//                }
//                case RIGHT -> {
//                    signageMap.computeIfAbsent(Signage.DirectionType.RIGHT, k -> new ArrayList<>()).add(locationName);
//                }
//                case HERE -> {
//                    signageMap.computeIfAbsent(Signage.DirectionType.HERE, k -> new ArrayList<>()).add(locationName);
//                }
//            }
//        }
        return null;
    }

    private void buildSignage() {
        for (var direction : Signage.DirectionType.values()) {
            var signageList = facade.getAllAsListSignage().filtered(signage -> signage.getDirectionType() == direction);
            Label locLabel = new Label(String.join("\n", signageList.stream().map(Signage::getLongName).toList()));
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
