package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.DragController;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXProgressBar;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.StringConverter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.controlsfx.control.PopOver;
import org.javatuples.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class AdminMapController {
    private final Map<String, HospitalFloor> floors = new LinkedHashMap<>() {{
        put("L2", new HospitalFloor("frontend/assets/map/00_thelowerlevel2.png", "Lower Level 2", "L2"));
        put("L1", new HospitalFloor("frontend/assets/map/00_thelowerlevel1.png", "Lower Level 1", "L1"));
        put("1", new HospitalFloor("frontend/assets/map/01_thefirstfloor.png", "First Layer", "1"));
        put("2", new HospitalFloor("frontend/assets/map/02_thesecondfloor.png", "Second Layer", "2"));
        put("3", new HospitalFloor("frontend/assets/map/03_thethirdfloor.png", "Third Layer", "3"));
    }};
    private final Facade facade = App.getSingleton().getFacade();
    private final AtomicBoolean commiting = new AtomicBoolean();
    private final Predicate<MouseEvent> isLeftClick = e -> e.getButton() == MouseButton.PRIMARY && !e.isControlDown() && !e.isShiftDown();
    private final Predicate<MouseEvent> isLeftClickAndShift = e -> e.getButton() == MouseButton.PRIMARY && !e.isControlDown() && e.isShiftDown();
    private final Predicate<MouseEvent> isRightClick = e -> e.getButton() == MouseButton.SECONDARY || (e.getButton() == MouseButton.PRIMARY && e.isControlDown());
    private final ObservableList<MapEdit> mapEdits = FXCollections.observableArrayList(new MapEdit(MapEdit.ActionType.ADD_NODE, null));
    private final ObservableList<javafx.scene.Node> mapEditsNodes = FXCollections.observableArrayList(new javafx.scene.Group());
    private final Map<Long, Circle> nodePoints = new HashMap<>();
    private final MultiValuedMap<Long, Line> edgeLines = new ArrayListValuedHashMap<>();
    @FXML
    private VBox changes;
    private IMap<HospitalFloor> map;
    @FXML
    private BorderPane root;
    @FXML
    private MFXProgressBar commitProgress;
    @FXML
    private PFXButton commitButton;
    @FXML
    private PFXButton doneEditingButton;
    @FXML
    private PFXButton editButton;
    @FXML
    private VBox editing;
    private ObservableMap<Long, Node> nodes;
    private ObservableMap<Long, Edge> edges;
    private ObservableMap<Long, LocationName> locations;
    private ObservableMap<Long, Move> moves;
    private Node firstNode, secondNode;
    private ObservableMap<Node, ObservableList<LocationName>> nodeToLocation;

    private Optional<Circle> drawNode(Node node, String color) {
        var location = nodeToLocation.get(node);
        if (location == null) location = FXCollections.observableArrayList();
        return map.drawNode(node, color, "", String.join("\n", location.stream().map(LocationName::getLongName).toArray(String[]::new)) + "\nNode ID: " + node.getNodeID().toString());
    }

    private Optional<LocationName> nodeToLocation(Node node) {
        if (node == null) return Optional.empty();
        var moves = this.moves.values().stream().filter(m -> m.getNodeID().equals(node.getNodeID())).toList();
        if (moves.isEmpty()) return Optional.empty();
        return Optional.ofNullable(locations.get(moves.get(0).getLocationID()));
    }

    private void load(Runnable callback) {
        var thread = new Thread(() -> {
            nodes = facade.getAllNode();
            edges = facade.getAllEdge();
            moves = facade.getAllMove();
            locations = facade.getAllLocationName();

            // TODO: Update on date change
            nodeToLocation = FacadeUtils.getNodeLocations(nodes, locations, moves, LocalDate.now());
            Platform.runLater(callback);
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void initialize() {
        map = new HospitalMap(floors);
        root.setCenter(map.get());
        map.addLayer(editing);
        mapEdits.addListener((ListChangeListener<MapEdit>) c -> {
            Platform.runLater(() -> {
                mapEditsNodes.clear();
                mapEditsNodes.addAll(c.getList().stream().map(MapEdit::getGraphic).toList());
            });
        });
        mapEdits.remove(0);
        Bindings.bindContentBidirectional(mapEditsNodes, changes.getChildren());
        editing.setPickOnBounds(false);
        editing.getChildren().get(0).setPickOnBounds(true);
        load(this::editNodes);
    }

    private PopOver nodeEditMenu(Node node) {
        var popOver = new PopOver();
        popOver.getRoot().getStylesheets().add(App.getSingleton().resolveResource("frontend/css/DefaultTheme.css").get().toExternalForm());
        var editNode = new VBox();
        editNode.getStyleClass().add("node-popover");
        editNode.setAlignment(Pos.CENTER);
        editNode.setSpacing(10);
        editNode.setPadding(new Insets(10));

        var buildingDropdown = new MFXFilterComboBox<String>();
        var buildings = nodes.values().stream().map(Node::getBuilding).filter(Objects::nonNull).distinct().toList();
        buildingDropdown.getItems().addAll(buildings);
        buildingDropdown.setFloatingText("Pick Building");
        if (node.getBuilding() == null) {
            popOver.setOnCloseRequest(e -> {
                nodePoints.get(node.getNodeID()).setFill(Color.RED);
            });
        } else
            buildingDropdown.getSelectionModel().selectItem(node.getBuilding());
        buildingDropdown.setOnAction(e -> {
            node.setBuilding(buildingDropdown.getValue());
            nodePoints.get(node.getNodeID()).setFill(Color.YELLOW);
            popOver.setOnCloseRequest(null);
            mapEdits.stream().filter(edit -> edit.type == MapEdit.ActionType.EDIT_NODE && Objects.equals(((Node) edit.object).getNodeID(), node.getNodeID())).findFirst().ifPresent(mapEdits::remove);
            mapEdits.add(new MapEdit(MapEdit.ActionType.EDIT_NODE, node));
        });

        var locationDropdown = new MFXFilterComboBox<LocationName>();
        locationDropdown.setFloatingText("Pick Location");
        locationDropdown.getItems().addAll(locations.values().stream().sorted(Comparator.comparing(LocationName::getLongName)).toList());
        nodeToLocation(node).flatMap(l -> locationDropdown.getItems().stream().filter(l2 -> Objects.equals(l2.getUuid(), l.getUuid())).findFirst()).ifPresent(l -> {
            locationDropdown.getSelectionModel().selectItem(l);
            locationDropdown.setValue(l);
        });
        locationDropdown.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocationName location) {
                return location == null ? "" : location.getLongName();
            }

            @Override
            public LocationName fromString(String string) {
                return locations.values().stream().filter(l -> l.getLongName().equals(string)).findFirst().orElse(null);
            }
        });
        var date = new MFXDatePicker();
        date.setText("Pick Effective Date");
        date.setEditable(false);
        var makeMove = new PFXButton("Make Move");
        makeMove.getStyleClass().add("node-popover-make-move");
        makeMove.setOnAction(a -> {
            if (locationDropdown.getSelectedItem() == null || date.getCurrentDate() == null) return;
            var newID = moves.values().stream().mapToLong(Move::getUuid).max().orElse(0) + 1;
            var move = new Move(newID, node.getNodeID(), locationDropdown.getSelectedItem().getUuid(), date.getCurrentDate());
            moves.put(newID, move);
            mapEdits.add(new MapEdit(MapEdit.ActionType.ADD_MOVE, move));
        });
        var delete = new PFXButton("Delete Node");
        delete.getStyleClass().add("node-popover-delete");
        delete.setOnAction(a -> {
            var nodePoint = nodePoints.get(node.getNodeID());
            if (nodePoint == null) return;
            nodePoint.setVisible(false);
            nodePoint.setManaged(false);
            edgeLines.get(node.getNodeID()).forEach(edge -> {
                edge.setVisible(false);
                edge.setManaged(false);
            });
//            nodes = nodes.entrySet().stream().filter(e -> !e.getKey().equals(node.getNodeID())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            mapEdits.add(new MapEdit(MapEdit.ActionType.REMOVE_NODE, node));
        });
        editNode.getChildren().addAll(buildingDropdown, new Separator(), new Label("Change Location"), locationDropdown, date, makeMove, new Separator(), delete);
        // sort locations by long name
        popOver.setContentNode(editNode);
        popOver.setTitle("Edit Node " + node.getNodeID().toString());
        return popOver;
    }

    private Optional<Circle> addEditableNode(Node n) {
        var result = new ArrayList<Pair<Long, Circle>>();
        var pointOpt = drawNode(n, "#FFFF00");
        if (pointOpt.isEmpty()) return pointOpt;
        var point = pointOpt.get();
        point.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!isRightClick.test(e)) return;
            nodeEditMenu(n).show(point);
        });

        point.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!isLeftClickAndShift.test(e)) return;
            if (firstNode == null) {
                firstNode = n;
                point.setStroke(Color.valueOf("#FF00FF"));
                return;
            }
            if (n == firstNode)
                return;
            if (secondNode == null)
                secondNode = n;
            var newEdgeID = edges.values().stream().mapToLong(Edge::getUuid).max().orElse(0) + 5;
            var newEdge = new Edge(newEdgeID, firstNode.getNodeID(), secondNode.getNodeID());
            edges.put(newEdgeID, newEdge);
            addEditableEdge(newEdge);
            mapEdits.add(new MapEdit(MapEdit.ActionType.ADD_EDGE, newEdge));
            var startPoint = nodePoints.get(firstNode.getNodeID());
            if (startPoint != null) startPoint.setStroke(Color.valueOf("#000000"));
            var endPoint = nodePoints.get(secondNode.getNodeID());
            if (endPoint != null) endPoint.setStroke(Color.valueOf("#000000"));
            firstNode = secondNode = null;
        });
        DragController dragController = new DragController(point, true);
        dragController.setScaleSupplier(() -> map.getZoom());
        dragController.setFilterMouseEvents(isLeftClick);
        dragController.setOnMove(node -> {
            if (n.getXcoord() == (int) node.getLayoutX() && n.getYcoord() == (int) node.getLayoutY()) return;
            n.setXcoord((int) node.getLayoutX());
            n.setYcoord((int) node.getLayoutY());
            mapEdits.stream().filter(edit -> edit.type == MapEdit.ActionType.EDIT_NODE && Objects.equals(((Node) edit.object).getNodeID(), n.getNodeID())).findFirst().ifPresent(mapEdits::remove);
            mapEdits.add(new MapEdit(MapEdit.ActionType.EDIT_NODE, n));
        });
        dragController.setOnEnd(node -> map.enableMove(true));
        dragController.setOnStart(node -> map.enableMove(false));
        nodePoints.put(n.getNodeID(), point);
        return pointOpt;
    }

    private Optional<Line> addEditableEdge(Edge edge) {
        var startNode = nodes.get(edge.getStartNode());
        var endNode = nodes.get(edge.getEndNode());
        if (startNode == null || endNode == null) return Optional.empty();
        var edgeLine = map.drawEdge(startNode, endNode, nodePoints.get(startNode.getNodeID()), nodePoints.get(endNode.getNodeID()));
        if (edgeLine.isEmpty()) return Optional.empty();
        // add right click event to edge to select it
        var edgeSelected = new AtomicBoolean(false);
        AtomicReference<MapEdit> newEdge = new AtomicReference<>();
        edgeLine.get().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            if (!isRightClick.test(e)) return;
            edgeSelected.set(!edgeSelected.get());
            edgeLine.get().setStroke(edgeSelected.get() ? Color.RED : Color.BLACK);
            if (edgeSelected.get()) {
                newEdge.set(new MapEdit(MapEdit.ActionType.REMOVE_EDGE, edge));
                mapEdits.add(newEdge.get());
            } else {
                mapEdits.remove(newEdge.get());
                newEdge.set(null);
            }
        });
        edgeLines.put(startNode.getNodeID(), edgeLine.get());
        edgeLines.put(endNode.getNodeID(), edgeLine.get());
        return edgeLine;
    }

    private PopOver alertPopover(String title, String message) {
        var alert = new PopOver();
        alert.setTitle(title);
        alert.setContentNode(new Label(message));
        return alert;
    }

    private void editNodes() {
        map.clearMap();
        map.get().addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            // check for double click of the primary button
            if (!isLeftClick.test(e) || e.getClickCount() != 2 || e.getTarget().getClass() != ImageView.class) return;
            var location = map.getClickLocation(e);
            var node = new Node(nodes.values().stream().mapToLong(Node::getNodeID).max().orElse(0) + 5, (int) location.getX(), (int) location.getY(), map.getLayer().identifier, null);
            nodes.put(node.getNodeID(), node);
            mapEdits.add(new MapEdit(MapEdit.ActionType.ADD_NODE, node));
            var nodePoint = addEditableNode(node);
            if (nodePoint.isEmpty()) {
                alertPopover("Error", "Could not add node").show(map.get());
            }
            nodeEditMenu(node).show(nodePoints.get(node.getNodeID()));
        });
        nodes.values().forEach(this::addEditableNode);
        edges.values().forEach(this::addEditableEdge);
    }

    @FXML
    private void commitChanges() {
        if (!commiting.compareAndSet(false, true)) return;
        var totalEdited = mapEdits.size();
        commitButton.setDisable(true);
        commitProgress.setProgress(0);
        AtomicInteger counter = new AtomicInteger(0);
        var commitThread = new Thread(() -> {
            var mapEditsCopy = new ArrayList<>(mapEdits);
            mapEditsCopy.forEach(e -> {
                e.execute();
                Platform.runLater(() -> {
                    mapEdits.remove(e);
                    commitProgress.setProgress((double) counter.incrementAndGet() / totalEdited);
                });
            });

            Platform.runLater(() -> {
                edgeLines.values().forEach(e -> {
                    if (e.getStroke() == Color.RED) {
                        e.setVisible(false);
                        e.setManaged(false);
                    }
                });
                mapEdits.clear();
                commitButton.setDisable(false);
            });
            commiting.set(false);
        });
        commitThread.setDaemon(true);
        commitThread.start();
    }

    @Getter
    public static class MapEdit {
        private final ActionType type;
        private final Object object;

        private final javafx.scene.Node graphic;

        public MapEdit(ActionType type, Object object) {
            this.type = type;
            this.object = object;


            this.graphic = new Label(object == null ? "" : type.getName.apply(object));
        }

        public void execute() {
            type.action.accept(object);
        }

        @RequiredArgsConstructor
        public enum ActionType {
            ADD_NODE(o -> {
                var node = (Node) o;
                App.getSingleton().getFacade().saveNode(node);
            }, o -> "Added node " + ((Node) o).getNodeID()),
            EDIT_NODE(o -> {
                var node = (Node) o;
                App.getSingleton().getFacade().updateNode(node, new Node.Field[]{Node.Field.XCOORD, Node.Field.YCOORD, Node.Field.BUILDING});
            }, o -> "Edited node " + ((Node) o).getNodeID()),
            REMOVE_NODE(o -> {
                var node = (Node) o;
                App.getSingleton().getFacade().deleteNode(node);
            }, o -> "Removed node " + ((Node) o).getNodeID()),
            ADD_EDGE(o -> {
                var edge = (Edge) o;
                App.getSingleton().getFacade().saveEdge(edge);
            }, o -> "Added edge"),
            REMOVE_EDGE(o -> {
                var edge = (Edge) o;
                App.getSingleton().getFacade().deleteEdge(edge);
            }, o -> "Removed edge"),
            ADD_MOVE(o -> {
                var move = (Move) o;
                App.getSingleton().getFacade().saveMove(move);
            }, o -> {
                var move = (Move) o;
                return "Node " + move.getNodeID().toString() + " to " + move.getLocationID() + " on " + move.getDate().toString();
            }),
            REMOVE_MOVE(o -> {
                var move = (Move) o;
                App.getSingleton().getFacade().deleteMove(move);
            }, o -> "Move deleted");
            private final Consumer<Object> action;
            public final Function<Object, String> getName;
        }
    }
}