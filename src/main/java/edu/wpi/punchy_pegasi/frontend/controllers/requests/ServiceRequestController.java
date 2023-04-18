package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXTab;
import edu.wpi.punchy_pegasi.frontend.components.PFXTabLayout;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage.AdminTable;
import edu.wpi.punchy_pegasi.schema.IField;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import java.util.*;


public class ServiceRequestController {

    public BorderPane container;
    PFXTabLayout layout = new PFXTabLayout();
    final List<Screen> screens = new ArrayList<>(){{
        add(Screen.FLOWER_DELIVERY_REQUEST);
        add(Screen.FOOD_SERVICE_REQUEST);
        add(Screen.OFFICE_SERVICE_REQUEST);
        add(Screen.FURNITURE_DELIVERY_SERVICE_REQUEST);
        add(Screen.CONFERENCE_ROOM_SERVICE_REQUEST);
    }};

    final List<Node> nodes = new ArrayList<>();

    public ServiceRequestController() {}
    public void initialize() {
        var thread = new Thread(() -> {

            for (Screen screen: screens) {
                nodes.add(screen.get());
            }
            Platform.runLater(() -> {
                for (int i = 0; i < screens.size(); i++) {
                    if (!screens.get(i).getReadable().toLowerCase().contains("request") || screens.get(i).getReadable().equalsIgnoreCase("service request")) {
                        continue;
                    }
                    PFXTab tab = new PFXTab(screens.get(i).getReadable().replace("Request ", ""),nodes.get(i));
                    tab.setOnMouseClicked(e -> {
                        layout.setSelected(tab.getNode());
                        container.setCenter(layout);
                    });
                    layout.addTab(tab);
                }
                container.setCenter(layout);

                layout.setSelected(nodes.get(0));
            });
        });
        thread.setDaemon(true);
        thread.start();
    }


}
