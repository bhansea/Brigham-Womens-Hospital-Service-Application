package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.App;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class MapPageController {
    public VBox container;
    public HBox buttonContainer;

    //@FXML MFXButton plusButton;
    //@FXML MFXButton minusButton;

    record FloorType(String path, String humanReadableName, String identifier) {};

    @FXML GesturePane gesturePane;

    private List<StackPane> stackPanes;
    private StackPane currentStackPane;

    @FXML
    public void initialize() {

        FloorType[] floorTypes = new FloorType[] {
                (new FloorType("../assets/00_thegroundfloor.png", "Ground Floor", "00")),
                (new FloorType("../assets/00_thelowerlevel1.png", "Lower Level 1", "L1")),
                (new FloorType("../assets/00_thelowerlevel2.png", "Lower Level 2", "L2")),
                (new FloorType("../assets/01_thefirstfloor.png", "First Floor", "1")),
                (new FloorType("../assets/02_thesecondfloor.png", "Second Floor", "2")),
                (new FloorType("../assets/03_thethirdfloor.png", "Third Floor", "3"))
        };
        StackPane basePane = new StackPane();


        stackPanes = Arrays.stream(floorTypes).map(f -> {
            var image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(f.path())));
            var imageView = new ImageView(image);
            var stackPane = new StackPane();
            var canvas = new Canvas();
            stackPane.getChildren().addAll(imageView, canvas);
            return stackPane;
        }).toList();

        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        gesturePane.setContent(basePane);

        for (StackPane stackpane: stackPanes) {
            basePane.getChildren().add(stackpane);
        }

        show(0);




//        plusButton.setOnMouseClicked(event ->  {
//
//
//        });


    }

    public void show(int i) {
        stackPanes.get(i).setVisible(true);
        stackPanes.get(i).setManaged(true);
        currentStackPane = stackPanes.get(i);

        for (int j = 0; j < stackPanes.size(); j++) {
            if (j != i) {
                stackPanes.get(j).setVisible(false);
                stackPanes.get(j).setManaged(false);
            }
        }
        gesturePane.setContent(currentStackPane);
    }
}
