package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class PFXAlert extends VBox {
    String text;

    public void start(Stage stage) {
        Popup popup = new Popup();
    }

    @Getter
    @Setter
    private Runnable exit;

    public PFXAlert(Stage stage, String text) {
        super();
        this.text = text;
        Label label = new Label();
        label.setText(text);
        PFXButton exit = new PFXButton("Exit");
        getChildren().add(label);
        getChildren().add(exit);
        getStyleClass().add("pfx-alert");
        exit.setOnAction(event -> this.exit.run());
    }
}