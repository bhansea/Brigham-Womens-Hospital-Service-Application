package edu.wpi.punchy_pegasi.frontend.map;

import edu.wpi.punchy_pegasi.schema.Node;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Optional;

public interface IMap<Layer> {
    double getZoom();

    void setZoom(double zoomScale);

    void enableMove(boolean enabled);

    javafx.scene.Node get();

    void addLayer(javafx.scene.Node n);

    Point2D getClickLocation(MouseEvent event);

    void clearMap();

    void initialize();

    void showLayer(Layer layer);

    Layer getLayer();

    void drawYouAreHere(Node node);

    void drawLine(List<Node> nodes);

    VBox makeTooltip(javafx.scene.Node parent, String text);

    Optional<Circle> drawNode(Node node, String color, String labelText, String hoverText);

    Optional<Line> drawEdge(Node startNode, Node endNode, javafx.scene.Node startSceneNode, javafx.scene.Node endSceneNode);

    javafx.scene.Node drawArrow(Node node, boolean up);

    void focusOn(Node node);

    void showRectangle(Rectangle rect);
}
