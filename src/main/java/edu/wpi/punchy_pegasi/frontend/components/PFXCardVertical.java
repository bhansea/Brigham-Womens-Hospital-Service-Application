package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class PFXCardVertical extends VBox {
    private final VBox imageBox = new VBox();
    private final ImageView image;
    private final HBox stats = new HBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    private final PFXPicker picker = new PFXPicker(this);
    private final Label titleLabel;
    private final Label subtitleLabel;
    private final Label quantity;
    private final Rectangle rectangle;

    private final int available;

    public PFXCardVertical() {
        super();
        image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
        image.setFitHeight(400);
        image.setFitWidth(300);
        titleLabel = new Label("Meal Name");
        subtitleLabel = new Label("A very nice meal");
        quantity = new Label("0 Available");
        available = 0;

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

        rectangle = new Rectangle(this.image.getFitWidth(), this.image.getFitHeight());
        rectangle.setArcHeight(12);
        rectangle.setArcWidth(12);
        this.image.setClip(rectangle);
    }

    public PFXCardVertical(String title, String subtitle, int quantity, Image image) {
        super();
        this.image = new ImageView(image);
        this.image.setFitHeight(250);
        this.image.setFitWidth(400);
        this.titleLabel = new Label(title);
        this.subtitleLabel = new Label(subtitle);
        this.available = quantity;
        this.quantity = new Label(Integer.toString(quantity));

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

        rectangle = new Rectangle(this.image.getFitWidth(), this.image.getFitHeight());
        rectangle.setArcHeight(24);
        rectangle.setArcWidth(24);
        this.image.setClip(rectangle);
    }

    public int getQuantity() {
        return picker.getQuantity();
    }

    public String getTitle() {
        return titleLabel.getText();
    }

    public void clearQuantity() {
        picker.clearQuantity();
    }

    public void addAvailable() {
        this.quantity.setText(Integer.toString(Integer.parseInt(this.quantity.getText()) + 1));
    }

    public void subtractAvailable() {
        this.quantity.setText(Integer.toString(Integer.parseInt(this.quantity.getText()) - 1));
    }

    public int getAvailable() {
        return available;
    }
}
