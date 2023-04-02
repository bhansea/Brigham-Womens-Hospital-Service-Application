package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.util.Objects;

public class MapPageController {

    @FXML GesturePane gesturePane;

    @FXML
    public void initialize() {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(new Image(App.class.getResourceAsStream("../assets/00_thegroundfloor.png")));
        Canvas canvas = new Canvas();

        stackPane.getChildren().addAll(imageView, canvas);
        gesturePane.setContent(stackPane);
    }



}
