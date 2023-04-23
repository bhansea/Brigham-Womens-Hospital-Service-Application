package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PFXPopup extends Application {
    String text;

    public void start(Stage stage) {
        Popup popup = new Popup();
        Label label = new Label();
        label.setText(text);
        label.setMinWidth(300);
        label.setMinHeight(300);
        label.setStyle("-fx-background-color: -pfx-background; -fx-font-size: 25; -fx-font-weight: bold;");
        label.setPadding(new Insets(20));
        popup.getContent().add(label);
        Button exit = new Button("Exit");
        popup.getContent().add(exit);
        popup.show(stage);
        exit.setOnAction(event -> {
            popup.hide();
            App.getSingleton().navigate(Screen.HOME);
        });
    }

    public PFXPopup(Stage stage, String text) {
        super();
        this.text = text;
        start(stage);
    }
}