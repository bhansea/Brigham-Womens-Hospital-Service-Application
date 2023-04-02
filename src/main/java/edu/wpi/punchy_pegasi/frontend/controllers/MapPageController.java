package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class MapPageController {
    public HBox buttonContainer;


    record FloorType(String path, String humanReadableName, String identifier) {};

    @FXML GesturePane gesturePane;

    private Map<String, StackPane> stackPanes = new HashMap<>();
    private StackPane currentStackPane;

    @FXML
    public void initialize() {

        FloorType[] floorTypes = new FloorType[] {
                (new FloorType("../../../../assets/00_thegroundfloor.png", "Ground Floor", "00")),
                (new FloorType("../../../../assets/00_thelowerlevel1.png", "Lower Level 1", "L1")),
                (new FloorType("../../../../assets/00_thelowerlevel2.png", "Lower Level 2", "L2")),
                (new FloorType("../../../../assets/01_thefirstfloor.png", "First Floor", "1")),
                (new FloorType("../../../../assets/02_thesecondfloor.png", "Second Floor", "2")),
                (new FloorType("../../../../assets/03_thethirdfloor.png", "Third Floor", "3"))
        };
        StackPane basePane = new StackPane();

        Arrays.stream(floorTypes).forEach(f -> {
            var image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(f.path())));
            var imageView = new ImageView(image);
            var stackPane = new StackPane();
            var canvas = new Canvas();
            stackPane.getChildren().addAll(imageView, canvas);
            stackPanes.put(f.identifier(), stackPane);
        });

        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        gesturePane.setContent(basePane);

        for (StackPane stackpane: stackPanes.values()) {
            basePane.getChildren().add(stackpane);
        }

        show(floorTypes[0].identifier());

        for (FloorType floorType: floorTypes) {
            Button button = new Button();
            button.setText(floorType.humanReadableName());
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                show(floorType.identifier());
            });
            buttonContainer.getChildren().add(button);
        }
    }

    public void show(String id) {
        if(!stackPanes.containsKey(id))
            return;
        currentStackPane = stackPanes.get(id);
        currentStackPane.setVisible(true);

        stackPanes.entrySet().stream().filter(v->!v.getKey().equals(id)).forEach(v -> {
            v.getValue().setVisible(false);
        });
    }



    // Focus map to a specific point(cord)
    // Zoom map to a certain scale, create a new class extends floor type
    // draw a line on map(id)
}
