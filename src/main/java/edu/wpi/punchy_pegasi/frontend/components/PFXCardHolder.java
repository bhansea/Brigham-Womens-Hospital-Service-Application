package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;


public class PFXCardHolder extends BorderPane {
    ScrollPane scrollPane = new ScrollPane();
    public PFXCardHolder() {
        super();
        getChildren().add(scrollPane);
    }
}
