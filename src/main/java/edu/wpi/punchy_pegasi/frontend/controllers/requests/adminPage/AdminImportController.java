package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdminImportController {



    @FXML
    VBox container;
    VBox vbox = new VBox();
    HBox buttonContainer = new HBox();

    private final Facade facade = App.getSingleton().getFacade();

    private final Map<String, AdminTable> tables = new LinkedHashMap<>() {{
        put("Node", new AdminTable<>("Node", TableType.NODES, () -> facade.getAllNode().values().stream().toList()));
        put("Location", new AdminTable<>("Location", TableType.LOCATIONNAMES, () -> facade.getAllLocationName().values().stream().toList()));
        put("Edge", new AdminTable<>("Edge", TableType.EDGES, () -> facade.getAllEdge().values().stream().toList()));
        put("Move", new AdminTable<>("Move", TableType.MOVES, () -> facade.getAllMove().values().stream().toList()));
    }};
    public void initialize() {


//        ObservableList<String> importTableTypes = FXCollections.observableArrayList("Nodes", "Edges", "Moves", "Location Names");
//        tableTypesComboBox.setItems(importTableTypes);
//
//        VBox.setVgrow(vbox, Priority.ALWAYS);
//        HBox.setHgrow(vbox, Priority.ALWAYS);
//        VBox.setVgrow(buttonContainer, Priority.ALWAYS);
//        HBox.setHgrow(buttonContainer, Priority.ALWAYS);

//        List<PFXButton> buttons = new ArrayList<>();
//        PFXButton importButton = new PFXButton("Import");
//        PFXButton exportButton = new PFXButton("Export");
//        buttons.add(importButton);
//        buttons.add(exportButton);




//        for (PFXButton button: buttons) {
//            button.getStyleClass().add("pfx-button");
//            buttonContainer.getChildren().add(button);
//        }
//
//        chooseText.setText("Select A Table Type");
//        buttonContainer.getStyleClass().add("admin-import-button-container");
//        vbox.getStyleClass().add("admin-import-container");
//        vbox.getChildren().add(chooseText);
//        vbox.getChildren().add(tableTypesComboBox);
//        vbox.getChildren().add(buttonContainer);
//        vbox.getChildren().add(fileText);
//        container.getChildren().add(vbox);
    }
}
