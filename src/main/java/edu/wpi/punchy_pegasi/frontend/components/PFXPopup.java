package edu.wpi.punchy_pegasi.frontend.components;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PFXPopup extends Application {
    public void start(Stage stage) {
        stage.setTitle("PFXPopup");
        Button exit = new Button("Exit");
        BorderPane borderPane = new BorderPane();
        HBox layoutHBox = new HBox();
        Label label = new Label("Your request has been submitted!");
        Popup popup = new Popup();
        layoutHBox.getChildren().addAll(label, exit);
        popup.getContent().add(layoutHBox);
        EventHandler<ActionEvent> event = e -> {
            if (!popup.isShowing())
                popup.show(stage);
            else
                popup.hide();
        };
        exit.setOnAction(event);
        borderPane.getChildren().add(layoutHBox);
        Scene scene = new Scene(borderPane, 200, 200);
        stage.setScene(scene);
        stage.show();
    }
}