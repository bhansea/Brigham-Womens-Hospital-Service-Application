package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.CartesianHeuristic;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.Palgo;
import edu.wpi.punchy_pegasi.generated.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.generated.NodeDaoImpl;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import net.kurobako.gesturefx.GesturePane;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Stream;

public class MapPageController {
    public HBox buttonContainer;
    public StackPane maps;
    public MFXComboBox nodeEndCombo;
    public MFXComboBox nodeStartCombo;
    public MFXButton pathfindButton;
    public Text pathfindStatus;
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

    private Map<Long, Node> nodes;
    private Map<Long, Edge> edges;

    @FXML
    public void initialize() {
        gesturePane.zoomTo(.1, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.ALWAYS);

        floors.values().forEach(Floor::init);

        show(floors.values().stream().toList().get(0));
        nodes = new NodeDaoImpl().getAll();
        edges = new EdgeDaoImpl().getAll();

        ObservableList<Node> nodesList = FXCollections.observableArrayList(nodes.values().stream().sorted(Comparator.comparing(Node::getNodeID)).toList());
        nodeStartCombo.setItems(nodesList);
        var nodeToID = new StringConverter<Node>() {
            @Override
            public String toString(Node node) {
                return node == null ? "" : node.getNodeID().toString();
            }

            @Override
            public Node fromString(String string) {
                return nodesList.stream().filter(n -> n.getNodeID().toString().equals(string)).findFirst().orElse(null);
            }
        };
        nodeStartCombo.setConverter(nodeToID);
        nodeEndCombo.setItems(nodesList);
        nodeEndCombo.setConverter(nodeToID);
    }
    @FXML
    public void graphicalSelect() {
        nodeStartCombo.clearSelection();
        nodeEndCombo.clearSelection();
        nodeStartCombo.setDisable(true);
        nodeEndCombo.setDisable(true);
        pathfindButton.setDisable(true);
        nodes.values().forEach(n->{
            var point = drawCircle(n, "#FFFF00");
            point.setRadius(10);
            point.setStrokeWidth(2);
            point.setStroke(Color.valueOf("#000000"));
            point.setOnMouseClicked(e->{
                point.setStroke(Color.valueOf("#00FFFF"));
                var startSet = nodeStartCombo.getSelectedItem() != null;
                if(!startSet)
                    nodeStartCombo.selectItem(n);
                else {
                    nodeEndCombo.selectItem(n);
                    pathFindWithSelectedNodes();
                    nodeStartCombo.setDisable(false);
                    nodeEndCombo.setDisable(false);
                    pathfindButton.setDisable(false);
                }
            });
        });
    }
    @FXML
    private void pathFindWithSelectedNodes() {
        var startNode = (Node) nodeStartCombo.getSelectedItem();
        var endNode = (Node) nodeEndCombo.getSelectedItem();
        if (startNode == null || endNode == null)
            return;
        pathfindStatus.setText(pathFind(startNode, endNode));
    }

    public void show(Floor floor) {
        currentFloor = floor;
        floor.root.setVisible(true);
        floor.button.setStyle("-fx-background-color: -pp-light-blue; -fx-text-fill: black");
        floors.values().stream().filter(f -> !Objects.equals(f.identifier, floor.identifier)).forEach(f -> {
            f.root.setVisible(false);
            f.button.setStyle("-fx-background-color: -pp-dark-blue; -fx-text-fill: white");
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

    private void drawYouAreHere(Node node) {
        var floor = floors.get(node.getFloor());
        if (floor == null)
            return;
        var img = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/you-are-here.jpg")));
        var imgView = new ImageView(img);
        imgView.setLayoutX(node.getXcoord() - 40);
        imgView.setLayoutY(node.getYcoord() - 80);
        imgView.setFitHeight(80);
        imgView.setFitWidth(80);
        floor.canvas.getChildren().add(imgView);
    }

    public Circle drawCircle(Node node, String color) {
        var floor = floors.get(node.getFloor());
        if (floor == null)
            return null;
        var circle = new Circle(node.getXcoord(), node.getYcoord(), 15);
        circle.setFill(Color.valueOf(color));
        floor.canvas.getChildren().add(circle);
        return circle;
    }

    private void focusOn(Node node) {
        var floor = floors.get(node.getFloor());
        if (floor == null)
            return;
        show(floor);
        gesturePane.centreOn(new Point2D(node.getXcoord(), node.getYcoord()));
    }

    public String pathFind(Node start, Node end) {
        var edgeList = edges.values().stream().map(v -> new Pair<>(v.getStartNode(), v.getEndNode())).toList();

        var graph = new Graph<>(nodes, edgeList);
        var heuristic = new CartesianHeuristic();
        var palgo = new Palgo<>(graph, heuristic, heuristic);
        try {
            var path = palgo.AStar(start, end).stream().toList();
            for (var floor : floors.values())
                floor.canvas.getChildren().clear();
            String currentFloor = path.get(0).getFloor();
            List<Node> currentPath = new ArrayList<>();
            for (var node : path) {
                if (!node.getFloor().equals(currentFloor)) {
                    drawLine(floors.get(currentPath.get(0).getFloor()), currentPath);
                    var endNode = currentPath.get(currentPath.size() - 1);
                    drawCircle(node, "#1040f0").setOnMouseClicked(e -> show(floors.get(endNode.getFloor())));
                    drawCircle(endNode, "#1040f0").setOnMouseClicked(e -> show(floors.get(node.getFloor())));
                    currentPath = new ArrayList<>();
                    currentFloor = node.getFloor();
                }
                currentPath.add(node);
            }
            drawLine(floors.get(currentPath.get(0).getFloor()), currentPath);
            drawYouAreHere(path.get(0));
            drawCircle(path.get(path.size() - 1), "#3cb043");
            focusOn(path.get(0));
            return "";
        } catch (IllegalStateException e) {
            return "Path not found";
        }
    }

    @RequiredArgsConstructor
    private class Floor {
        final String path;
        final String humanReadableName;
        final String identifier;
        MFXButton button = new MFXButton();
        Group root = new Group();
        Group canvas = new Group();
        ImageView imageView = new ImageView();

        public void init() {
            root.getChildren().addAll(imageView, canvas);
            button.setText(this.humanReadableName);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> show(this));
            buttonContainer.getChildren().add(button);
            maps.getChildren().add(root);
            new Thread(this::loadImage).start();
        }
        private static Map<String, Image> imageCache = new HashMap<>();

        public void loadImage() {
            Image image;
            if(imageCache.containsKey(path))
                image = imageCache.get(path);
            else {
                image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(path)));
                imageCache.put(path, image);
            }
            imageView.imageProperty().set(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
            gesturePane.setFitMode(GesturePane.FitMode.CENTER);
            gesturePane.zoomTo(.3, new Point2D(image.getWidth()/2, image.getHeight()/2));
        }
    }
}
