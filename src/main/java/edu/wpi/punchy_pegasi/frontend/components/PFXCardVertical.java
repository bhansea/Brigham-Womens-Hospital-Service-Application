package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardVertical extends VBox {
    private final VBox imageBox = new VBox();
    private final ImageView image;
    private final HBox stats = new HBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    private final PFXPicker picker = new PFXPicker();
    private final Label titleLabel;
    private final Label subtitleLabel;
    private final Label quantity;

    public PFXCardVertical() {
        super();
        image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
        image.setFitHeight(400);
        image.setFitWidth(350);
        titleLabel = new Label("Meal Name");
        subtitleLabel = new Label("A very nice meal");
        quantity = new Label("5 Available");

        getStyleClass().add("pfx-card-vertical-container");
        getChildren().addAll(imageBox, stats);
        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
        imageBox.getChildren().add(image);
        HBox.setHgrow(stats, Priority.ALWAYS);

        stats.getStyleClass().add("pfx-card-vertical-stats");
        HBox.setHgrow(info, Priority.ALWAYS);
        HBox.setHgrow(selection, Priority.ALWAYS);
        stats.getChildren().addAll(info, selection);

        info.getStyleClass().add("pfx-card-vertical-info");
        selection.getStyleClass().add("pfx-card-vertical-selection");


        info.getChildren().addAll(titleLabel, subtitleLabel);
        selection.getChildren().addAll(quantity, picker);
    }

    public PFXCardVertical(Label title, Label subtitle, Label quantity, Image image) {
        super();
        this.image = new ImageView(image);
        this.image.setFitHeight(300);
        this.image.setFitWidth(500);
        this.titleLabel = title;
        this.subtitleLabel = subtitle;
        this.quantity = quantity;

        getStyleClass().add("pfx-card-vertical-container");
        getChildren().addAll(imageBox, stats);
        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
        imageBox.getChildren().add(this.image);
        HBox.setHgrow(stats, Priority.ALWAYS);

        stats.getStyleClass().add("pfx-card-vertical-stats");
        HBox.setHgrow(info, Priority.ALWAYS);
        HBox.setHgrow(selection, Priority.ALWAYS);
        stats.getChildren().addAll(info, selection);

        info.getStyleClass().add("pfx-card-vertical-info");
        selection.getStyleClass().add("pfx-card-vertical-selection");

        info.getChildren().addAll(titleLabel, subtitleLabel);
        selection.getChildren().addAll(this.quantity, picker);
    }
}
