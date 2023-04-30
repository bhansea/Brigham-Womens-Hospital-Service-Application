package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.components.PFXListView;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.frontend.map.HospitalFloor;
import edu.wpi.punchy_pegasi.frontend.map.HospitalMap;
import edu.wpi.punchy_pegasi.frontend.map.IMap;
import edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.enums.FloatMode;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;

public class SignageController {
    private static final Facade facade = App.getSingleton().getFacade();
    private static final ObservableList<String> signageNames = FXCollections.observableArrayList();
    private static boolean editing = false;
    private final String lightTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageLight.css")).toExternalForm();
    private final String darkTheme = Objects.requireNonNull(getClass().getResource("/edu/wpi/punchy_pegasi/frontend/css/SignageDark.css")).toExternalForm();
    private final Scene myScene = App.getSingleton().getScene();
    private final PFXIcon iconUp = new PFXIcon(MaterialSymbols.ARROW_UPWARD);
    private final PFXIcon iconDown = new PFXIcon(MaterialSymbols.ARROW_DOWNWARD);
    private final PFXIcon iconLeft = new PFXIcon(MaterialSymbols.ARROW_BACK);
    private final PFXIcon iconRight = new PFXIcon(MaterialSymbols.ARROW_FORWARD);
    private final PFXIcon iconHere = new PFXIcon(MaterialSymbols.DISTANCE);
    private final PFXIcon iconTime = new PFXIcon(MaterialSymbols.SCHEDULE);
    private final HBox signageHeaderLeft = new HBox();
    private final HBox signageHeaderMid = new HBox();
    private final HBox signageHeaderRight = new HBox();
    private final Label signageDateTime = new Label();
    private final VBox editingVbox = new VBox();
    private final List<Consumer<String>> filterUpdaters = new ArrayList<>();
    private final MFXComboBox<String> signageNameCB = new MFXComboBox<>();
    private final MFXFilterComboBox<LocationName> locNameCB = new MFXFilterComboBox<>();
    private final MFXComboBox<Signage.DirectionType> directionCB = new MFXComboBox<>();
    private final PFXButton submitButton = new PFXButton("Submit");
    private static final IMap<HospitalFloor.Floors> hospitalMap = new HospitalMap();
    private String prefSignageName;
    @FXML
    private VBox headerEdit;
    @FXML
    private VBox headerNormal;
    @FXML
    private HBox viewEdit;
    @FXML
    private HBox viewNormal;
    @FXML
    private HBox signageBody;
    @FXML
    private StackPane signageBodyStackPane;
    @FXML
    private VBox signageBodyLeft;
    @FXML
    private HBox signageHeader;
    private Rectangle maxRectangle = new Rectangle(0, 0, 0,0);

    private static ObservableMap<Node, ObservableList<LocationName>> nodeToLocation;
    private ObservableMap<LocationName, Node> locationToNode;

    private void load() {
        App.getSingleton().getExecutorService().execute(() -> {
            var nodes = App.getSingleton().getFacade().getAllNode();
            var edges = App.getSingleton().getFacade().getAllEdge();
            var moves = App.getSingleton().getFacade().getAllMove();
            var locations = App.getSingleton().getFacade().getAllLocationName();
            var nodesList = App.getSingleton().getFacade().getAllAsListNode();
            var edgesList = App.getSingleton().getFacade().getAllAsListEdge();
            var movesList = App.getSingleton().getFacade().getAllAsListMove();
            var locationsList = App.getSingleton().getFacade().getAllAsListLocationName();
            MFXDatePicker dateee = new MFXDatePicker();
            dateee.setValue(LocalDate.now());
            nodeToLocation = FacadeUtils.getNodeLocations(nodes, locations, moves, dateee.valueProperty());
            locationToNode = FacadeUtils.getLocationNode(nodes, locations, moves, dateee.valueProperty());
        });
    }

    private static void initSignSelector() {
        ObservableList<Signage> signageList = facade.getAllAsListSignage();
        signageNames.clear();
        signageNames.addAll(signageList.stream().map(Signage::getSignName).distinct().sorted().toList());
        signageList.addListener((ListChangeListener<Signage>) c -> Platform.runLater(() -> {
            while (c.next()) {
                if (c.wasAdded())
                    for (Signage signage : c.getAddedSubList())
                        if (!signageNames.contains(signage.getSignName()))
                            signageNames.add(signage.getSignName());
                if (c.wasRemoved())
                    for (Signage signage : c.getRemoved())
                        if (c.getList().stream().map(Signage::getSignName).filter(signage.getSignName()::equals).count() == 0)
                            signageNames.remove(signage.getSignName());
            }
        }));
    }

