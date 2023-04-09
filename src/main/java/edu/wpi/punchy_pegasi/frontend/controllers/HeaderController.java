package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HeaderController implements PropertyChangeListener {
    public StackPane headerStackpane;
    @FXML HBox navButtonContainer;
    @FXML
    private Button exitButton;
    @FXML
    private Button homeButton;

    @FXML
    private void toHome(ActionEvent event) {
        App.getSingleton().navigate(Screen.HOME);
    }

    @FXML
    private void exit() {
        App.getSingleton().exit();
    }

    @FXML
    private void initialize() {

        App.getSingleton().addPropertyChangeListener(this);

        for (var entry : Screen.values()) {
            if (entry.name().toLowerCase().contains("request")
                || entry.name().toLowerCase().contains("home"))
                continue;
            Button button = new Button();
            button.setText(entry.getReadable());
            button.setStyle("-fx-background-color: transparent; -fx-text-fill: #f1f1f1; -fx-font-size: 21");
            button.setOnMouseClicked(e -> {
                App.getSingleton().navigate(entry);
            });
            navButtonContainer.getChildren().add(button);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue().equals(Screen.HOME)) {
            exitButton.setVisible(true);
            exitButton.setManaged(true);
            homeButton.setVisible(false);
            homeButton.setManaged(false);
        } else {
            exitButton.setVisible(false);
            exitButton.setManaged(false);
            homeButton.setVisible(true);
            homeButton.setManaged(true);
        }
    }
}
