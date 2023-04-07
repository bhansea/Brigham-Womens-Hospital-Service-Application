package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.events.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HeaderController implements PropertyChangeListener {
    public StackPane headerStackpane;
    @FXML HBox navButtonContainer;
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
                Navigation.navigate(entry);
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