    private static void addDelButton(HBox hbox, Signage signage) {
        if (editing) {
            var deleteBtn = new PFXButton("", new PFXIcon(MaterialSymbols.DELETE_FOREVER));
            deleteBtn.getStyleClass().add("signage-delete-btn");

            deleteBtn.setOnAction(event -> {
                facade.deleteSignage(signage);
            });
            hbox.getChildren().add(deleteBtn);
            deleteBtn.visibleProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
            deleteBtn.managedProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
        }
    }

    @FXML
    private void initialize() {
        load();
        editing = App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel();
        configTimer(1000);
        initIcons();
        initHeader();
        buildSignage();
        signageBody.getStyleClass().add("signage-body");
        initSignSelector();
        if (editing) {
            buildEditSignage();
        } else {
            buildSignageMap();
        }

        Platform.runLater(() -> {
            setFullScreen(false);
        });
        myScene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.F11))
                setFullScreen(true);
            else if (event.getCode().equals(KeyCode.ESCAPE))
                setFullScreen(false);
        });
    }

    private void buildSignageMap() {
        hospitalMap.setDefaultOverlaysVisible(false);
        hospitalMap.enableMove(false);
        hospitalMap.setAnimate(false);
        signageBodyStackPane.getChildren().add(hospitalMap.get());
        signageBodyStackPane.setMinWidth(600);
        signageBodyStackPane.setMaxWidth(700);
    }

    private static void updateView(ObservableList<Signage> signageList) {
//        var nodesList = getNode(signageList);
//        signageList.addListener((ListChangeListener<Signage>) c -> {
//            if (editing) return;
//            var nodes = getNode(signageList);
//            var minX = nodes.stream().mapToDouble(Node::getXcoord).min().orElse(0);
//            var maxX = nodes.stream().mapToDouble(Node::getXcoord).max().orElse(0);
//            var minY = nodes.stream().mapToDouble(Node::getYcoord).min().orElse(0);
//            var maxY = nodes.stream().mapToDouble(Node::getYcoord).max().orElse(0);
//            hospitalMap.showRectangle(new Rectangle(minX - 100, minY - 100, maxX - minX + 200, maxY - minY + 200));
////            hospitalMap.addNode()
//        });


        var nodes = signageToNodes(signageList);
        var minX = nodes.stream().mapToDouble(Node::getXcoord).min().orElse(0);
        var maxX = nodes.stream().mapToDouble(Node::getXcoord).max().orElse(0);
        var minY = nodes.stream().mapToDouble(Node::getYcoord).min().orElse(0);
        var maxY = nodes.stream().mapToDouble(Node::getYcoord).max().orElse(0);
        Platform.runLater(() -> {
            hospitalMap.clearMap();
            if (nodes.size() == 0) return;
            hospitalMap.showLayer(HospitalFloor.floorMap.get(nodes.get(0).getFloor()));
            nodes.forEach(n -> hospitalMap.addNode(n, "#fffb00", Bindings.createStringBinding(() -> nodeToLocation(n)), Bindings.createStringBinding(() -> "")));
            hospitalMap.showRectangle(new Rectangle(minX - 100, minY - 100, maxX - minX + 200, maxY - minY + 200)); });
    }

    private static String nodeToLocation(Node node) {
        var locID = facade.getMove(Move.Field.NODE_ID, node.getNodeID()).values().iterator().next().getLocationID();
        return facade.getLocationName(LocationName.Field.UUID, locID).values().iterator().next().getLongName();
    }

    private static ObservableList<Node> signageToNodes(ObservableList<Signage> signageList) {
        var locationNames = facade.getAllAsListLocationName().filtered(
                locationName -> signageList.stream().map(Signage::getLongName).distinct().toList().contains(locationName.getLongName()));
        var moves = facade.getAllAsListMove().filtered(
                move -> locationNames.stream().map(LocationName::getUuid).toList().contains(move.getLocationID()));
        return facade.getAllAsListNode().filtered(
                node -> moves.stream().map(Move::getNodeID).toList().contains(node.getNodeID()));
    }

