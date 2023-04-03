package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.backend.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.backend.Node;
import edu.wpi.punchy_pegasi.backend.NodeDaoImpl;
import edu.wpi.punchy_pegasi.backend.pathfinding.CartesianHeuristic;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.Palgo;
import edu.wpi.punchy_pegasi.frontend.App;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import net.kurobako.gesturefx.GesturePane;
import org.javatuples.Pair;

import java.util.*;

public class MapPageController {
    public HBox buttonContainer;
    @FXML
    GesturePane gesturePane;
    private Map<String, Floor> floors = new HashMap<>() {{
        put("00", new Floor("../../../../assets/00_thegroundfloor.png", "Ground Floor", "00"));
        put("L1", new Floor("../../../../assets/00_thelowerlevel1.png", "Lower Level 1", "L1"));
        put("L2", new Floor("../../../../assets/00_thelowerlevel2.png", "Lower Level 2", "L2"));
        put("1", new Floor("../../../../assets/01_thefirstfloor.png", "First Floor", "1"));
        put("2", new Floor("../../../../assets/02_thesecondfloor.png", "Second Floor", "2"));
        put("3", new Floor("../../../../assets/03_thethirdfloor.png", "Third Floor", "3"));
    }};
    private Floor currentFloor;

    @FXML
    public void initialize() {
        StackPane basePane = new StackPane();
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.AS_NEEDED);
        gesturePane.setContent(basePane);
        gesturePane.zoomTo(.3, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));

        for (var floor : floors.values())
            basePane.getChildren().add(floor.root);

        show(floors.values().stream().findFirst().get());

        for (var floor : floors.values()) {
            Button button = new Button();
            button.setText(floor.humanReadableName);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> show(floor));
            buttonContainer.getChildren().add(button);
        }
        currentFloor.canvas.getGraphicsContext2D().setLineWidth(5);
        currentFloor.canvas.getGraphicsContext2D().setStroke(new Color(1, 0, 0, 1));
        drawLine(currentFloor, Arrays.asList(new Point2D(10, 100),
                new Point2D(100, 100),
                new Point2D(1000, 150),
                new Point2D(2000, 1000),
                new Point2D(10, 100)));
    }

    public void show(Floor floor) {
        currentFloor = floor;
        currentFloor.show(true);
        floors.values().stream().filter(v -> !v.equals(floor)).forEach(v -> v.show(false));
    }

    public void drawLine(Floor floor, List<Point2D> points) {
        if (points.size() < 2 || floor == null)
            return;
        var gc = floor.canvas.getGraphicsContext2D();
        gc.moveTo(points.get(0).getX(), points.get(0).getY());
        for (var point : points.stream().skip(1).toList())
            gc.lineTo(point.getX(), point.getY());
        gc.stroke();
    }

    public void pathFind() {
        var edges = new EdgeDaoImpl().getAll().values().stream().map(v -> new Pair<>(Long.valueOf(v.getStartNode()), Long.valueOf(v.getEndNode()))).toList();
        var nodes = new NodeDaoImpl().getAll();

        var graph = new Graph<>(nodes, edges);
        var heuristic = new CartesianHeuristic();
        var palgo = new Palgo<>(graph, heuristic, heuristic);
        var path = palgo.AStar(nodes.get(100), nodes.get(200)).stream().toList();
        var it = path.iterator();
        Node previous = null;
        while (it.hasNext()) {
            Node current = null;
            Floor floor = null;
            var points = new ArrayList<Point2D>();
            while (it.hasNext() && (previous == null || (current = it.next()).getFloor().equals(previous.getFloor()))) {
                if (floor == null)
                    floor = floors.get(current.getFloor());
                points.add(new Point2D(current.getXcoord(), current.getYcoord()));
                previous = current;
            }
            previous = null;
            drawLine(floor, points);
        }
    }

    private class Floor {
        final String path;
        final String humanReadableName;
        final String identifier;
        StackPane root;
        Canvas canvas;

        public Floor(String path, String humanReadableName, String identifier) {
            this.path = path;
            this.humanReadableName = humanReadableName;
            this.identifier = identifier;
            var image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(path)));
            var imageView = new ImageView(image);
            root = new StackPane();
            canvas = new Canvas(image.getWidth(), image.getHeight());
            root.getChildren().addAll(imageView, canvas);
        }

        public void show(boolean bool) {
            root.setVisible(bool);
        }
    }


    // Focus map to a specific point(cord)
    // Zoom map to a certain scale, create a new class extends floor type
    // draw a line on map(id)
}
