package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.io.IOException;

public class LayoutController extends BorderPane {

    @FXML
    @Getter
    BorderPane ViewPane;

    @FXML
    HeaderController topLayout;

    @FXML
    SidebarController leftLayout;

    public LayoutController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/layouts/AppLayout.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void showLeftLayout(boolean hide) {
        leftLayout.setVisible(hide);
        leftLayout.setManaged(hide);
    }

    public void showTopLayout(boolean hide) {
        topLayout.setVisible(hide);
        topLayout.setManaged(hide);
    }

    @FXML
    public void initialize() {
    }
}
