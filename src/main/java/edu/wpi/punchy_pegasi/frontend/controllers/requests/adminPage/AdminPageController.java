package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXTab;
import edu.wpi.punchy_pegasi.frontend.components.PFXTabLayout;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;

public class AdminPageController {
    public BorderPane container;

    PFXTabLayout layout = new PFXTabLayout();
    final List<Screen> screens = new ArrayList<>(){{
        add(Screen.ADMIN_TABLE_PAGE);
        add(Screen.ADMIN_IMPORT_PAGE);
    }};

    final List<Node> nodes = new ArrayList<>();

    public AdminPageController(){};

    public void initialize() {

        var thread = new Thread(() -> {

            for (Screen screen: screens) {
                nodes.add(screen.get());
            }
            Platform.runLater(() -> {
                for (int i = 0; i < screens.size(); i++) {
                    PFXTab tab = new PFXTab(screens.get(i).getReadable().replace("Admin ", ""),nodes.get(i));
                    tab.setOnMouseClicked(e -> {
                        layout.setSelected(tab);
                        container.setCenter(layout);
                    });
                    layout.addTab(tab);
                }
                layout.setSelected(layout.getTabGroup().get(0));
                container.setCenter(layout);
            });
        });
        thread.setDaemon(true);
        thread.start();
    }
}
