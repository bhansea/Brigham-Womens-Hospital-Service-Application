package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class LayoutController extends StackPane {

    @FXML
    @Getter
    BorderPane ViewPane;

    @FXML
    private BorderPane layoutPane;

    @FXML
    private BorderPane notificationsOverlay;
    @FXML
    private BorderPane alertOverlay;

    @FXML
    private VBox notificationsContainer;

    @FXML
    private HeaderController topLayout;

    @FXML
    private SidebarController leftLayout;
    private AtomicBoolean alert = new AtomicBoolean(false);

    public LayoutController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/layouts/AppLayout.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//        notificationsOverlay.setPickOnBounds(false);
//
//        notificationsOverlay.setVisible(false);
//        notificationsOverlay.setManaged(false);
        notificationsContainer.setPickOnBounds(false);
        alertOverlay.setVisible(false);
        alertOverlay.setManaged(false);
    }

    public void showLeftLayout(boolean hide) {
        leftLayout.setVisible(hide);
        leftLayout.setManaged(hide);
    }

    public void showTopLayout(boolean hide) {
        topLayout.setVisible(hide);
        topLayout.setManaged(hide);
    }

    public void notify(String title, String body) {
        var notification = new VBox();
        var topBar = new HBox();
        var titleLabel = new Label(title);
        var grow = new HBox();
        var dismissButton = new PFXButton("");
        var separator = new Separator();
        var bodyLabel = new Label(body);
        notification.getStyleClass().add("notification-popup");
        topBar.getStyleClass().add("notification-title-bar");
        dismissButton.getStyleClass().add("notification-dismiss");
        titleLabel.getStyleClass().add("notification-title");
        titleLabel.getStyleClass().add("notification-body");
        titleLabel.setWrapText(true);
        bodyLabel.setWrapText(true);
        HBox.setHgrow(grow, Priority.ALWAYS);
        dismissButton.setGraphic(new PFXIcon(MaterialSymbols.CLOSE));
        dismissButton.setOnAction(e -> notificationsContainer.getChildren().remove(notification));
        var showBody = !body.isBlank();
        bodyLabel.setVisible(showBody);
        bodyLabel.setManaged(showBody);
        separator.setVisible(showBody);
        separator.setManaged(showBody);
        topBar.getChildren().addAll(titleLabel, grow, dismissButton);
        notification.getChildren().addAll(topBar, separator, bodyLabel);
        notificationsContainer.getChildren().add(notification);
    }

    public void showAlert(Node node) {
        if (!alert.compareAndSet(false, true)) return;
        alertOverlay.setCenter(node);
        alertOverlay.setVisible(true);
        alertOverlay.setManaged(true);
    }

    public void dismissAlert() {
        alertOverlay.setCenter(null);
        alertOverlay.setVisible(false);
        alertOverlay.setManaged(false);
    }

    @FXML
    public void initialize() {
    }
}
