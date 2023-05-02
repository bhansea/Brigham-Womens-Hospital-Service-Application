package edu.wpi.punchy_pegasi;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class IdleScreen extends Application {

    private final int idleTimeSeconds = 5;
    private boolean isIdle = false;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        Label screensaverLabel = new Label("Screensaver");
        StackPane root = new StackPane(screensaverLabel);
        Scene scene = new Scene(root, Color.BLACK);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.setOpacity(0);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screensaverLabel.setLayoutX((screenBounds.getWidth() - screensaverLabel.getWidth()) / 2);
        screensaverLabel.setLayoutY((screenBounds.getHeight() - screensaverLabel.getHeight()) / 2);
        Timeline idleTimeline = new Timeline(new KeyFrame(Duration.seconds(idleTimeSeconds), e -> {
            if (!isIdle) {
                stage.show();
                isIdle = true;
            }
        }));
        idleTimeline.setCycleCount(Timeline.INDEFINITE);
        idleTimeline.play();
        scene.setOnMouseMoved(e -> {
            if (isIdle) {
                stage.hide();
                isIdle = false;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
