package edu.wpi.punchy_pegasi.frontend.controllers;

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
    }};

    public void initialize() {
        container.setStyle("-fx-background-color: -pfx-background;");
        container.setCenter(layout);
        var thread = new Thread(() -> {
            for (Screen screen: screens) {
                var node = screen.get();
                Platform.runLater(() -> {
                    var tab = new PFXTab(screen.getReadable(), node);
                    layout.addTab(tab);
                    tab.setOnMouseClicked(e -> layout.setSelected(tab));
                });
            }
            Platform.runLater(() -> layout.setSelected(layout.getTabGroup().get(0)));
        });
        thread.setDaemon(true);
        thread.start();
    }


}
