package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class PFXPopup extends Application {
    String text;

    public void start(Stage stage) {
        Popup popup = new Popup();
        VBox vBox = new VBox();
        Label label = new Label();
        label.setText(text);
        PFXButton exit = new PFXButton("Exit");
        vBox.getChildren().add(label);
        vBox.getChildren().add(exit);
        vBox.getStyleClass().add("pfx-popup");
        popup.getContent().add(vBox);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        popup.show(stage);
        popup.setOnHidden(event -> {
            popup.hide();
            App.getSingleton().navigate(Screen.HOME);
        });
        exit.setOnAction(event -> popup.hide());
    }

    public PFXPopup(Stage stage, String text) {
        super();
        this.text = text;
        start(stage);
    }
}