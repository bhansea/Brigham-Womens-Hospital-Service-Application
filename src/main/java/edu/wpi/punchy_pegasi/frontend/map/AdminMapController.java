package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.DragController;
import edu.wpi.punchy_pegasi.generated.NodeDaoImpl;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AdminMapController {
    private final Map<String, HospitalFloor> floors = new LinkedHashMap<>() {{
        put("L1", new HospitalFloor("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"));
        put("L2", new HospitalFloor("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"));
//        put("00", new HospitalFloor("frontend/assets/map/00_thegroundfloor.png", "Ground Layer", "00"));
        put("1", new HospitalFloor("frontend/assets/map/01_thefirstfloor.png", "First Layer", "1"));
        put("2", new HospitalFloor("frontend/assets/map/02_thesecondfloor.png", "Second Layer", "2"));
        put("3", new HospitalFloor("frontend/assets/map/03_thethirdfloor.png", "Third Layer", "3"));
    }};
    private final AtomicBoolean commiting = new AtomicBoolean();
    private final Set<Node> editedNodes = ConcurrentHashMap.newKeySet();
    private IMap<HospitalFloor> map;
    @FXML
    private BorderPane root;
    @FXML
    private MFXProgressBar commitProgress;
    @FXML
    private MFXButton commitButton;
    @FXML
    private MFXButton doneEditingButton;
    @FXML
    private MFXButton editButton;
    @FXML
    private VBox editing;
    private Map<Long, Node> nodes;
    private Map<Long, Edge> edges;
    private Map<Long, LocationName> locations;
    private Map<Long, Move> moves;
    private Map<Long, Move> movesByNodeID = new HashMap<>();
    private Map<String, LocationName> locationsByLongName = new HashMap<>();

    private final StringConverter<Node> nodeToLocation = new StringConverter<>() {
        @Override
        public String toString(Node node) {
            var location = nodeToLocation(node);
            if (location.isEmpty()) return "";
            return location.get().getLongName();
        }

        @Override
        public Node fromString(String string) {
            return nodes.get(moves.values().stream().filter(m -> Objects.equals(m.getLongName(), string)).findFirst().get().getNodeID());// nodesList.stream().filter(n -> n.getNodeID().toString().equals(string)).findFirst().orElse(null);
        }
    };

    private Optional<Circle> drawNode(Node node, String color) {
        var location = nodeToLocation(node).orElseGet(() -> new LocationName(null, "", "", LocationName.NodeType.HALL));
        var locationType = location.getNodeType();
        var shortLabel = false; //locationType != LocationName.NodeType.HALL && locationType != LocationName.NodeType.STAI && locationType != LocationName.NodeType.ELEV;
        return map.drawNode(node, color, shortLabel ? location.getShortName() : "", location.getLongName() + "\nNode ID: " + node.getNodeID().toString());
    }

    @FXML
    private void initialize() {
        map = new HospitalMap(floors);
        root.setCenter(map.get());
        map.addLayer(editing);
        editing.setPickOnBounds(false);
        load(this::editNodes);
    }

    private Optional<LocationName> nodeToLocation(Node node) {
        if (node == null) return Optional.empty();
        var move = movesByNodeID.get(node.getNodeID());
        if (move == null) return Optional.empty();
        return Optional.ofNullable(locationsByLongName.get(move.getLongName()));
    }

    private void load(Runnable callback) {
        var thread = new Thread(() -> {
            nodes = App.getSingleton().getFacade().getAllNode();
            edges = App.getSingleton().getFacade().getAllEdge();
            moves = App.getSingleton().getFacade().getAllMove();
            locations = App.getSingleton().getFacade().getAllLocationName();

            movesByNodeID = moves.values().stream().collect(Collectors.toMap(Move::getNodeID, v -> v));
            locationsByLongName = locations.values().stream().collect(Collectors.toMap(LocationName::getLongName, v -> v));

            Platform.runLater(callback);
        });
        thread.setDaemon(true);
        thread.start();
    }

    private void editNodes() {
        map.clearMap();
        var nodePoints = nodes.values().stream().map(n -> {
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return new Pair<Long, javafx.scene.Node>(null, null);
            var point = pointOpt.get();
            point.setOnMouseClicked(e -> {
                if (e.isSecondaryButtonDown()) {
                    var popOver = new PopOver();
                    popOver.getRoot().getStylesheets().add(App.getSingleton().resolveResource("frontend/components/DefaultTheme.css").get().toExternalForm());
                    popOver.getRoot().getChildren().add(new Label());
                }
            });
            DragController dragController = new DragController(point, true);
            dragController.setScaleSupplier(() -> map.getZoom());
            dragController.setOnMove(node -> {
                n.setXcoord((int) node.getLayoutX());
                n.setYcoord((int) node.getLayoutY());
                editedNodes.add(n);
            });
            dragController.setOnEnd(node -> map.enableMove(true));
            dragController.setOnStart(node -> map.enableMove(false));
            return new Pair<Long, javafx.scene.Node>(n.getNodeID(), point);
        }).collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));
        edges.values().forEach(e -> {
            var startNode = nodes.get(e.getStartNode());
            var endNode = nodes.get(e.getEndNode());
            if (startNode == null || endNode == null) return;
            map.drawEdge(startNode, endNode, nodePoints.get(startNode.getNodeID()), nodePoints.get(endNode.getNodeID()));
        });
    }

    @FXML
    private void commitChanges() {
        if (commiting.compareAndExchange(false, true)) return;
        var totalEdited = editedNodes.size();
        commitButton.setDisable(true);
        commitProgress.setProgress(0);
        AtomicInteger counter = new AtomicInteger(0);
        var commitThread = new Thread(() -> {
            editedNodes.parallelStream().forEach(n -> {
                new NodeDaoImpl().update(n, new Node.Field[]{Node.Field.XCOORD, Node.Field.YCOORD});
                Platform.runLater(() -> commitProgress.setProgress((double) counter.incrementAndGet() / totalEdited));
            });
            editedNodes.clear();
            Platform.runLater(() -> commitButton.setDisable(false));
            commiting.set(false);
        });
        commitThread.setDaemon(true);
        commitThread.start();
    }
}
