package edu.wpi.punchy_pegasi.frontend.map;


import edu.wpi.punchy_pegasi.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
class HospitalFloor {
    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
    final String path;
    final String humanReadableName;
    final String identifier;
    MFXButton button = new MFXButton();
    Group root = new Group();
    Group lineCanvas = new Group();
    Group nodeCanvas = new Group();
    Group tooltipCanvas = new Group();
    ImageView imageView = new ImageView();
    void init() {
        tooltipCanvas.setPickOnBounds(false);
        root.getChildren().addAll(imageView, lineCanvas, nodeCanvas, tooltipCanvas);
        button.setText(this.humanReadableName);
        new Thread(this::loadImage).start();
    }

    void loadImage() {
        Image image;
        if (imageCache.containsKey(identifier))
            image = imageCache.get(identifier);
        else {
            image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(path)));
            imageCache.put(identifier, image);
        }
        Platform.runLater(() -> {
            imageView.imageProperty().set(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
        });
    }

    void clearFloor() {
        lineCanvas.getChildren().clear();
        tooltipCanvas.getChildren().clear();
        nodeCanvas.getChildren().clear();
    }
}