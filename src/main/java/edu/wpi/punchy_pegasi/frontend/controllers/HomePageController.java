package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomePageController {
    ObservableList<HBox> list;
    @FXML
    private VBox serviceRequests;
    @FXML
    public void initialize() {
        ObservableList<HBox> list = FXCollections.observableArrayList();
        Bindings.bindContent(serviceRequests.getChildren(), list);
        for (var entry : Screen.values()) {
            if(!entry.name().toLowerCase().contains("request"))
                continue;
            FXMLLoader loader = new FXMLLoader(App.class.getResource("components/Link.fxml"));
            var controller = new LinkController(entry.getReadable(), () -> {
                Navigation.navigate(entry);
            });
            loader.setController(controller);
            try {
                list.add(loader.load());
            } catch (IOException e){
            }
        }
    }

    public void signageButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.SIGNAGE);
    }

    public void mapButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.MAP_PAGE);
    }
}
