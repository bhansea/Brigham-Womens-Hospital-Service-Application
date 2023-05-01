package edu.wpi.punchy_pegasi;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
public class IdleScreen extends AnimationTimer{

    private Scene screen;
    private Timeline timeline;

    public IdleScreen(Scene screen) {
            this.screen = screen;
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(5), event -> updateScreen());
            timeline = new Timeline(keyFrame);
            timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void updateScreen() {
        Color randomColor = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        screen.setFill(randomColor);
    }
    @Override
    public void handle(long now) {
        if (isIdle()) {
            start();
        }
    }
    private boolean isIdle() {
        // Get the last time the user interacted with the application
        long lastInteractionTime = screen.getOnMouseDragged().getClass();
        // Get the current system time
        long currentTime = System.currentTimeMillis();
        // Calculate the time since the last interaction
        long idleTime = currentTime - lastInteractionTime;
        // Define the idle threshold (e.g. 5 seconds)
        long idleThreshold = 5000;
        // Check if the user has been idle for longer than the threshold
        return idleTime >= idleThreshold;
    }
    public void start() {
        timeline.play();
    }
    public void reset() {
        timeline.stop();
        timeline.play();
    }
}


/**
 * private IdleScreenSaver idleScreenSaver;
 *
 *     @Override
 *     public void start(Stage primaryStage) throws Exception {
 *         StackPane root = new StackPane(new Label("Idle Screen Saver"));
 *         Scene scene = new Scene(root, 400, 300);
 *
 *         idleScreenSaver = new IdleScreenSaver(scene);
 *         scene.setOnMouseMoved(event -> idleScreenSaver.start());
 *         scene.setOnKeyPressed(event -> idleScreenSaver.reset());
 *
 *         primaryStage.setScene(scene);
 *         primaryStage.show();
 *     }
 *
 *     public static void main(String[] args) {
 *         launch(args);
 *     }
 */
