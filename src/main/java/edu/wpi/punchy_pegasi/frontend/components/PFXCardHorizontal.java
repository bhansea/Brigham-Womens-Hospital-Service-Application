package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardHorizontal extends HBox{
    private final VBox leftPane = new VBox();
    private final VBox rightPane = new VBox();
    private final ImageView image;
    private final VBox spacer = new VBox();
    private final PFXPicker picker = new PFXPicker(null);
    private final HBox selection = new HBox();
    private final VBox info = new VBox();
    private final Label titleLabel;
    private final Label subtitleLabel;
    private final Label quantity;


    public PFXCardHorizontal(){
        super();
        this.image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
        this.image.setFitHeight(400);
        this.image.setFitWidth(350);
        titleLabel = new Label("Meal Name");
        subtitleLabel = new Label("A very nice meal");
        subtitleLabel.setWrapText(true);
        quantity = new Label("5 Available");
        rightPane.getChildren().addAll(info, spacer, selection);

        getStyleClass().add("pfx-card-horizontal-container");
        getChildren().addAll(leftPane, rightPane);

        info.getChildren().addAll(titleLabel, subtitleLabel);

        leftPane.getStyleClass().add("pfx-card-horizontal-imageBox");
        rightPane.getStyleClass().add("pfx-card-horizontal-stats");
        leftPane.getChildren().add(image);

        selection.getStyleClass().add("pfx-card-horizontal-selection");
        selection.getChildren().addAll(quantity, picker);

        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

    }

    public PFXCardHorizontal(String title, String subtitle, String quantity, Image image){
        super();
        this.image = new ImageView(image);
        this.image.setFitHeight(400);
        this.image.setFitWidth(350);
        titleLabel = new Label(title);
        subtitleLabel = new Label(subtitle);
        subtitleLabel.setWrapText(true);
        this.quantity = new Label(quantity);
        rightPane.getChildren().addAll(info, spacer, selection);

        getStyleClass().add("pfx-card-horizontal-container");
        getChildren().addAll(leftPane, rightPane);

        info.getChildren().addAll(titleLabel, subtitleLabel);

        leftPane.getStyleClass().add("pfx-card-horizontal-imageBox");
        rightPane.getStyleClass().add("pfx-card-horizontal-stats");
        leftPane.getChildren().add(this.image);

        selection.getStyleClass().add("pfx-card-horizontal-selection");
        selection.getChildren().addAll(this.quantity, picker);

        VBox.setVgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(leftPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);

    }
}
