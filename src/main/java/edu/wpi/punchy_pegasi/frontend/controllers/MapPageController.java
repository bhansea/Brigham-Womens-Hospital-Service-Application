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
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kurobako.gesturefx.GesturePane;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class MapPageController {
    private static final Image downArrow = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/double-chevron-down-512.png")));
    private final Map<String, Floor> floors = new LinkedHashMap<>() {{
        put("00", new Floor("frontend/assets/map/00_thegroundfloor.png", "Ground Floor", "00"));
        put("L1", new Floor("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"));
        put("L2", new Floor("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"));
        put("1", new Floor("frontend/assets/map/01_thefirstfloor.png", "First Floor", "1"));
        put("2", new Floor("frontend/assets/map/02_thesecondfloor.png", "Second Floor", "2"));
        put("3", new Floor("frontend/assets/map/03_thethirdfloor.png", "Third Floor", "3"));
    }};
    @FXML
    private MFXProgressBar commitProgress;
    @FXML
    private MFXButton commitButton;
    @FXML
    private MFXButton doneEditingButton;
    @FXML
    private MFXButton editButton;
    @FXML
    private VBox pathfinding;
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
    @FXML
    private VBox editing;
    private Floor currentFloor;

    private Map<Long, Node> nodes;
    private Map<Long, Edge> edges;
    @FXML
    private Text pathfindStatus;
    private Map<Long, LocationName> locations;
    private Map<Long, Move> moves;

    private Set<Node> editedNodes = ConcurrentHashMap.newKeySet();
    private AtomicBoolean commiting = new AtomicBoolean();
    private Map<Long, Move> movesByNodeID = new HashMap<>();
    private Map<String, LocationName> locationsByLongName = new HashMap<>();
    private StringConverter<Node> nodeToLocation = new StringConverter<>() {
        @Override
        public String toString(Node node) {
            if (node == null) return "";
            var move = movesByNodeID.get(node.getNodeID()).getLongName();
            if (move == null) return "";
            return locationsByLongName.get(move).getLongName();
        }

        @Override
        public Node fromString(String string) {
            return nodes.get(moves.values().stream().filter(m -> Objects.equals(m.getLongName(), string)).findFirst().get().getNodeID());// nodesList.stream().filter(n -> n.getNodeID().toString().equals(string)).findFirst().orElse(null);
        }
    };
    private ObservableList<Node> filteredNodes;

    @FXML
    private void clearCanvas() {
        floors.values().forEach(Floor::clearFloor);
    }

    @FXML
    private void graphicalSelect() {
        nodeStartCombo.clearSelection();
        nodeEndCombo.clearSelection();
        nodeStartCombo.setDisable(true);
        nodeEndCombo.setDisable(true);
        pathfindButton.setDisable(true);
        filteredNodes.forEach(n -> {
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return;
            var point = pointOpt.get();
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

    private void load() {
        nodes = new NodeDaoImpl().getAll();
        edges = new EdgeDaoImpl().getAll();
        moves = new MoveDaoImpl().getAll();
        locations = new LocationNameDaoImpl().getAll();

        movesByNodeID = moves.values().stream().collect(Collectors.toMap(Move::getNodeID, v -> v));
        locationsByLongName = locations.values().stream().collect(Collectors.toMap(LocationName::getLongName, v -> v));
        filteredNodes = FXCollections.observableArrayList(nodes.values().stream().filter(v -> {
            var move = movesByNodeID.get(v.getNodeID());
            if (move == null) return false;
            var location = locationsByLongName.get(move.getLongName());
            var locationType = location.getNodeType();
            return locationType != LocationName.NodeType.HALL && locationType != LocationName.NodeType.STAI && locationType != LocationName.NodeType.ELEV;
        }).sorted(Comparator.comparing(Node::getNodeID)).toList());
    }

    @FXML
    private void initialize() {
        gesturePane.zoomTo(.1, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.ALWAYS);

        floors.values().forEach(Floor::init);

        show(floors.get("1"));
        setEditingNodes(false);
        load();

        nodeStartCombo.setFilterFunction(s -> n -> nodeToLocation.toString(n).toLowerCase().contains(s.toLowerCase()));
        nodeStartCombo.setItems(filteredNodes);
        nodeStartCombo.setConverter(nodeToLocation);
        nodeEndCombo.setItems(filteredNodes);
        nodeEndCombo.setFilterFunction(s -> n -> nodeToLocation.toString(n).toLowerCase().contains(s.toLowerCase()));
        nodeEndCombo.setConverter(nodeToLocation);
    }

    @FXML
    private void editNodes() {
        if (App.getSingleton().getAccountType() != AccountType.ADMIN) return;
        setEditingNodes(true);
        clearCanvas();
        var nodePoints = nodes.values().stream().map(n -> {
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return new Pair<Long, javafx.scene.Node>(null, null);
            var point = pointOpt.get();
            point.setOnMouseClicked(e -> {
                if (e.isSecondaryButtonDown()) {

                }
            });
            DragController dragController = new DragController(point, true);
            dragController.setScaleSupplier(gesturePane::getCurrentScale);
            dragController.setOnMove(node -> {
                n.setXcoord((int) node.getLayoutX());
                n.setYcoord((int) node.getLayoutY());
                editedNodes.add(n);
            });
            dragController.setOnEnd(node -> gesturePane.setGestureEnabled(true));
            dragController.setOnStart(node -> gesturePane.setGestureEnabled(false));
            return new Pair<Long, javafx.scene.Node>(n.getNodeID(), point);
        }).collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));
        edges.values().forEach(e -> {
            var startNode = nodes.get(e.getStartNode());
            var endNode = nodes.get(e.getEndNode());
            if (startNode == null || endNode == null) return;
            drawEdge(startNode, endNode, nodePoints.get(startNode.getNodeID()), nodePoints.get(endNode.getNodeID()));
        });
    }

    @FXML
    private void commitChanges() {
        if (commiting.compareAndExchange(false, true)) return;
        var totalEdited = editedNodes.size();
        commitButton.setDisable(true);
        doneEditingButton.setDisable(true);
        commitProgress.setProgress(0);
        AtomicInteger counter = new AtomicInteger(0);
        var commitThread = new Thread(() -> {
            editedNodes.parallelStream().forEach(n -> {
                new NodeDaoImpl().update(n, new Node.Field[]{Node.Field.XCOORD, Node.Field.YCOORD});
                Platform.runLater(() -> commitProgress.setProgress((double) counter.incrementAndGet() / totalEdited));
            });
            editedNodes.clear();
            Platform.runLater(() -> {
                commitButton.setDisable(false);
                doneEditingButton.setDisable(false);
            });
            commiting.set(false);
        });
        commitThread.setDaemon(true);
        commitThread.start();
    }

    @FXML
    private void doneEditingNodes() {
        setEditingNodes(false);
    }

    private void setEditingNodes(boolean editing) {
        // if you are editing the map nodes
        pathfinding.setDisable(editing);
        doneEditingButton.setVisible(editing);
        doneEditingButton.setManaged(editing);
        commitButton.setManaged(editing);
        commitButton.setVisible(editing);
        commitProgress.setManaged(editing);
        commitProgress.setVisible(editing);

        // if you are done editing
        if (!editing) clearCanvas();
        editButton.setVisible(!editing);
        editButton.setManaged(!editing);
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
        floor.nodeCanvas.getChildren().add(imgView);
    }

    private void drawLine(Floor floor, List<Node> nodes) {
        if (nodes.size() < 2 || floor == null) return;
        var polyline = new Polyline();
        polyline.setStroke(Color.valueOf("#FF000099"));
        polyline.setStrokeWidth(10);
        polyline.getPoints().addAll(nodes.stream().flatMap(n -> Stream.of(n.getXcoord(), n.getYcoord())).map(Double::valueOf).toList());
        floor.lineCanvas.getChildren().add(0, polyline);
    }

    private Optional<Circle> drawNode(Node node, String color) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return Optional.empty();
        var circle = new Circle(0, 0, 15);
        circle.setLayoutX(node.getXcoord());
        circle.setLayoutY(node.getYcoord());
        circle.setFill(Color.valueOf(color));
        circle.setRadius(10);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.valueOf("#000000"));
        var toolTip = new VBox();
        var text = new Label(nodeToLocation.toString(node) + "\nNode ID: " + node.getNodeID().toString());
        // tool tip text styling
        text.setTextFill(Color.valueOf("#ffffff"));
        text.setTextAlignment(TextAlignment.CENTER);
        text.layout();
        text.applyCss();
        //tool tip styling
        toolTip.getChildren().add(text);
        toolTip.setPadding(new Insets(5));
        toolTip.setAlignment(Pos.CENTER);
        toolTip.setStyle("-fx-background-color: #000000aa; -fx-background-radius: 5");
        toolTip.layoutXProperty().bind(circle.layoutXProperty());
        toolTip.layoutYProperty().bind(circle.layoutYProperty());
        toolTip.layout();
        toolTip.boundsInParentProperty().addListener((v, o, n) -> {
            toolTip.setTranslateY(-(n.getHeight() + 20));
            toolTip.setTranslateX(-n.getWidth() / 2);
        });

        circle.setOnMouseEntered(e -> toolTip.setVisible(true));
        circle.setOnMouseExited(e -> toolTip.setVisible(false));
        toolTip.setVisible(false);

        floor.nodeCanvas.getChildren().add(circle);
        floor.tooltipCanvas.getChildren().add(toolTip);
        return Optional.of(circle);
    }

    private Optional<Line> drawEdge(Node startNode, Node endNode, javafx.scene.Node startSceneNode, javafx.scene.Node endSceneNode) {
        if (startNode == null || endNode == null || startSceneNode == null || endSceneNode == null)
            return Optional.empty();
        var startFloor = floors.get(startNode.getFloor());
        var endFloor = floors.get(endNode.getFloor());
        if (startFloor == null || startFloor != endFloor) return Optional.empty();
        var line = new Line();
        line.startXProperty().bind(startSceneNode.layoutXProperty());
        line.startYProperty().bind(startSceneNode.layoutYProperty());
        line.endXProperty().bind(endSceneNode.layoutXProperty());
        line.endYProperty().bind(endSceneNode.layoutYProperty());
        line.setFill(Color.valueOf("#000000"));
        line.setStrokeWidth(3);
        startFloor.lineCanvas.getChildren().add(0, line);
        return Optional.of(line);
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
        floor.nodeCanvas.getChildren().add(group);
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
                floor.clearFloor();
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
            drawNode(path.get(path.size() - 1), "#3cb043");
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
        Group lineCanvas = new Group();
        Group nodeCanvas = new Group();
        Group tooltipCanvas = new Group();
        ImageView imageView = new ImageView();

        private Image imageCache;

        private void init() {
            root.getChildren().addAll(imageView, lineCanvas, nodeCanvas, tooltipCanvas);
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
            lineCanvas.getChildren().clear();
            tooltipCanvas.getChildren().clear();
            nodeCanvas.getChildren().clear();
        }
    }
}
