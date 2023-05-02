package edu.wpi.punchy_pegasi.frontend.map;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.javafx.geom.Vec3d;
import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.pathfinding.Graph;
import edu.wpi.punchy_pegasi.backend.pathfinding.PathfindingSingleton;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.simple.SimpleMatrix;
import org.javatuples.Pair;

import javax.management.ValueExp;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils.isDestination;

public class PathfindingMap {
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
    private final ArrayList<Integer> xCoords = new ArrayList<Integer>();
    private final ArrayList<Integer> yCoords = new ArrayList<Integer>();
    private final HBox container = new HBox();
    private final LocalDate movesDate = LocalDate.now();
    @FXML
    private MFXDatePicker adminDatePicker;
    @FXML
    private PFXButton selectGraphicallyCancel;
    @FXML
    private Label invalidText;
    @FXML
    private PFXButton selectGraphically;
    @FXML
    private MFXFilterComboBox<String> selectAlgo;
    @FXML
    private PFXButton robotButton;
    @FXML
    private BorderPane root;
    private IMap<HospitalFloor.Floors> map;
    @FXML
    private VBox pathfinding;
    @FXML
    private VBox robotInfo;
    @FXML
    private VBox pathDirections;
    @FXML
    private MFXFilterComboBox<LocationName> nodeEndCombo;
    @FXML
    private MFXFilterComboBox<LocationName> nodeStartCombo;
    @FXML
    private PFXButton pathfindButton;
    @FXML
    private Text pathfindStatus;
    private ObservableMap<Long, Node> nodes;
    private ObservableMap<UUID, Edge> edges;
    private ObservableMap<Long, LocationName> locations;
    private ObservableMap<Long, Move> moves;
    private ObservableList<Node> nodesList;
    private ObservableList<Edge> edgesList;
    private ObservableList<LocationName> locationsList;
    private ObservableList<Move> movesList;
    private ObservableMap<Node, ObservableList<Move>> nodeToMoves;
    private ObservableMap<LocationName, Node> locationToNode;
    private String selectedAlgo;
    @FXML
    private Label batteryPercent;
    private LinkedHashMap<String, List<DirectionalNode>> directionMap = new LinkedHashMap<>();


