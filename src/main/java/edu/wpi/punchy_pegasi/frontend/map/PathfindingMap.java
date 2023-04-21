package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.*;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.LocationNameDaoImpl;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import com.fazecast.jSerialComm.*;

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
    @FXML
    private PFXButton selectGraphicallyCancel;
    @FXML
    private PFXButton selectGraphically;
    @FXML
    private MFXFilterComboBox<String> selectAlgo;
    @FXML
    private PFXButton robotButton;
    @FXML
    private BorderPane root;
    private IMap<HospitalFloor> map;
    @FXML
    private VBox pathfinding;
    @FXML
    private MFXFilterComboBox<Node> nodeEndCombo;
    @FXML
    private MFXFilterComboBox<Node> nodeStartCombo;
    @FXML
    private PFXButton pathfindButton;
    @FXML
    private Text pathfindStatus;
    private Map<Long, Node> nodes;
    private Map<Long, Edge> edges;
    private Map<Long, LocationName> locations;
    private Map<Long, Move> moves;
    private Map<Long, Move> movesByNodeID = new HashMap<>();
    private ArrayList<Integer> xCoords = new ArrayList<Integer>();
    private ArrayList<Integer> yCoords = new ArrayList<Integer>();
    private Map<String, LocationName> locationsByLongName = new HashMap<>();
    private String selectedAlgo;
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
    private ObservableList<Node> filteredNodes;

    private Optional<Circle> drawNode(Node node, String color) {
        var location = nodeToLocation(node).orElseGet(() -> new LocationName(null, "", "", null));
        return map.drawNode(node, color, location.getShortName(), location.getLongName());
    }

    @FXML
    private void initialize() {
        map = new HospitalMap(floors);
        root.setCenter(map.get());
        map.addLayer(pathfinding);
        pathfinding.setPickOnBounds(false);
        load(() -> {
            nodeStartCombo.setItems(filteredNodes);
            nodeStartCombo.setFilterFunction(s -> n -> nodeToLocation.toString(n).toLowerCase().contains(s.toLowerCase()));
            nodeStartCombo.setConverter(nodeToLocation);
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
            nodeEndCombo.setItems(filteredNodes);
            nodeEndCombo.setFilterFunction(s -> n -> nodeToLocation.toString(n).toLowerCase().contains(s.toLowerCase()));
            nodeEndCombo.setConverter(nodeToLocation);
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
        selectAlgo.setItems(FXCollections.observableArrayList("AStar", "Depth-First Search", "Breadth-First Search"));
        selectGraphically.setDisable(true);
        robotButton.setDisable(true);
        selectGraphicallyCancel.setVisible(false);
        selectGraphicallyCancel.setManaged(false);
        PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getAStar());
    }

    private Optional<LocationName> nodeToLocation(Node node) {
        if (node == null) return Optional.empty();
        var move = movesByNodeID.get(node.getNodeID()).getLongName();
        if (move == null) return Optional.empty();
        return Optional.ofNullable(locationsByLongName.get(move));
    }

    @FXML
    private void graphicalSelect() {
        if (selectingGraphically.compareAndExchange(false, true)) return;
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
        filteredNodes.forEach(n -> {
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return;
            var point = pointOpt.get();
            point.setOnMouseClicked(e -> {
                if (startSelected.get())
                    nodeStartCombo.selectItem(n);
                else if (endSelected.get())
                    nodeEndCombo.selectItem(n);
                selectGraphicallyCancel.fire();
            });
        });
    }

    private void load(Runnable callback) {
        var thread = new Thread(() -> {
            nodes = App.getSingleton().getFacade().getAllNode();
            edges = App.getSingleton().getFacade().getAllEdge();
            moves = App.getSingleton().getFacade().getAllMove();
            locations = new LocationNameDaoImpl().getAll();
            movesByNodeID = moves.values().stream().collect(Collectors.toMap(Move::getNodeID, v -> v));
            locationsByLongName = locations.values().stream().collect(Collectors.toMap(LocationName::getLongName, v -> v));
            filteredNodes = FXCollections.observableArrayList(nodes.values().stream().filter(v -> {
                var move = movesByNodeID.get(v.getNodeID());
                if (move == null) return false;
                var location = locationsByLongName.get(move.getLongName());
                if (location == null) return false;
                var locationType = location.getNodeType();
                return locationType != LocationName.NodeType.HALL && locationType != LocationName.NodeType.STAI && locationType != LocationName.NodeType.ELEV;
            }).sorted(Comparator.comparing(Node::getNodeID)).toList());
            Platform.runLater(callback);
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void pathFindWithSelectedNodes() {
        var startNode = (Node) nodeStartCombo.getSelectedItem();
        var endNode = (Node) nodeEndCombo.getSelectedItem();
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
                    map.drawNode(node,"red", "","From Here");
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

            for (Node node : path) {
                xCoords.add((Integer)node.getFromField(Node.Field.XCOORD));
                yCoords.add((Integer)node.getFromField(Node.Field.YCOORD));
            }
            robotButton.setDisable(false);
            return "";
        } catch (IllegalStateException e) {
            return "Path not found";
        }
    }

    @FXML
    private void sendRobotMessage() {
        robotButton.setDisable(true);

        SerialPort comPort = null;
        SerialPort[] ports = SerialPort.getCommPorts();

        for(int i=0;i<ports.length;i++) {
            if(ports[i].getPortDescription().equals("Pololu A-Star 32U4")) {
                comPort = ports[i];
            }
        }
        try {
            comPort.openPort();
        } catch(Exception e) {
            System.out.println("No port established!");
            return;
        }
        double multiplier = 0.05;
        byte[] message = generateMessage("S", xCoords.get(0), yCoords.get(0));
        System.out.println(xCoords.get(0) + ", " + yCoords.get(0));
        comPort.writeBytes(message, message.length);

        for(int i=1;i<xCoords.size() - 1;i++) {
            message = generateMessage("M", xCoords.get(i), yCoords.get(i));
            System.out.println(xCoords.get(i) + ", " + yCoords.get(i));
            comPort.writeBytes(message, message.length);
        }

        message = generateMessage("E", xCoords.get(xCoords.size() - 1), yCoords.get(yCoords.size() - 1));
        System.out.println(xCoords.get(xCoords.size() - 1) + ", " + yCoords.get(yCoords.size() - 1));
        comPort.writeBytes(message, message.length);
        comPort.closePort();
    }

    public static byte[] generateMessage(String str, Integer startPos, Integer endPos) {
        byte[] strArray = str.getBytes();
        byte[] tempStartArray = Integer.toString(startPos).getBytes();
        byte[] tempEndArray = Integer.toString(endPos).getBytes();
        byte[] startIntArray = {(byte)'0', (byte)'0', (byte)'0', (byte)'0'};
        byte[] endIntArray = {(byte)'0', (byte)'0', (byte)'0', (byte)'0'};

        for(int i=startIntArray.length - 1, j = tempStartArray.length - 1;j>=0;i--, j--) {
            startIntArray[i] = tempStartArray[j];
        }

        for(int i=endIntArray.length - 1, j = tempEndArray.length - 1;j>=0;i--, j--) {
            endIntArray[i] = tempEndArray[j];
        }


        byte[] result = new byte[strArray.length + startIntArray.length + endIntArray.length + 1];

        int pos = 0;
        for (byte element : strArray) {
            result[pos] = element;
            pos++;
        }
        for (byte element : startIntArray) {
            result[pos] = element;
            pos++;
        }
        for (byte element : endIntArray) {
            result[pos] = element;
            pos++;
        }
        result[result.length - 1] = '\n';

        return result;
    }
    @FXML
    private void setAlgo() {
        selectedAlgo = selectAlgo.getSelectedItem();
        if(selectedAlgo.equals("Depth-First Search")){
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getDFS());
        } else if(selectedAlgo.equals("Breadth-First Search")){
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getBFS());
        } else {
            PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getAStar());
        }
    }
}
