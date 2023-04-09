package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.CartesianHeuristic;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.Palgo;
import edu.wpi.punchy_pegasi.frontend.DragController;
import edu.wpi.punchy_pegasi.frontend.animations.Bobbing;
import edu.wpi.punchy_pegasi.frontend.authentication.AccountType;
import edu.wpi.punchy_pegasi.generated.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.generated.LocationNameDaoImpl;
import edu.wpi.punchy_pegasi.generated.MoveDaoImpl;
import edu.wpi.punchy_pegasi.generated.NodeDaoImpl;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kurobako.gesturefx.GesturePane;
import org.javatuples.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MapPageController {
    private static Image downArrow = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/double-chevron-down-512.png")));
    @FXML
    private HBox buttonContainer;
    @FXML
    private StackPane maps;
    @FXML
    private MFXFilterComboBox<Node> nodeEndCombo;
    @FXML
    private MFXFilterComboBox<Node> nodeStartCombo;
    @FXML
    private MFXButton pathfindButton;
    @FXML
    GesturePane gesturePane;
    private Map<String, Floor> floors = new LinkedHashMap<>() {{
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
    private Text pathfindStatus;
    private Map<Long, LocationName> locations;
    private Map<Long, Move> moves;

    @FXML
    private void initialize() {
        gesturePane.zoomTo(.1, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.ALWAYS);

        floors.values().forEach(Floor::init);

        show(floors.values().stream().toList().get(0));
        nodes = new NodeDaoImpl().getAll();
        edges = new EdgeDaoImpl().getAll();
        moves = new MoveDaoImpl().getAll();
        locations = new LocationNameDaoImpl().getAll();

        // TODO: This code makes the assumption that if the location names column of moves is a foreign key and thus there is no null checking when converting from a move to a location name
        var movesByNodeID = moves.values().stream().collect(Collectors.toMap(Move::getNodeID, v -> v));
        var locationsByLongName = locations.values().stream().collect(Collectors.toMap(LocationName::getLongName, v -> v));

        ObservableList<Node> nodesList = FXCollections.observableArrayList(nodes.values().stream().filter(v -> {
            var move = movesByNodeID.get(v.getNodeID());
            if (move == null) return false;
            var location = locationsByLongName.get(move.getLongName());
            var locationType = location.getNodeType();
            if (locationType == LocationName.NodeType.HALL || locationType == LocationName.NodeType.STAI || locationType == LocationName.NodeType.ELEV)
                return false;
            return true;
        }).sorted(Comparator.comparing(Node::getNodeID)).toList());
        var nodeToID = new StringConverter<Node>() {
            @Override
            public String toString(Node node) {
                if (node == null) return "";
                var move = movesByNodeID.get(node.getNodeID()).getLongName();
                if (move == null) return "";
                return locationsByLongName.get(move).getLongName();
            }

            @Override
            public Node fromString(String string) {
                return nodes.get(moves.values().stream().filter(m -> m.getLongName() == string).findFirst().get().getNodeID());// nodesList.stream().filter(n -> n.getNodeID().toString().equals(string)).findFirst().orElse(null);
            }
        };
        nodeStartCombo.setFilterFunction(s -> n -> nodeToID.toString(n).toLowerCase().contains(s.toLowerCase()));
        nodeStartCombo.setItems(nodesList);
        nodeStartCombo.setConverter(nodeToID);
        nodeEndCombo.setItems(nodesList);
        nodeEndCombo.setFilterFunction(s -> n -> nodeToID.toString(n).toLowerCase().contains(s.toLowerCase()));
        nodeEndCombo.setConverter(nodeToID);
    }

    @FXML
    private void graphicalSelect() {
        nodeStartCombo.clearSelection();
        nodeEndCombo.clearSelection();
        nodeStartCombo.setDisable(true);
        nodeEndCombo.setDisable(true);
        pathfindButton.setDisable(true);
        nodes.values().forEach(n -> {
            var point = drawCircle(n, "#FFFF00");
            point.setRadius(10);
            point.setStrokeWidth(2);
            point.setStroke(Color.valueOf("#000000"));
            point.setOnMouseClicked(e -> {
                point.setStroke(Color.valueOf("#00FFFF"));
                var startSet = nodeStartCombo.getSelectedItem() != null;
                if (!startSet) nodeStartCombo.selectItem(n);
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
    private void clearCanvas() {
        floors.values().forEach(Floor::clearFloor);
    }

    @FXML
    private void editNodes() {
        if (App.getSingleton().getAccountType() != AccountType.ADMIN) return;
        nodes.values().forEach(n -> {
            var point = drawCircle(n, "#FFFF00");
            point.setRadius(10);
            point.setStrokeWidth(2);
            point.setStroke(Color.valueOf("#000000"));
            DragController dragController = new DragController(point, true);
        });
    }

    @FXML
    private void pathFindWithSelectedNodes() {
        var startNode = (Node) nodeStartCombo.getSelectedItem();
        var endNode = (Node) nodeEndCombo.getSelectedItem();
        if (startNode == null || endNode == null) return;
        pathfindStatus.setText(pathFind(startNode, endNode));
    }

    private void show(Floor floor) {
        currentFloor = floor;
        floor.root.setVisible(true);
        floor.button.setStyle("-fx-background-color: -pp-light-blue; -fx-text-fill: black");
        floors.values().stream().filter(f -> !Objects.equals(f.identifier, floor.identifier)).forEach(f -> {
            f.root.setVisible(false);
            f.button.setStyle("-fx-background-color: -pp-dark-blue; -fx-text-fill: white");
        });
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

    private void drawLine(Floor floor, List<Node> nodes) {
        if (nodes.size() < 2 || floor == null) return;
        var polyline = new Polyline();
        polyline.setStroke(Color.valueOf("#FF000099"));
        polyline.setStrokeWidth(10);
        polyline.getPoints().addAll(nodes.stream().flatMap(n -> Stream.of(n.getXcoord(), n.getYcoord())).map(Double::valueOf).toList());
        floor.canvas.getChildren().add(0, polyline);
    }

    private Circle drawCircle(Node node, String color) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return null;
        var circle = new Circle(node.getXcoord(), node.getYcoord(), 15);
        circle.setFill(Color.valueOf(color));
        floor.canvas.getChildren().add(circle);
        return circle;
    }

    private javafx.scene.Node drawArrow(Node node, boolean up) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return null;
        var group = new Group();
        group.setLayoutX(node.getXcoord());
        group.setLayoutY(node.getYcoord());
        var box = new Rectangle(-12.5, -12.5, 25, 25);
        box.setFill(Color.valueOf("#1040f0"));
        var arrow = new ImageView(downArrow);
        if(up)
            arrow.setRotate(180);
        arrow.setFitWidth(15);
        arrow.setFitHeight(15);
        arrow.setLayoutX(-7.5);
        arrow.setLayoutY(-7.5);
        group.getChildren().addAll(box, arrow);
        floor.canvas.getChildren().add(group);
        new Bobbing(arrow).play();
        return group;
    }

    private void focusOn(Node node) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return;
        show(floor);
        gesturePane.centreOn(new Point2D(node.getXcoord(), node.getYcoord()));
    }

    private String pathFind(Node start, Node end) {
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
                    drawArrow(node, endNode.getFloorNum() > node.getFloorNum()).setOnMouseClicked(e ->Platform.runLater(() -> show(floors.get(endNode.getFloor()))));
                    drawArrow(endNode, endNode.getFloorNum() < node.getFloorNum()).setOnMouseClicked(e -> Platform.runLater(()->show(floors.get(node.getFloor()))));
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

        private static Image imageCache;

        private void init() {
            root.getChildren().addAll(imageView, canvas);
            button.setText(this.humanReadableName);
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> show(this));
            buttonContainer.getChildren().add(button);
            maps.getChildren().add(root);
            new Thread(this::loadImage).start();
        }

        void loadImage() {
            Image image;
            if (imageCache != null) image = imageCache;
            else imageCache = image = new Image(Objects.requireNonNull(App.class.getResourceAsStream(path)));
            Platform.runLater(() -> {
                imageView.imageProperty().set(image);
                imageView.setFitHeight(image.getHeight());
                imageView.setFitWidth(image.getWidth());
                gesturePane.setFitMode(GesturePane.FitMode.CENTER);
                gesturePane.zoomTo(.3, new Point2D(image.getWidth() / 2, image.getHeight() / 2));
            });
        }

        void clearFloor() {
            canvas.getChildren().clear();
        }
    }
}
