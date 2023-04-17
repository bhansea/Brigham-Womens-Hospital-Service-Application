package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardHorizontal extends HBox{
    private final VBox leftPane = new VBox();
    private final VBox rightPane = new VBox();
    private final VBox imageBox = new VBox();
    private final ImageView image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
    private final HBox stats = new HBox();
    private final VBox title = new VBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    private final PFXPicker picker = new PFXPicker();

    public PFXCardHorizontal(){
        super();
        getStyleClass().add("pfx-card-horizontal-container");
        getChildren().addAll(leftPane, rightPane);
        leftPane.getStyleClass().add("pfx-card-horizontal-container");
        rightPane.getStyleClass().add("pfx-card-horizontal-container");
        leftPane.getChildren().addAll(imageBox, stats);
        rightPane.getChildren().add(info);
        imageBox.getStyleClass().add("pfx-card-horizontal-imageBox");
        imageBox.getChildren().add(image);
        HBox.setHgrow(stats, Priority.ALWAYS);

        stats.getStyleClass().add("pfx-card-horizontal-stats");
        HBox.setHgrow(title, Priority.ALWAYS);
        HBox.setHgrow(selection, Priority.ALWAYS);
        stats.getChildren().addAll(title, selection);

        title.getStyleClass().add("pfx-card-horizontal-info");
        selection.getStyleClass().add("pfx-card-horizontal-selection");

        var titleLabel = new Label("Meal Name");
        title.getChildren().add(titleLabel);

        info.getStyleClass().add("pfx-card-horizontal-info");
        HBox.setHgrow(info, Priority.ALWAYS);
        var itemInfo = new Label("A very nice meal. Use the + or - buttons to choose the quantity you want");
        info.getChildren().add(itemInfo);


        var quantity = new Label("1");
        selection.getChildren().addAll(quantity, picker);
    }
}
