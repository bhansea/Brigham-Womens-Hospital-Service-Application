package edu.wpi.punchy_pegasi.frontend.map;


import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public enum HospitalFloor {
    L2("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"),
    L1("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"),
    F1("frontend/assets/map/01_thefirstfloor.png", "First Layer", "1"),
    F2("frontend/assets/map/02_thesecondfloor.png", "Second Layer", "2"),
    F3("frontend/assets/map/03_thethirdfloor.png", "Third Layer", "3");
    private static final Map<String, Image> imageCache = new ConcurrentHashMap<>();
    public static Map<String, HospitalFloor> floorMap = Arrays.stream(values()).map(f -> Map.entry(f.identifier, f)).collect(
            ConcurrentHashMap::new,
            (m, v) -> m.put(v.getKey(), v.getValue()),
            ConcurrentHashMap::putAll
    );
    final String path;
    final String humanReadableName;
    final String identifier;
    PFXButton button = new PFXButton();
    Group root = new Group();
    Group lineCanvas = new Group();
    Group nodeCanvas = new Group();
    Group tooltipCanvas = new Group();
    ImageView imageView = new ImageView();

    void init() {
        tooltipCanvas.setPickOnBounds(false);
        root.getChildren().addAll(imageView, lineCanvas, nodeCanvas, tooltipCanvas);
        button.setText(this.identifier);
        button.getStyleClass().add("floor-selector");
        App.getSingleton().getExecutorService().execute(this::loadImage);
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