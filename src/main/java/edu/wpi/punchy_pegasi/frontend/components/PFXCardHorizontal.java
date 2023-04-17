package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardHorizontal extends HBox{
    private final VBox imageBox = new VBox();
    private final ImageView image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
    private final HBox stats = new HBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    private final PFXPicker picker = new PFXPicker();

    public PFXCardHorizontal(){
        super();
        getStyleClass("pfx-card-horizontal-container");
    }
}
