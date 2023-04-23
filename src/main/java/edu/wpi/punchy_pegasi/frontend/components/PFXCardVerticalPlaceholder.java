package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class PFXCardVerticalPlaceholder extends VBox {
    private final VBox imageBox = new VBox();
    // private final ImageView image;
    private final HBox stats = new HBox();
    private final VBox info = new VBox();
    private final VBox selection = new VBox();
    //private final PFXPicker picker = new PFXPicker();
    // private final Label titleLabel;
    //private final Label subtitleLabel;
    //private final Label quantity;

    public PFXCardVerticalPlaceholder() {
        super();
//        image = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/mealPicture.jpeg");
//        image.setFitHeight(400);
//        image.setFitWidth(300);
//        titleLabel = new Label("Meal Name");
//        subtitleLabel = new Label("A very nice meal");
//        quantity = new Label("0 Available");

        getStyleClass().add("pfx-card-vertical-container");
        getChildren().addAll(imageBox, stats);
        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
        //imageBox.getChildren().add(image);
        HBox.setHgrow(stats, Priority.ALWAYS);

        stats.getStyleClass().add("pfx-card-vertical-stats");
        HBox.setHgrow(info, Priority.ALWAYS);
        HBox.setHgrow(selection, Priority.ALWAYS);
        stats.getChildren().addAll(info, selection);

        info.getStyleClass().add("pfx-card-vertical-info");
        selection.getStyleClass().add("pfx-card-vertical-selection");


//        info.getChildren().addAll(titleLabel, subtitleLabel);
        //selection.getChildren().addAll(picker);
    }
}

//    public PFXCardVerticalPlaceholder(String title, String subtitle, int quantity, Image image) {
//        super();
//        this.image = new ImageView(image);
//        this.image.setFitHeight(300);
//        this.image.setFitWidth(400);
//        this.titleLabel = new Label(title);
//        this.subtitleLabel = new Label(subtitle);
//        this.quantity = new Label(Integer.toString(quantity));
//
//        getStyleClass().add("pfx-card-vertical-container");
//        getChildren().addAll(imageBox, stats);
//        imageBox.getStyleClass().add("pfx-card-vertical-imageBox");
//        imageBox.getChildren().add(this.image);
//        HBox.setHgrow(stats, Priority.ALWAYS);
//
//        stats.getStyleClass().add("pfx-card-vertical-stats");
//        HBox.setHgrow(info, Priority.ALWAYS);
//        HBox.setHgrow(selection, Priority.ALWAYS);
//        stats.getChildren().addAll(info, selection);
//
//        info.getStyleClass().add("pfx-card-vertical-info");
//        selection.getStyleClass().add("pfx-card-vertical-selection");
//
//        info.getChildren().addAll(titleLabel, subtitleLabel);
//        selection.getChildren().addAll(this.quantity, picker);
//    }
//
//    public int getQuantity() {
//        return picker.getQuantity();
//    }
//
//    public String getTitle() {
//        return titleLabel.getText();
//    }
//
//    public void clearQuantity() {
//        picker.clearQuantity();
//    }
//}
