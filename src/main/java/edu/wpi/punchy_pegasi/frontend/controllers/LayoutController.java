package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.io.IOException;

public class LayoutController extends BorderPane {

    @FXML
    @Getter
    BorderPane ViewPane;

    public LayoutController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/layouts/AppLayout.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    @FXML
    public void initialize() {
    }
}
