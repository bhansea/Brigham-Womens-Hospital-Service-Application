package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXTab;
import edu.wpi.punchy_pegasi.frontend.components.PFXTabLayout;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public class InfoController {
    public BorderPane container;
    PFXTabLayout layout = new PFXTabLayout();
    final List<Screen> screens = new ArrayList<>(){{
        add(Screen.ABOUT);
        add(Screen.CREDITS);
    }};

    public void initialize() {
        container.setStyle("-fx-background-color: -pfx-background;");
        container.setCenter(layout);
        App.getSingleton().getExecutorService().submit(() -> {
            for (Screen screen : screens) {
                var node = screen.get();
                Platform.runLater(() -> {
                    var tab = new PFXTab(screen.getReadable(), node);
                    layout.addTab(tab);
                    tab.setOnMouseClicked(e -> layout.setSelected(tab));
                });
            }
            Platform.runLater(() -> layout.setSelected(layout.getTabGroup().get(0)));
        });
    }


}
