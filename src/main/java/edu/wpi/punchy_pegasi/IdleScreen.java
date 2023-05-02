package edu.wpi.punchy_pegasi;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class IdleScreen extends VBox {
    private final double idleTimeSeconds;
    private boolean isIdle = false;

    public IdleScreen(double idleTimeSeconds) {
        this.idleTimeSeconds = idleTimeSeconds;

        ImageView image = new ImageView(new Image(App.getSingleton().resolveResource("frontend/assets/BW-logo.png").get().toString()));
        setStyle("-fx-background-color: white");
        setAlignment(Pos.CENTER);
        getChildren().add(image);
    }
}
