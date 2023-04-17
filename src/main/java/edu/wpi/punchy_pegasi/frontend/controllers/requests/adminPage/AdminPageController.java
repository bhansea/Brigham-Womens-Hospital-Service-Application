package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class AdminPageController {

    private final Facade facade = App.getSingleton().getFacade();
    private final Map<String, AdminTable> tables = new LinkedHashMap<>() {{
        put("Node", new AdminTable<>("Node", TableType.NODES, () -> facade.getAllNode().values().stream().toList()));
        put("Location", new AdminTable<>("Location", TableType.LOCATIONNAMES, () -> facade.getAllLocationName().values().stream().toList()));
        put("Edge", new AdminTable<>("Edge", TableType.EDGES, () -> facade.getAllEdge().values().stream().toList()));
        put("Move", new AdminTable<>("Move", TableType.MOVES, () -> facade.getAllMove().values().stream().toList()));
        put("Conference", new AdminTable<>("Conference Room Service Request", TableType.CONFERENCEREQUESTS, () -> facade.getAllConferenceRoomEntry().values().stream().toList()));
        put("Office", new AdminTable<>("Office Supplies Service Request", TableType.OFFICEREQUESTS, () -> facade.getAllOfficeServiceRequestEntry().values().stream().toList()));
        put("Furniture", new AdminTable<>("Furniture Service Request", TableType.FURNITUREREQUESTS, () -> facade.getAllFurnitureRequestEntry().values().stream().toList()));
        put("Food", new AdminTable<>("Food Service Request", TableType.FOODREQUESTS, () -> facade.getAllFoodServiceRequestEntry().values().stream().toList()));
        put("Flower", new AdminTable<>("Flower Service Request", TableType.FLOWERREQUESTS, () -> facade.getAllFlowerDeliveryRequestEntry().values().stream().toList()));
    }};
    @FXML
    Button displayButton;
    @FXML
    MFXComboBox<String> displayTableTypeComboBox;
    @FXML
    MFXComboBox<String> tableTypesComboBox;
    @FXML
    Button importButton;
    @FXML
    Button exportButton;
    @FXML
    Label fileText;
    String filePath = "";
    File selectedFile = new File("");
    File selectedDir = new File("");
    @FXML
    private VBox tableContainer;
    private AdminTable currentTable;
    @Getter
    @Setter
    private ArrayList<String> requests = new ArrayList<>(); //store requests in list to search through

    public void initialize() {
        ObservableList<String> importTableTypes = FXCollections.observableArrayList("Nodes", "Edges", "Moves", "Location Names");
        tableTypesComboBox.setItems(importTableTypes);

        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        fileChooser.setTitle("File Chooser");

        ObservableList<String> displayTableTypeList = FXCollections.observableArrayList();
        tables.values().forEach(f -> {
            displayTableTypeList.add(f.humanReadableName);
        });
        displayTableTypeComboBox.setItems(displayTableTypeList);

        initTables();

        displayButton.setOnAction(e -> {
            String name = displayTableTypeComboBox.getSelectedItem();
            tables.values().stream().filter(f -> Objects.equals(f.humanReadableName, name)).forEach(f -> {
                System.out.println(f.humanReadableName);
                showTable(f);
            });
        });

        importButton.setOnAction(e -> {
            //fix
        });

        exportButton.setOnAction(e -> {
            //fix
        });
    }

    public void showTable(AdminTable tableType) {
        currentTable = tableType;
        currentTable.getTable().setVisible(true);
        currentTable.getTable().setManaged(true);

        tables.values().stream().filter(f -> !Objects.equals(f.tableType, tableType.tableType)).forEach(f -> {
            f.getTable().setVisible(false);
            f.getTable().setManaged(false);
        });
    }

    public void initTables() {
        tables.values().forEach(AdminTable::init);
        tableContainer.getChildren().addAll(tables.values().stream().map(AdminTable::getTable).toList());
        showTable(tables.get("Node"));
    }
}
