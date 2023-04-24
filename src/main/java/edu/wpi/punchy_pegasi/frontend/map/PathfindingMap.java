package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.PathfindingSingleton;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils.isDestination;

public class PathfindingMap {
    private final Map<String, HospitalFloor> floors = new LinkedHashMap<>() {{
        put("L2", new HospitalFloor("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"));
        put("L1", new HospitalFloor("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"));
//        put("00", new HospitalFloor("frontend/assets/map/00_thegroundfloor.png", "Ground Layer", "00"));
        put("1", new HospitalFloor("frontend/assets/map/01_thefirstfloor.png", "First Layer", "1"));
        put("2", new HospitalFloor("frontend/assets/map/02_thesecondfloor.png", "Second Layer", "2"));
        put("3", new HospitalFloor("frontend/assets/map/03_thethirdfloor.png", "Third Layer", "3"));
    }};
    private final AtomicBoolean startSelected = new AtomicBoolean(false);
    private final AtomicBoolean endSelected = new AtomicBoolean(false);
    private final AtomicBoolean selectingGraphically = new AtomicBoolean(false);
    private final StringConverter<LocationName> locationToString = new StringConverter<>() {
        @Override
        public String toString(LocationName locationName) {
            return locationName == null ? "" : locationName.getLongName();
        }

        @Override
        public LocationName fromString(String string) {
            return null;
        }
    };
    @FXML
    private PFXButton selectGraphicallyCancel;
    @FXML
    private PFXButton selectGraphically;
    @FXML
    private MFXFilterComboBox<String> selectAlgo;
    @FXML
    private BorderPane root;
    private IMap<HospitalFloor> map;
    @FXML
    private VBox pathfinding;
    @FXML
    private MFXFilterComboBox<LocationName> nodeEndCombo;
    @FXML
    private MFXFilterComboBox<LocationName> nodeStartCombo;
    @FXML
    private PFXButton pathfindButton;
    @FXML
    private Text pathfindStatus;
    private ObservableMap<Long, Node> nodes;
    private ObservableMap<Long, Edge> edges;
    private ObservableMap<Long, LocationName> locations;
    private ObservableMap<Long, Move> moves;
    private ObservableList<Node> nodesList;
    private ObservableList<Edge> edgesList;
    private ObservableList<LocationName> locationsList;
    private ObservableList<Move> movesList;
    private ObservableMap<Node, ObservableList<LocationName>> nodeToLocation;
    private ObservableMap<LocationName, Node> locationToNode;
    private String selectedAlgo;

    private LocalDate movesDate = LocalDate.now();

    private Optional<Circle> drawNode(Node node, String color) {
        var location = nodeToLocation.get(node);
        if(location.isEmpty()) return Optional.empty();
        return map.drawNode(node, color, location.get(0).getShortName(), String.join("\n", location.stream().map(LocationName::getLongName).toArray(String[]::new)));
    }
    @FXML
    private void initialize() {
        map = new HospitalMap(floors);
        root.setCenter(map.get());
        map.addLayer(pathfinding);
        pathfinding.setPickOnBounds(false);
        load(() -> {
            var filteredSorted = locationsList.filtered(isDestination).sorted(Comparator.comparing(LocationName::getLongName));
            nodeStartCombo.setItems(filteredSorted);
            nodeStartCombo.setFilterFunction(s -> n -> locationToString.toString(n).toLowerCase().contains(s.toLowerCase()));
            nodeStartCombo.setConverter(locationToString);
            nodeStartCombo.pressedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (newPropertyValue && !selectingGraphically.get()) {
                    startSelected.set(true);
                    endSelected.set(false);
                    Platform.runLater(() -> {
                        selectGraphically.setText("Select Start Graphically");
                        selectGraphically.setDisable(false);
                    });
                }
            });
            nodeEndCombo.setItems(filteredSorted);
            nodeEndCombo.setFilterFunction(s -> n -> locationToString.toString(n).toLowerCase().contains(s.toLowerCase()));
            nodeEndCombo.setConverter(locationToString);
            nodeEndCombo.pressedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (newPropertyValue) {
                    startSelected.set(false);
                    endSelected.set(true);
                    Platform.runLater(() -> {
                        selectGraphically.setText("Select End Graphically");
                        selectGraphically.setDisable(false);
                    });
                }
            });
        });
        selectAlgo.setItems(FXCollections.observableArrayList("AStar", "Depth-First Search", "Breadth-First Search", "Dijkstra"));
        selectGraphically.setDisable(true);
        selectGraphicallyCancel.setVisible(false);
        selectGraphicallyCancel.setManaged(false);
        PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getAStar());
    }

