package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;

public class PFXAlert extends VBox {
    @Getter
    private final String text;

    @Setter
    private Runnable exit;

    public PFXAlert(String text) {
        super();
        this.text = text;
        Label label = new Label();
        label.setText(text);
        PFXButton exit = new PFXButton("Exit");
        getChildren().add(label);
        getChildren().add(exit);
        getStyleClass().add("pfx-alert");
        exit.setOnAction(event -> {
            App.getSingleton().getLayout().hideOverlay();
            if (this.exit != null) this.exit.run();
        });
        App.getSingleton().getLayout().showOverlay(this);
    }
    public PFXAlert(String text, Runnable exit) {
        this(text);
        setExit(exit);
    }
}