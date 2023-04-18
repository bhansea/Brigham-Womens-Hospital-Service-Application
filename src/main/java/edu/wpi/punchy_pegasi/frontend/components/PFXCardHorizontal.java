package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardHorizontal extends HBox{
    private final VBox leftPane = new VBox();
    private final VBox rightPane = new VBox();
    private final ImageView image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
    private final PFXPicker picker = new PFXPicker();
    private final HBox selection = new HBox();
    private final VBox info = new VBox();
    private final HBox spacer = new HBox();
    private final Label titleLabel;
    private final Label subtitleLabel;
    private final Label quantity;

    public PFXCardHorizontal(){
        super();
        image.setFitHeight(400);
        image.setFitWidth(350);
        titleLabel = new Label("Meal Name");
        subtitleLabel = new Label("A very nice meal");
        quantity = new Label("5 Available");
        rightPane.getChildren().addAll(info, spacer, selection);

        getStyleClass().add("pfx-card-horizontal-container");
        getChildren().addAll(leftPane, rightPane);

        info.getChildren().addAll(titleLabel, subtitleLabel, selection);

        leftPane.getStyleClass().add("pfx-card-horizontal-imageBox");
        rightPane.getStyleClass().add("pfx-card-horizontal-stats");
        leftPane.getChildren().add(image);

        selection.getStyleClass().add("pfx-card-horizontal-selection");
        selection.getChildren().addAll(quantity, picker);

        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

    }
}
