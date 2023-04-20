package edu.wpi.punchy_pegasi.frontend.animations;

import animatefx.animation.AnimationFX;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class FollowPath extends AnimationFX {
    private final double rotationOffset;
    private final double speed;
    private final double xOffset;
    private final double yOffset;
    /**
     * Create new Bounce
     *
     * @param node The node to affect
     */
    private final List<Point2D> points = new ArrayList<>();

    public FollowPath(Node node, List<Point2D> points, double xOffset, double yOffset, double rotationOffset, double speed) {
        super();
        this.points.addAll(points);
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.rotationOffset = rotationOffset;
        this.speed = speed;
        setNode(node);
    }

    @Override
    public AnimationFX resetNode() {
        getNode().setTranslateX(xOffset);
        getNode().setTranslateY(yOffset);
        getNode().setRotate(0 + rotationOffset);
        return this;
    }

    @Override
    protected void initTimeline() {
        var timeline = new Timeline();
        var totalDistance = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            //get distance between points
            var start = points.get(i);
            var end = points.get(i + 1);
            //get angle
            var angle = Math.toDegrees(Math.atan2(end.getY() - start.getY(), end.getX() - start.getX()));
            createKeyFrame(timeline, totalDistance, start, angle);
            totalDistance += start.distance(end);
            if (i == points.size() - 2)
                createKeyFrame(timeline, totalDistance, end, angle);
        }
        setTimeline(timeline);
        getTimeline().setCycleCount(INDEFINITE);
    }

    private void createKeyFrame(Timeline timeline, int totalDistance, Point2D start, double angle) {
        var keyFrame = new KeyFrame(Duration.millis(totalDistance * speed),
                new KeyValue(getNode().translateXProperty(), start.getX() + xOffset, Interpolator.LINEAR),
                new KeyValue(getNode().translateYProperty(), start.getY() + yOffset, Interpolator.LINEAR),
                new KeyValue(getNode().rotateProperty(), angle + rotationOffset, Interpolator.DISCRETE) //SPLINE(1.0, 0.0, 1, 0)
        );
        timeline.getKeyFrames().add(keyFrame);
    }
}
