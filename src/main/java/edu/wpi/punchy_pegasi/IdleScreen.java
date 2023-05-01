package edu.wpi.punchy_pegasi;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class IdleScreen {
    private Scene scene;
    private long lastInteractionTime;
    private long idleThreshold = 5000;
    private AnimationTimer idleTimer;

    public IdleScreen(Scene scene) {
        this.scene = scene;
        // Register event handlers to detect user input
        scene.setOnMouseMoved(event -> resetTimer());
        scene.setOnMouseClicked(event -> resetTimer());
        scene.setOnScroll(event -> resetTimer());
        scene.setOnKeyPressed(event -> resetTimer());
        startTimer();
    }

    private boolean isIdle() {
        // Get the current system time
        long currentTime = System.currentTimeMillis();
        // Calculate the time since the last interaction
        long idleTime = currentTime - lastInteractionTime;
        // Check if the user has been idle for longer than the threshold
        return idleTime >= idleThreshold;
    }

    private void updateScreen() {
        Color randomColor = Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
        scene.setFill(randomColor);
    }


    private void startTimer() {
        idleTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isIdle()) {
                    updateScreen();
                }
            }
        };
        idleTimer.start();
    }

    public void resetTimer() {
        lastInteractionTime = System.currentTimeMillis();
    }
}
