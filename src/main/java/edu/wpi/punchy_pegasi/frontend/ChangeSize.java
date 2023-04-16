package edu.wpi.punchy_pegasi.frontend;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class ChangeSize {
    public static void changeSize(final Pane pane, double width, double height, EventHandler<ActionEvent> callback) {
        Duration cycleDuration = Duration.millis(500);
        Timeline timeline = new Timeline(new KeyFrame(cycleDuration, new KeyValue(pane.maxWidthProperty(), width, Interpolator.EASE_BOTH)), new KeyFrame(cycleDuration, new KeyValue(pane.maxHeightProperty(), height, Interpolator.EASE_BOTH)));

        timeline.play();
        timeline.setOnFinished(callback);
    }

    public static void changeHeight(final Pane pane, double height, EventHandler<ActionEvent> callback) {
        Duration cycleDuration = Duration.millis(500);
        Timeline timeline = new Timeline(new KeyFrame(cycleDuration, new KeyValue(pane.maxHeightProperty(), height, Interpolator.EASE_BOTH)));

        timeline.play();
        timeline.setOnFinished(callback);
    }

    public static void changeWidth(final Pane pane, double width, EventHandler<ActionEvent> callback) {
        Duration cycleDuration = Duration.millis(500);
        Timeline timeline = new Timeline(new KeyFrame(cycleDuration, new KeyValue(pane.maxWidthProperty(), width, Interpolator.EASE_BOTH)));
        timeline.play();
        timeline.setOnFinished(callback);
    }
}
