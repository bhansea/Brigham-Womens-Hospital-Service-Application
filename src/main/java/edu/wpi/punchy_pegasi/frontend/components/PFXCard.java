package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCard extends VBox {
    private final VBox imageBox = new VBox();
    private final ImageView image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
    private final HBox stats = new HBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    private final PFXPicker picker = new PFXPicker();

    public PFXCard() {
        super();
        getStyleClass().add("pfx-card-container");
        getChildren().addAll(imageBox, stats);
        imageBox.getStyleClass().add("pfx-card-imageBox");
        imageBox.getChildren().add(image);
        HBox.setHgrow(stats, Priority.ALWAYS);

        stats.getStyleClass().add("pfx-card-stats");
        HBox.setHgrow(info, Priority.ALWAYS);
        HBox.setHgrow(selection, Priority.ALWAYS);
        stats.getChildren().addAll(info, selection);

        info.getStyleClass().add("pfx-card-info");
        selection.getStyleClass().add("pfx-card-selection");

        var titleLabel = new Label("Meal Name");
        var subtitleLabel = new Label("A very nice meal");
        info.getChildren().addAll(titleLabel, subtitleLabel);

        var quantity = new Label("1");
        selection.getChildren().addAll(quantity, picker);
    }
}
