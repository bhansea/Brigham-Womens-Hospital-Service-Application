package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.CartesianHeuristic;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.Palgo;
import edu.wpi.punchy_pegasi.generated.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.generated.NodeDaoImpl;
import edu.wpi.punchy_pegasi.schema.Node;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import lombok.RequiredArgsConstructor;
import net.kurobako.gesturefx.GesturePane;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Stream;

public class MapPageController {
    public HBox buttonContainer;
    public StackPane maps;
    @FXML
    GesturePane gesturePane;
    private Map<String, Floor> floors = new HashMap<>() {{
        put("00", new Floor("frontend/assets/map/00_thegroundfloor.png", "Ground Floor", "00"));
        put("L1", new Floor("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"));
        put("L2", new Floor("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"));
        put("1", new Floor("frontend/assets/map/01_thefirstfloor.png", "First Floor", "1"));
        put("2", new Floor("frontend/assets/map/02_thesecondfloor.png", "Second Floor", "2"));
        put("3", new Floor("frontend/assets/map/03_thethirdfloor.png", "Third Floor", "3"));
    }};
    private Floor currentFloor;

    @FXML
    public void initialize() {
        gesturePane.zoomTo(.1, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.ALWAYS);

        for (var floor : floors.values()) {
            floor.init();
            maps.getChildren().add(floor.root);
            Button button = new Button();
        }
        show(floors.values().stream().toList().get(0));
        pathFind();
    }

    public void show(Floor floor) {
        currentFloor = floor;
        floor.root.setVisible(true);
        floor.button.setStyle("-fx-background-color: blue; -fx-text-fill: white");
        floors.values().stream().filter(f -> !Objects.equals(f.identifier, floor.identifier)).forEach(f -> {
            f.root.setVisible(false);
            f.button.setStyle("");
        });
    }

    public void drawLine(Floor floor, List<Node> nodes) {
        if (nodes.size() < 2 || floor == null)
            return;
        var polyline = new Polyline();
        polyline.setStroke(Color.valueOf("#FF000099"));
        polyline.setStrokeWidth(10);
        polyline.getPoints().addAll(nodes.stream().flatMap(n -> Stream.of(n.getXcoord(), n.getYcoord())).map(Double::valueOf).toList());
        floor.canvas.getChildren().add(polyline);
    }

    public void pathFind() {
        var edges = new EdgeDaoImpl().getAll().values().stream().map(v -> new Pair<>(v.getStartNode(), v.getEndNode())).toList();
        var nodes = new NodeDaoImpl().getAll();

        var graph = new Graph<>(nodes, edges);
        var heuristic = new CartesianHeuristic();
        var palgo = new Palgo<>(graph, heuristic, heuristic);
        try {
            var path = palgo.AStar(nodes.get("100"), nodes.get("300")).stream().toList();
            for (var floor : floors.values())
                floor.canvas.getChildren().clear();
            String currentFloor = path.get(0).getFloor();
            List<Node> currentPath = new ArrayList<>();
            for (var node : path) {
                if (!node.getFloor().equals(currentFloor)) {
                    drawLine(floors.get(currentPath.get(0).getFloor()), currentPath);
                    currentPath = new ArrayList<>();
                    currentFloor = node.getFloor();
                }
                currentPath.add(node);
            }
            drawLine(floors.get(currentPath.get(0).getFloor()), currentPath);
        } catch (IllegalStateException e) {
            System.out.println("Path not found");
        }
    }

    @RequiredArgsConstructor
    private class Floor {
        final String path;
        final String humanReadableName;
        final String identifier;
        Button button = new Button();
        StackPane root = new StackPane();
        Group canvas = new Group();
        ImageView imageView = new ImageView();

        public void init() {
            root = new StackPane();
            canvas = new Group();
            imageView = new ImageView();
            root.getChildren().addAll(imageView, canvas);
            button = new Button();
            button.setText(this.humanReadableName);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> show(this));
            buttonContainer.getChildren().add(button);
            new Thread(this::loadImage).start();
        }

        public void loadImage() {
            var image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(path)));
            imageView.imageProperty().set(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            gesturePane.setFitMode(GesturePane.FitMode.CENTER);
        }
    }
}
