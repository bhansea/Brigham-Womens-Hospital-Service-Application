package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomePageController {
    ObservableList<HBox> list;
    @FXML
    private VBox serviceRequests;

    @FXML
    public void initialize() {
        ObservableList<HBox> list = FXCollections.observableArrayList();
        Bindings.bindContent(serviceRequests.getChildren(), list);
        for (var entry : Screen.values()) {
            if (!entry.name().toLowerCase().contains("request"))
                continue;
            LinkControl linkControl = new LinkControl(entry.getReadable(), () -> {
                Navigation.navigate(entry);
            });
            list.add(linkControl);
        }
    }

    public void signageButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.SIGNAGE);
    }

    public void mapButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.MAP_PAGE);
    }
}