    public static byte[] generateMessage(String str, Integer startPos, Integer endPos) {
        byte[] strArray = str.getBytes();
        byte[] tempStartArray = Integer.toString(startPos).getBytes();
        byte[] tempEndArray = Integer.toString(endPos).getBytes();
        byte[] startIntArray = {(byte) '0', (byte) '0', (byte) '0', (byte) '0'};
        byte[] endIntArray = {(byte) '0', (byte) '0', (byte) '0', (byte) '0'};

        for (int i = startIntArray.length - 1, j = tempStartArray.length - 1; j >= 0; i--, j--) {
            startIntArray[i] = tempStartArray[j];
        }

        for (int i = endIntArray.length - 1, j = tempEndArray.length - 1; j >= 0; i--, j--) {
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

    private Optional<Circle> drawNode(Node node, String color) {
        var moves = nodeToMoves.get(node);
        if (moves.isEmpty()) return Optional.empty();
        var labelBinding = Bindings.createStringBinding(() -> locations.get(moves.get(0).getLocationID()).getShortName(), moves);
        var hoverBinding = Bindings.createStringBinding(() -> String.join("\n", moves.stream().map(e->locations.get(e.getLocationID())).filter(Objects::nonNull).map(LocationName::getLongName).toArray(String[]::new)), moves);
        return map.addNode(node, color, labelBinding, hoverBinding);
    }

    @FXML
    private void initialize() {
        map = new HospitalMap();
        root.setCenter(map.get());
        map.addLayer(container);
        container.getChildren().addAll(pathfinding, robotInfo, pathDirections);
        HBox.setHgrow(pathfinding, Priority.ALWAYS);
        invalidText.setVisible(false);
        robotInfo.setVisible(false);
        container.setPickOnBounds(false);
        pathfinding.setPickOnBounds(false);
        robotInfo.setPickOnBounds(false);
        var date = adminDatePicker.getValue();
        adminDatePicker.setValue(LocalDate.now());
        // Check account status for admin features
        if (App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel()) {
            robotInfo.setVisible(true);
        }

        selectAlgo.setItems(FXCollections.observableArrayList("AStar", "Depth-First Search", "Breadth-First Search", "Dijkstra"));
        selectGraphically.setDisable(true);
        pathfindStatus.managedProperty().bind(Bindings.createBooleanBinding(() -> !pathfindStatus.textProperty().get().isBlank(), pathfindStatus.textProperty()));
        pathfindStatus.visibleProperty().bind(Bindings.createBooleanBinding(() -> !pathfindStatus.textProperty().get().isBlank(), pathfindStatus.textProperty()));
        robotButton.setDisable(true);
        selectGraphicallyCancel.setVisible(false);
        selectGraphicallyCancel.setManaged(false);

        PathfindingSingleton.SINGLETON.setAlgorithm(PathfindingSingleton.SINGLETON.getAStar());
        load(() -> {
            // set zoom to average of all nodes
            var minX = nodesList.stream().mapToDouble(Node::getXcoord).min().orElse(0);
//            var averageX = nodesList.stream().mapToDouble(Node::getXcoord).sum() / nodesList.size();
            var maxX = nodesList.stream().mapToDouble(Node::getXcoord).max().orElse(0);
            var minY = nodesList.stream().mapToDouble(Node::getYcoord).min().orElse(0);
//            var averageY = nodesList.stream().mapToDouble(Node::getYcoord).sum() / nodesList.size();
            var maxY = nodesList.stream().mapToDouble(Node::getYcoord).max().orElse(0);
//            map.focusOn(new Node(0L, (int)averageX, (int)averageY, "1", ""));
//            map.setZoom(1);
            map.showRectangle(new Rectangle(minX, minY, maxX - minX, maxY - minY));

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
    }

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
            if (!nodeToMoves.containsKey(n)) return;
            var moves = nodeToMoves.get(n).stream().map(m -> locations.get(m.getLocationID())).filter(l->l != null && isDestination.test(l)).toList();
            if (moves.isEmpty()) return;
            var pointOpt = drawNode(n, "#FFFF00");
            if (pointOpt.isEmpty()) return;
            var point = pointOpt.get();
            point.setOnMouseClicked(e -> {
                if (startSelected.get()) nodeStartCombo.selectItem(moves.get(0));
                else if (endSelected.get()) nodeEndCombo.selectItem(moves.get(0));
                selectGraphicallyCancel.fire();
            });
        });
    }

    private void load(Runnable callback) {
        App.getSingleton().getExecutorService().execute(() -> {
            nodes = App.getSingleton().getFacade().getAllNode();
            edges = App.getSingleton().getFacade().getAllEdge();
            moves = App.getSingleton().getFacade().getAllMove();
            locations = App.getSingleton().getFacade().getAllLocationName();
            nodesList = App.getSingleton().getFacade().getAllAsListNode();
            edgesList = App.getSingleton().getFacade().getAllAsListEdge();
            movesList = App.getSingleton().getFacade().getAllAsListMove();
            locationsList = App.getSingleton().getFacade().getAllAsListLocationName();
            nodeToMoves = FacadeUtils.getNodeLocations(nodes, locations, moves, adminDatePicker.valueProperty());
            locationToNode = FacadeUtils.getLocationNode(nodes, locations, moves, adminDatePicker.valueProperty());
            Platform.runLater(callback);
        });
    }

    @FXML
    private void pathFindWithSelectedNodes() {
        var startLocation = nodeStartCombo.getSelectedItem();
        var endLocation = nodeEndCombo.getSelectedItem();
        if (startLocation == null || endLocation == null) return;
        var startNode = locationToNode.get(startLocation);
        var endNode = locationToNode.get(endLocation);
        if (startNode == null) {
            pathfindStatus.setText("The starting location is not currently available");
            return;
        }
        if (endNode == null) {
            pathfindStatus.setText("The ending location is not currently available");
            return;
        }
        pathfindStatus.setText(pathFind(startNode, endNode));
    }

    @RequiredArgsConstructor
    private enum PathDirectionType {
        START(MaterialSymbols.STEP_OUT),
        LEFT(MaterialSymbols.KEYBOARD_ARROW_LEFT),
        RIGHT(MaterialSymbols.KEYBOARD_ARROW_RIGHT),
        UP(MaterialSymbols.KEYBOARD_DOUBLE_ARROW_UP),
        DOWN(MaterialSymbols.SOUTH_WEST),
        END(MaterialSymbols.ADJUST);

        @Getter
        private final MaterialSymbols icon;
        public PFXIcon getPFXIcon(){
            return new PFXIcon(getIcon());
        }
    }

    @RequiredArgsConstructor
    @Data
    class DirectionalNode {
        private final LocationName locationName;
        private final PathDirectionType directionIcon;
    }

    private void pathDrawDirections() {
//        VBox directions = new VBox();
//        var allKeys = new ArrayList<>(directionMap.keySet());
//        Collections.reverse(allKeys);
        for (var floor : directionMap.keySet()) {
            var dNodeList = directionMap.get(floor);
            VBox directionsOnFloor = new VBox(new Label("Floor " + floor));
            for (var dNode : dNodeList) {
//                var directionEntry = new HBox();
                var directionIcon = dNode.getDirectionIcon().getPFXIcon();
                directionIcon.setSize(15.0);
                var directionText = new Label(dNode.getLocationName().getLongName());
//                directionEntry.getChildren().addAll(directionIcon ,directionText);
                directionsOnFloor.getChildren().add(new HBox(directionIcon, directionText));
            }
            pathDirections.getChildren().add(directionsOnFloor);
        }
    }

    private void pathPutNodes(PathDirectionType direction, Node node) {
        String hallwayName = "Follow the Hallway";
        var floor = node.getFloor();
        LocationName currLocation = locations.get(nodeToMoves.get(node).get(0).getLocationID());
        if (currLocation.getNodeType().equals(LocationName.NodeType.HALL)) {
            currLocation.setLongName(hallwayName);
            var hallwayDirection = direction;
            var dNode = new DirectionalNode(currLocation, hallwayDirection);
            if (directionMap.containsKey(floor)) {
                var locationsOnFloor = directionMap.get(floor);
                var prevLocation = locationsOnFloor.get(locationsOnFloor.size()-1);
                if (prevLocation.getLocationName().getLongName().equals(hallwayName) && prevLocation.getDirectionIcon().equals(direction)) {
                    // if the previous location is Hallway and no direction change, then don't add the hallway
                } else {
                    dNode.getLocationName().setLongName(hallwayName);
                    directionMap.get(floor).add(dNode);
                }
            } else {
                List<DirectionalNode> dNodes = new ArrayList<>() {{
                    add(dNode);
                }};
                directionMap.put(floor, dNodes);
            }
        } else {
            var dNode = new DirectionalNode(currLocation, direction);
            if (directionMap.containsKey(floor)) {
                directionMap.get(floor).add(dNode);
            } else {
                List<DirectionalNode> dNodes = new ArrayList<>() {{
                    add(dNode);
                }};
                directionMap.put(floor, dNodes);
            }
        }
    }

    private void clearDirections() {
        pathDirections.getChildren().clear();
        directionMap.clear();
    }

    private PathDirectionType calculateDirection (Node prevNode, Node currNode, Node nextNode) {
        if (currNode.equals(nextNode))
            return PathDirectionType.END;
        else if (prevNode.equals(currNode))
            return PathDirectionType.START;

//        Vec3d point1 = new Vec3d(prevNode.getXcoord().doubleValue(), prevNode.getYcoord().doubleValue(), 0.0);
//        Vec3d point2 = new Vec3d(currNode.getXcoord().doubleValue(), currNode.getYcoord().doubleValue(), 0.0);
//        Vec3d point3 = new Vec3d(nextNode.getXcoord().doubleValue(), nextNode.getYcoord().doubleValue(), 0.0);
//        var displacement1 = new Vec3d();
//        displacement1.sub(point1, point2);
//        var displacement2 = new Vec3d();
//        displacement2.sub(point2, point3);
//        var dot = displacement1.dot(displacement2);
//
//        var cross = new Vec3d();
//        cross.cross(displacement1, displacement2);
//        if(dot > -.1 && dot < .1) {
//            if (cross.z > 0)
//                return PathDirectionType.LEFT;
//            else
//                return PathDirectionType.RIGHT;
//        }
        var prevX = prevNode.getXcoord().doubleValue();
        var prevY = prevNode.getYcoord().doubleValue();
        var currX = currNode.getXcoord().doubleValue();
        var currY = currNode.getYcoord().doubleValue();
        var nextX = nextNode.getXcoord().doubleValue();
        var nextY = nextNode.getYcoord().doubleValue();
        DMatrixRMaj prev = new DMatrixRMaj(2, 1);
        prev.set(0, 0, prevX);
        prev.set(1, 0, prevY);

        DMatrixRMaj curr = new DMatrixRMaj(2, 1);
        curr.set(0, 0, currX);
        curr.set(1, 0, currY);

        DMatrixRMaj next = new DMatrixRMaj(2, 1);
        next.set(0, 0, nextX);
        next.set(1, 0, nextY);

        DMatrixRMaj vecPrevCurr = new DMatrixRMaj(2, 1);
        CommonOps_DDRM.subtract(curr, prev, vecPrevCurr);

        DMatrixRMaj vecCurrNext = new DMatrixRMaj(2, 1);
        CommonOps_DDRM.subtract(next, curr, vecCurrNext);
        double crossProduct = vecPrevCurr.data[0] * vecCurrNext.data[1] - vecPrevCurr.data[1] * vecCurrNext.data[0];

        if (crossProduct > 1000) {
            // Next point is on the left side of the line
            return PathDirectionType.LEFT;
        } else if (crossProduct < -1000) {
            // Next point is on the right side of the line
            return PathDirectionType.RIGHT;
        } else {
            // Three points are collinear
            return PathDirectionType.UP;
        }


    }

    private String pathFind(Node start, Node end) {
        var edgeList = edges.values().stream().map(v -> new Pair<>(v.getStartNode(), v.getEndNode())).toList();
        var graph = new Graph<>(nodes, edgeList);
        clearDirections();
        try {
            var path = PathfindingSingleton.SINGLETON.getAlgorithm().findPath(graph, start, end);
            map.clearMap();
            String currentFloor = path.get(0).getFloor();
            List<Node> currentPath = new ArrayList<>();
            var pathSize = path.size();
            var nodeIndex = 0;
            for (var node : path) {
                var prvNode = nodeIndex == 0 ? node : path.get(nodeIndex - 1);
                var nxtNode = nodeIndex == pathSize - 1 ? node : path.get(nodeIndex + 1);
                PathDirectionType nodeDirection = calculateDirection(prvNode, node, nxtNode);
                pathPutNodes(nodeDirection, node);

                if (!node.getFloor().equals(currentFloor)) {
                    map.drawLine(currentPath);
                    var endNode = currentPath.get(currentPath.size() - 1);
                    map.addNode(node, "red", Bindings.createStringBinding(() -> ""), Bindings.createStringBinding(() -> "From Here"));
                    //map.drawArrow(node, endNode.getFloorNum() > node.getFloorNum()).setOnMouseClicked(e -> Platform.runLater(() -> map.showLayer(floors.get(endNode.getFloor()))));
                    map.drawArrow(endNode, endNode.getFloorNum() < node.getFloorNum()).setOnMouseClicked(e -> Platform.runLater(() -> map.showLayer(HospitalFloor.floorMap.get(node.getFloor()))));
                    currentPath = new ArrayList<>();
                    currentFloor = node.getFloor();
                }
                currentPath.add(node);
                nodeIndex++;
            }
            pathDrawDirections();
            map.drawLine(currentPath);
            map.drawYouAreHere(path.get(0));
            drawNode(path.get(path.size() - 1), "#3cb043");
            map.focusOn(path.get(0));

            for (Node node : path) {
                xCoords.add(node.getXcoord());
                yCoords.add(node.getYcoord());
            }
            robotButton.setDisable(false);
            return "Path found successfully!";
        } catch (IllegalStateException e) {
            return "Path not found";
        }
    }

    @FXML
    private void sendRobotMessage() throws InterruptedException {
        SerialPort comPort = null;
        SerialPort[] ports = SerialPort.getCommPorts();

        for (int i = 0; i < ports.length; i++) {
            if (ports[i].getPortDescription().equals("Pololu A-Star 32U4")) {
                comPort = ports[i];
            }
        }
        try {
            comPort.openPort();
            robotButton.setDisable(true);
            invalidText.setVisible(false);
        } catch (Exception e) {
            invalidText.setVisible(true);
            return;
        }
        byte[] message = generateMessage("S", xCoords.get(0), yCoords.get(0));
        System.out.println(xCoords.get(0) + ", " + yCoords.get(0));
        comPort.writeBytes(message, message.length);

        for (int i = 1; i < xCoords.size() - 1; i++) {
            Thread.sleep(50);
            message = generateMessage("M", xCoords.get(i), yCoords.get(i));
            System.out.println(xCoords.get(i) + ", " + yCoords.get(i));
            comPort.writeBytes(message, message.length);
        }

        message = generateMessage("E", xCoords.get(xCoords.size() - 1), yCoords.get(yCoords.size() - 1));
        System.out.println(xCoords.get(xCoords.size() - 1) + ", " + yCoords.get(yCoords.size() - 1));
        comPort.writeBytes(message, message.length);

        // Receive Message for Battery
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

        byte[] firstReadBuffer = new byte[1];
        byte[] secondReadBuffer = new byte[1];
        for(int i=0;i<2;i++)
        {
            if(i == 0) comPort.readBytes(firstReadBuffer, firstReadBuffer.length);
            if(i == 1) comPort.readBytes(secondReadBuffer, secondReadBuffer.length);
        }

        byte[] result = new byte[2];
        result[0] = firstReadBuffer[0];
        result[1] = secondReadBuffer[0];

        batteryPercent.setText("Battery Percentage: " + new String(result, StandardCharsets.UTF_8) + "%");

        comPort.closePort();
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