//    @NotNull
//    private static VBox getSignageTableView(ObservableList<Signage> rightList) {
//        var vBox = new VBox();
////        Bindings.bindContent(vBox.getChildren(), new MappedList<>(rightList, signage ->{
////            var hbox = new HBox(new Label(signage.getLongName()));
////            hbox.setId(signage.getUuid().toString());
////            addDelButton(hbox, signage);
////            return hbox;
////        }));
//        rightList.addListener((ListChangeListener<? super Signage>) c -> {
//            while (c.next()) {
//                if (c.wasAdded())
//                    for (Signage signage : c.getAddedSubList()) {
//                        var hbox = new HBox(new Label(signage.getLongName()));
//                        hbox.setId(signage.getUuid().toString());
//                        addDelButton(hbox, signage);
//                        Platform.runLater(() ->
//                                vBox.getChildren().add(hbox)
//                        );
//                    }
//                if (c.wasRemoved())
//                    for (Signage signage : c.getRemoved())
//                        Platform.runLater(() ->
//                                vBox.getChildren().removeIf(node -> node.getId().equals(signage.getUuid().toString()))
//                        );
//            }
//        });
////        init the vBox
//        for (Signage signage : rightList) {
//            var hbox = new HBox(new Label(signage.getLongName()));
//            addDelButton(hbox, signage);
//            hbox.setId(signage.getUuid().toString());
//            vBox.getChildren().add(hbox);
//        }
//        return vBox;
//    }

    private void setFullScreen(boolean setFullScreen) {
        if (setFullScreen) {
            switchTheme(true);
            App.getSingleton().getPrimaryStage().setFullScreen(true);
            App.getSingleton().getLayout().showLeftLayout(false);
            App.getSingleton().getLayout().showTopLayout(false);
            signageBodyStackPane.setMaxWidth(1300);
        } else {
            switchTheme(false);
            App.getSingleton().getPrimaryStage().setFullScreen(false);
            App.getSingleton().getLayout().showLeftLayout(true);
            App.getSingleton().getLayout().showTopLayout(true);
            signageBodyStackPane.setMaxWidth(700);
        }
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

    private void initHeader() {
        var fullscreenButton = new PFXButton("", new PFXIcon(MaterialSymbols.OPEN_IN_FULL));
        fullscreenButton.getStyleClass().add("signage-fullscreen-button");
        fullscreenButton.setOnAction(e -> {
            setFullScreen(true);
        });
        fullscreenButton.visibleProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
        fullscreenButton.managedProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());

        // Set up left header
        signageHeader.getChildren().add(fullscreenButton);
        signageHeaderLeft.getStyleClass().add("signage-header-left");

        // Set up right header
        signageHeaderRight.getStyleClass().add("signage-header-right");
        HBox.setHgrow(signageHeaderRight, Priority.ALWAYS);
        signageHeaderRight.getChildren().add(iconTime);
        signageDateTime.getStyleClass().add("signage-text-DateTime");
        updateTime();
        signageHeaderRight.getChildren().add(signageDateTime);
        var signageNameSelector = new MFXComboBox<>(signageNames);
        signageNameSelector.setPromptText("Signage Name");
        signageNameSelector.getStyleClass().add("signage-combo-box");
        signageNameSelector.setFloatMode(FloatMode.DISABLED);
        signageNameSelector.setOnAction(e -> {
            setSignageName(signageNameSelector.getValue());
        });
//        var button = new PFXButton("Zoom to rect");
//        button.setOnMouseClicked(e->{
//            hospitalMap.showRectangle(new Rectangle(1401 - 100, 2566 - 100, 1681 - 1401 + 200, 2747 - 2556 + 200));
//        });
        signageHeaderMid.getChildren().addAll(signageNameSelector);
        signageHeaderMid.getStyleClass().add("signage-header-mid");
        signageHeaderMid.visibleProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
        signageHeaderMid.managedProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());

        // Combine left and right header in signageHeader HBox
        signageHeader.getChildren().add(signageHeaderLeft);
        signageHeader.getChildren().add(signageHeaderMid);
        signageHeader.getChildren().add(signageHeaderRight);
        signageHeader.getStyleClass().add("signage-header");
    }

    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        signageDateTime.setText(formatter.format(date));
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

    private void buildEditSignage() {
        signageNameCB.setFloatingText("Signage Name");
        signageNameCB.setFloatMode(FloatMode.INLINE);
        signageNameCB.textProperty().addListener((observable, oldValue, newValue) -> {
            validateSubmit();
        });
        locNameCB.setFloatingText("Location Name");
        locNameCB.setFloatMode(FloatMode.INLINE);
        locNameCB.setOnAction(e -> {
            validateSubmit();
        });
        directionCB.setFloatingText("Direction");
        directionCB.setFloatMode(FloatMode.INLINE);
        directionCB.setOnAction(e -> {
            validateSubmit();
        });
        submitButton.setDisable(true);
        submitButton.getStyleClass().add("signage-submit-btn");
        submitButton.setOnAction(e -> {
            facade.saveSignage(new Signage(facade.getAllAsListSignage().stream().mapToLong(Signage::getUuid).max().orElse(0) + 1, signageNameCB.getText(), locNameCB.getValue().getLongName(), directionCB.getValue()));
        });

        signageNameCB.setItems(signageNames);
        signageNameCB.setEditable(true);
        signageNameCB.setText("");
        locNameCB.setItems(facade.getAllAsListLocationName());
        locNameCB.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocationName object) {
                return object == null ? "" : object.getLongName();
            }

            @Override
            public LocationName fromString(String string) {
                return null;
            }
        });
        directionCB.setItems(FXCollections.observableArrayList(Signage.DirectionType.values()));

        VBox editingOptVBox = new VBox(
                new Label("Signage Name: "),
                signageNameCB,
                new Label("Location Name: "),
                locNameCB,
                new Label("Direction: "),
                directionCB);

        editingOptVBox.getStyleClass().add("signage-edit-options");

        editingVbox.getChildren().add(editingOptVBox);
        editingVbox.getChildren().add(submitButton);
        editingVbox.getStyleClass().add("signage-right-edit-vbox");
        VBox blankVBoxHi = new VBox();
        VBox blankVBoxLo = new VBox();
        VBox.setVgrow(blankVBoxHi, Priority.ALWAYS);
        VBox.setVgrow(blankVBoxLo, Priority.ALWAYS);

        VBox outerVbox = new VBox(blankVBoxHi, editingVbox, blankVBoxLo);
        VBox.setVgrow(outerVbox, Priority.ALWAYS);
        outerVbox.getStyleClass().add("signage-right-edit-outer-vbox");
        signageBodyStackPane.getChildren().add(outerVbox);
        outerVbox.visibleProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
        outerVbox.managedProperty().bind(App.getSingleton().getPrimaryStage().fullScreenProperty().not());
    }

    private void validateSubmit() {
        boolean invalid = locNameCB.getValue() == null || directionCB.getValue() == null;
        invalid |= signageNameCB.getText().isBlank();
        submitButton.setDisable(invalid);
    }

    private void setSignageName(String name) {
        filterUpdaters.forEach(updater -> updater.accept(name));
        if (editing) {
            return;
        }
        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) ->
                Platform.runLater(() -> {
                    updateView(facade.getAllAsListSignage().filtered(s -> s.getSignName().equals(name)));});

        App.getSingleton().getPrimaryStage().widthProperty().addListener(stageSizeListener);
        App.getSingleton().getPrimaryStage().heightProperty().addListener(stageSizeListener);
        updateView(facade.getAllAsListSignage().filtered(s -> s.getSignName().equals(name)));
    }

    private void buildSignage() {
//        updateView(facade.getAllAsListSignage());
        for (var direction : Signage.DirectionType.values()) {
            var signageList = facade.getAllAsListSignage().filtered(s -> true);
            Consumer<String> updated = s ->
                    signageList.setPredicate(signage -> signage.getDirectionType() == direction && signage.getSignName().equals(s));

            filterUpdaters.add(updated);
            updated.accept(prefSignageName);
            var listView = new PFXListView<>(
                    signageList,
                    s -> {
                        var hbox = new HBox(new Label(s.getLongName()));
                        hbox.setId(s.getUuid().toString());
                        addDelButton(hbox, s);
                        return hbox;
                    },
                    s -> s.getUuid().toString()); //getSignageTableView(signageList);
            listView.getStyleClass().add("signage-label");
            HBox signageHB = new HBox();
            signageHB.visibleProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
            signageHB.managedProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
            signageHB.getStyleClass().add("signage-list");
            switch (direction) {
                case UP -> {
                    signageHB.getChildren().add(iconUp);
                    signageHB.getChildren().add(listView);
                }
                case DOWN -> {
                    signageHB.getChildren().add(iconDown);
                    signageHB.getChildren().add(listView);
                }
                case LEFT -> {
                    signageHB.getChildren().add(iconLeft);
                    signageHB.getChildren().add(listView);
                }
                case RIGHT -> {
                    signageHB.getChildren().add(iconRight);
                    signageHB.getChildren().add(listView);
                }
                case HERE -> {
                    listView.getStyleClass().add("signage-label-Here");
                    var label = new Label();
                    label.fontProperty().bind(Bindings.createObjectBinding(() ->
                            Font.font(App.getSingleton().getPrimaryStage().getWidth() / 20), App.getSingleton().getPrimaryStage().widthProperty()));
                    signageHeaderLeft.getChildren().add(iconHere);
                    signageHeaderLeft.getChildren().add(listView);
                    signageHeaderLeft.visibleProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
                    signageHeaderLeft.managedProperty().bind(Bindings.greaterThan(Bindings.size(signageList), 0));
                    continue;
                }
            }
//            signageHB.maxWidth(1000);
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
