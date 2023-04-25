package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class PFXAlert extends VBox {
    @Getter
    private final String text;

    public PFXAlert(String text) {
        super();
        this.text = text;
        Label label = new Label();
        label.setText(text);
        PFXButton exit = new PFXButton("Exit");
        getChildren().add(label);
        getChildren().add(exit);
        getStyleClass().add("pfx-alert");
        exit.setOnAction(event -> App.getSingleton().getLayout().hideOverlay());
        App.getSingleton().getLayout().showOverlay(this);
    }
}