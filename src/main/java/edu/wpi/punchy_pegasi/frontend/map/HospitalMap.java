package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.animations.Bobbing;
import edu.wpi.punchy_pegasi.schema.Node;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import net.kurobako.gesturefx.GesturePane;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HospitalMap extends BorderPane implements IMap<HospitalFloor> {
    private static final Image downArrow = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/double-chevron-down-512.png")));
    private final Map<String, HospitalFloor> floors;
    @FXML
    GesturePane gesturePane;
    @FXML
    HBox buttonContainer;
    @FXML
    StackPane maps;
    private HospitalFloor currentFloor;

    public HospitalMap(Map<String, HospitalFloor> floors) {
        this.floors = floors;
        try {
            App.getSingleton().loadWithCache("frontend/components/HospitalMap.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public double getZoom() {
        return gesturePane.getCurrentScale();
    }

    @Override
    public void setZoom(double zoomScale) {
        gesturePane.zoomTo(zoomScale, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
    }

    @Override
    public void enableMove(boolean enabled) {
        gesturePane.setGestureEnabled(enabled);
    }

    @Override
    public javafx.scene.Node getMapNode() {
        return this;
    }

    @FXML
    @Override
    public void clearMap() {
        floors.values().forEach(HospitalFloor::clearFloor);
    }

    @FXML
    @Override
    public void initialize() {
        gesturePane.zoomTo(.1, new Point2D(gesturePane.getCurrentX(), gesturePane.getCurrentY()));
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.ALWAYS);

        floors.values().forEach(HospitalFloor::init);
        floors.values().forEach(f -> {
            f.button.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> show(f));
            buttonContainer.getChildren().add(f.button);
            maps.getChildren().add(f.root);
        });

        show(floors.get("1"));
    }

    @Override
    public void show(HospitalFloor floor) {
        currentFloor = floor;
        floor.root.setVisible(true);
        floor.button.setStyle("-fx-background-color: -pp-light-blue; -fx-text-fill: black");
        floors.values().stream().filter(f -> !Objects.equals(f.identifier, floor.identifier)).forEach(f -> {
            f.root.setVisible(false);
            f.button.setStyle("-fx-background-color: -pp-dark-blue; -fx-text-fill: white");
        });
    }

    @Override
    public void drawYouAreHere(Node node) {
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

    @Override
    public void drawLine(List<Node> nodes) {
        if (nodes.size() < 2) return;
        var floor = floors.get(nodes.get(0).getFloor());
        if (floor == null || nodes.stream().map(Node::getFloor).collect(Collectors.toSet()).size() > 1)
            return;
        var polyline = new Polyline();
        polyline.setStroke(Color.valueOf("#FF000099"));
        polyline.setStrokeWidth(10);
        polyline.getPoints().addAll(nodes.stream().flatMap(n -> Stream.of(n.getXcoord(), n.getYcoord())).map(Double::valueOf).toList());
        floor.lineCanvas.getChildren().add(0, polyline);
    }

    @Override
    public VBox makeTooltip(javafx.scene.Node parent, String text) {
        var toolTip = new VBox();
        var textLabel = new Label(text);
        // tool tip text styling
        textLabel.setTextFill(Color.valueOf("#ffffff"));
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.layout();
        toolTip.setPickOnBounds(false);
        //tool tip styling
        toolTip.getChildren().add(textLabel);
        toolTip.setPadding(new Insets(5));
        toolTip.setAlignment(Pos.CENTER);
        toolTip.setStyle("-fx-background-color: #000000aa; -fx-background-radius: 5");
        toolTip.layoutXProperty().bind(parent.layoutXProperty());
        toolTip.layoutYProperty().bind(parent.layoutYProperty());
        toolTip.layout();
        return toolTip;
    }

    @Override
    public Optional<Circle> drawNode(Node node, String color, String labelText, String hoverText) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return Optional.empty();
        var circle = new Circle(0, 0, 15);
        circle.setLayoutX(node.getXcoord());
        circle.setLayoutY(node.getYcoord());
        circle.setFill(Color.valueOf(color));
        circle.setRadius(10);
        circle.setStrokeWidth(2);
        circle.setStroke(Color.valueOf("#000000"));
        var toolTip = makeTooltip(circle, hoverText);
        var shortNameTooltip = makeTooltip(circle, labelText);

        final boolean[] updated = {false, false};
        toolTip.boundsInParentProperty().addListener((v, o, n) -> {
            if (!updated[0] && n.getHeight() > 20 && n.getWidth() > 0) {
                updated[0] = true;
                toolTip.setTranslateY(-(n.getHeight()));
                toolTip.setTranslateX(-n.getWidth() / 2);
            }
        });
        shortNameTooltip.boundsInParentProperty().addListener((v, o, n) -> {
            if (!updated[1] && n.getHeight() > 20 && n.getWidth() > 0) {
                updated[1] = true;
                shortNameTooltip.setTranslateY(-(n.getHeight()));
                shortNameTooltip.setTranslateX(-n.getWidth() / 2);
            }
        });

        circle.setOnMouseEntered(e -> {
            toolTip.setVisible(true);
            shortNameTooltip.setVisible(false);
        });
        circle.setOnMouseExited(e -> {
            toolTip.setVisible(false);
            shortNameTooltip.setVisible(true);
        });
        toolTip.setVisible(false);
        shortNameTooltip.setVisible(true);

        floor.nodeCanvas.getChildren().add(circle);
        if (!hoverText.isBlank())
            floor.tooltipCanvas.getChildren().add(toolTip);
        if (!labelText.isBlank())
            floor.tooltipCanvas.getChildren().add(shortNameTooltip);
        return Optional.of(circle);
    }

    @Override
    public Optional<Line> drawEdge(Node startNode, Node endNode, javafx.scene.Node startSceneNode, javafx.scene.Node endSceneNode) {
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

    @Override
    public javafx.scene.Node drawArrow(Node node, boolean up) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return null;
        var group = new Group();
        group.setLayoutX(node.getXcoord());
        group.setLayoutY(node.getYcoord());
        var box = new Rectangle(-12.5, -12.5, 25, 25);
        box.setFill(Color.valueOf("#1040f0"));
        var arrow = new ImageView(downArrow);
        if (up)
            arrow.setRotate(180);
        arrow.setFitWidth(15);
        arrow.setFitHeight(15);
        arrow.setLayoutX(-7.5);
        arrow.setLayoutY(-7.5);
        group.getChildren().addAll(box, arrow);
        group.setCursor(Cursor.HAND);
        floor.nodeCanvas.getChildren().add(group);
        new Bobbing(arrow).play();
        return group;
    }

    @Override
    public void focusOn(Node node) {
        var floor = floors.get(node.getFloor());
        if (floor == null) return;
        show(floor);
        gesturePane.centreOn(new Point2D(node.getXcoord(), node.getYcoord()));
    }
}
