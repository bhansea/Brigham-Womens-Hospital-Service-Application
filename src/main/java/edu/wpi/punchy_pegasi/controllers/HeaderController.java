package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
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
    @FXML
    private VBox imageContainer;
    @FXML
    private ImageView bannerImage;
    @FXML
    private GridPane headerGrid;
    @FXML
    private Button exitButton;
    @FXML
    private Button homeButton;
    @FXML
    private Pane clipper;

    @FXML
    private void toHome(ActionEvent event) {
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    private void exit(ActionEvent event) {
        App.exit();
    }

    @FXML
    private void initialize() {
        // get second column to fill width
        final var col = new ColumnConstraints(0, Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
        col.setHgrow(Priority.ALWAYS);
        headerGrid.getRowConstraints().add(new RowConstraints(100));
        headerGrid.getColumnConstraints().add(col);

        var clipRect = new Rectangle(100, 100);
        clipRect.widthProperty().bind(imageContainer.widthProperty());

        clipper
                .clipProperty()
                .bind(
                        new ObjectBinding<Node>() {
                            {
                                bind(clipper.widthProperty(), clipper.heightProperty());
                            }

                            @Override
                            protected Node computeValue() {
                                return new Rectangle(clipper.getWidth(), clipper.getHeight());
                            }
                        });
        bannerImage.fitWidthProperty().bind(imageContainer.widthProperty());

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
