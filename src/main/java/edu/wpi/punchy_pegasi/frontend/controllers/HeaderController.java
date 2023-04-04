package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HeaderController implements PropertyChangeListener {
    public StackPane headerStackpane;
//    @FXML
//    private VBox imageContainer;
//    @FXML
//    private ImageView bannerImage;
//    @FXML
//    private GridPane headerGrid;
    @FXML
    private Button exitButton;
    @FXML
    private Button homeButton;
//    @FXML
//    private Pane clipper;

    @FXML
    private void toHome(ActionEvent event) {
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    private void exit() {
        App.exit();
    }

    @FXML
    private void initialize() {
        App.getSingleton().addPropertyChangeListener(this);
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