//    private Optional<Node> locationToNode(LocationName locationName) {
//        if (node == null) return Optional.empty();
//        var moves = this.moves.values().stream().filter(m -> m.getNodeID().equals(node.getNodeID())).toList();
//        if (moves.isEmpty()) return Optional.empty();
//        return Optional.ofNullable(locations.get(moves.get(0).getLocationID()));
//    }

    @FXML
    private void graphicalSelect() {
        if (!selectingGraphically.compareAndSet(false, true)) return;
        nodeStartCombo.setDisable(true);
        nodeEndCombo.setDisable(true);
        pathfindButton.setDisable(true);
        selectGraphicallyCancel.setVisible(true);
        selectGraphicallyCancel.setManaged(true);
        selectGraphicallyCancel.setOnAction(e -> {
            selectGraphicallyCancel.setOnAction(null);
            nodeStartCombo.setDisable(false);
            nodeEndCombo.setDisable(false);
            pathfindButton.setDisable(false);
            selectGraphicallyCancel.setVisible(false);
            selectGraphicallyCancel.setManaged(false);
            selectingGraphically.set(false);
            map.clearMap();
        });
        nodesList.forEach(n -> {
            var location = nodeToLocation.get(n);
            if(location == null || location.isEmpty()) return;
            if (!isDestination.test(location.get(0))) return;
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return;
            var point = pointOpt.get();
            point.setOnMouseClicked(e -> {
                if (startSelected.get())
                    nodeStartCombo.selectItem(location.get(0));
                else if (endSelected.get())
                    nodeEndCombo.selectItem(location.get(0));
                selectGraphicallyCancel.fire();
            });
        });
    }

    private void load(Runnable callback) {
        var thread = new Thread(() -> {
            nodes = App.getSingleton().getFacade().getAllNode();
            edges = App.getSingleton().getFacade().getAllEdge();
            moves = App.getSingleton().getFacade().getAllMove();
            locations = App.getSingleton().getFacade().getAllLocationName();
            nodesList = App.getSingleton().getFacade().getAllAsListNode();
            edgesList = App.getSingleton().getFacade().getAllAsListEdge();
            movesList = App.getSingleton().getFacade().getAllAsListMove();
            locationsList = App.getSingleton().getFacade().getAllAsListLocationName();
            // TODO: Rerun when moves Date changes
            nodeToLocation = FacadeUtils.getNodeLocations(nodes, locations, moves, movesDate);
            locationToNode = FacadeUtils.getLocationNode(nodes, locations, moves, movesDate);
            Platform.runLater(callback);
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void pathFindWithSelectedNodes() {
        var startLocation = nodeStartCombo.getSelectedItem();
        var endLocation = nodeEndCombo.getSelectedItem();
        if (startLocation == null || endLocation == null) return;
        var startNode = locationToNode.get(startLocation);
        var endNode = locationToNode.get(endLocation);
        if (startNode == null || endNode == null) return;
        pathfindStatus.setText(pathFind(startNode, endNode));
    }

    private String pathFind(Node start, Node end) {
        var edgeList = edges.values().stream().map(v -> new Pair<>(v.getStartNode(), v.getEndNode())).toList();
        var graph = new Graph<>(nodes, edgeList);

        try {
            var path = PathfindingSingleton.SINGLETON.getAlgorithm().findPath(graph, start, end);
            for (var floor : floors.values())
                floor.clearFloor();
            String currentFloor = path.get(0).getFloor();
            List<Node> currentPath = new ArrayList<>();
            for (var node : path) {
                if (!node.getFloor().equals(currentFloor)) {
                    map.drawLine(currentPath);
                    var endNode = currentPath.get(currentPath.size() - 1);
                    map.drawNode(node, "red", "", "From Here");
                    //map.drawArrow(node, endNode.getFloorNum() > node.getFloorNum()).setOnMouseClicked(e -> Platform.runLater(() -> map.showLayer(floors.get(endNode.getFloor()))));
                    map.drawArrow(endNode, endNode.getFloorNum() < node.getFloorNum()).setOnMouseClicked(e -> Platform.runLater(() -> map.showLayer(floors.get(node.getFloor()))));
                    currentPath = new ArrayList<>();
                    currentFloor = node.getFloor();
                }
                currentPath.add(node);
            }
            map.drawLine(currentPath);
            map.drawYouAreHere(path.get(0));
            drawNode(path.get(path.size() - 1), "#3cb043");
            map.focusOn(path.get(0));
            return "";
        } catch (IllegalStateException e) {
            return "Path not found";
        }
    }

    @FXML
    private void setAlgo() {
        selectedAlgo = selectAlgo.getSelectedItem();
        if (selectedAlgo.equals("Depth-First Search")) {
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getDFS());
        } else if (selectedAlgo.equals("Breadth-First Search")) {
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getBFS());
        } else if (selectedAlgo.equals("Dijkstra")) {
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getDijkstra());
        } else {
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getAStar());
        }
    }
}
