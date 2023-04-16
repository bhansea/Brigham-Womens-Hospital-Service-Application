package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.generated.*;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;


public class AdminPageController {

    public HBox buttonContainer;
    @FXML
    Button displayButton;
    @FXML
    MFXComboBox<String> displayTableTypeComboBox;
    @FXML
    MFXTableView<Node> nodeTable;
    @FXML
    MFXTableView<LocationName> locationTable;
    @FXML
    MFXTableView<Edge> edgeTable;
    @FXML
    MFXTableView<Move> moveTable;
    @FXML
    MFXTableView<ConferenceRoomEntry> conferenceRoomServiceRequestTable;
    @FXML
    MFXTableView<OfficeServiceRequestEntry> officeServiceRequestTable;
    @FXML
    MFXTableView<FurnitureRequestEntry> furnitureServiceRequestTable;
    @FXML
    MFXTableView<FoodServiceRequestEntry> foodServiceRequestTable;
    @FXML
    MFXTableView<FlowerDeliveryRequestEntry> flowerServiceRequestTable;
    @FXML
    MFXComboBox<String> tableTypesComboBox;
    @FXML
    MFXTextField servSearchBar;

    @FXML
    Button submit;

    @FXML
    Button importButton;
    @FXML
    Button exportButton;
    @FXML
    Label fileText;

    String filePath = "";
    File selectedFile = new File("");
    File selectedDir = new File("");


    // @FXML MFXButton back;
    @Getter
    @Setter
    private ArrayList<String> requests = new ArrayList<>(); //store requests in list to search through

    public void initialize() {
        final PdbController pdb = App.getSingleton().getPdb();
        // TODO: make tableTypes enum type
        ObservableList<String> tableTypes = FXCollections.observableArrayList("Nodes", "Edges", "Moves", "Location Names");
        tableTypesComboBox.setItems(tableTypes);

        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        fileChooser.setTitle("File Chooser");

        // TODO: make tableTypes enum type
        ObservableList<String> displayTableTypeList = FXCollections.observableArrayList
                ("Flower", "Food", "Furniture", "Conference Room Reservation", "Office Supplies", "Node", "Edge", "Moves", "Location Names");
        displayTableTypeComboBox.setItems(displayTableTypeList);

        initTables();

        displayButton.setOnAction(e -> {
            if (displayTableTypeComboBox.getSelectedItem().equals("Flower")) {
                showFlowerTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Food")) {
                showFoodTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Furniture")) {
                showFurnitureTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Conference Room Reservation")) {
                showConferenceTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Office Supplies")) {
                showOfficeSuppliesTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Node")) {
                showNodeTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Edge")) {
                showEdgeTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Moves")) {
                showMoveTable();
            } else if (displayTableTypeComboBox.getSelectedItem().equals("Location Names")) {
                showLocationTable();
            }
        });

        importButton.setOnAction(e -> {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            selectedFile = fileChooser.showOpenDialog(App.getSingleton().getPopupStage());

            if (selectedFile != null && tableTypesComboBox.getSelectedItem() != null) {
                filePath = selectedFile.getAbsolutePath();
                fileText.setText(filePath);
                if (tableTypesComboBox.getSelectedItem().equals("Nodes")) {
                    try {
                        pdb.importTable(TableType.NODES, filePath);
                        selectedFile = null;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Edges")) {
                    try {
                        pdb.importTable(TableType.EDGES, filePath);
                        selectedFile = null;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Moves")) {
                    try {
                        pdb.importTable(TableType.MOVES, filePath);
                        selectedFile = null;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Location Names")) {
                    try {
                        pdb.importTable(TableType.LOCATIONNAMES, filePath);
                        selectedFile = null;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        exportButton.setOnAction(e -> {
            selectedDir = directoryChooser.showDialog(App.getSingleton().getPopupStage());
            fileText.setText(selectedDir.getAbsolutePath());

            while (selectedDir != null) {
                if (tableTypesComboBox.getSelectedItem().equals("Nodes")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Nodes.csv", TableType.NODES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Edges")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Edges.csv", TableType.EDGES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Moves")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Moves.csv", TableType.MOVES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Location Names")) {
                    try {
                        pdb.exportTable(selectedDir + "\\LocationName.csv", TableType.LOCATIONNAMES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });
    }

    public void showFlowerTable() {
        flowerServiceRequestTable.setVisible(true);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);

        flowerServiceRequestTable.setManaged(true);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);


        // add function that shows only current table, hides the rest
    }

    public void showFoodTable() {
        foodServiceRequestTable.setVisible(true);
        flowerServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);

        foodServiceRequestTable.setManaged(true);
        flowerServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }

    public void showConferenceTable() {
        conferenceRoomServiceRequestTable.setVisible(true);
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);

        conferenceRoomServiceRequestTable.setManaged(true);
        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }

    public void showFurnitureTable() {
        furnitureServiceRequestTable.setVisible(true);
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);

        furnitureServiceRequestTable.setManaged(true);
        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }

    public void showOfficeSuppliesTable() {
        officeServiceRequestTable.setVisible(true);
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);

        officeServiceRequestTable.setManaged(true);
        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }


    public void showNodeTable() {
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);


        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(true);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(true);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }

    public void showEdgeTable() {
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);


        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(true);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(true);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }

    public void showMoveTable() {
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);


        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(true);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(true);
        locationTable.setManaged(false);
    }

    public void showLocationTable() {
        flowerServiceRequestTable.setVisible(false);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);


        flowerServiceRequestTable.setManaged(false);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(true);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(true);
    }

    public void initFlowerTable() {
        FlowerDeliveryRequestEntryDaoImpl flowerDaoImpl = new FlowerDeliveryRequestEntryDaoImpl();
        ObservableList<FlowerDeliveryRequestEntry> flowerList = FXCollections.observableArrayList(flowerDaoImpl.getAll().values());

        for (FlowerDeliveryRequestEntry.Field field : FlowerDeliveryRequestEntry.Field.values()) {
            MFXTableColumn<FlowerDeliveryRequestEntry> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(FlowerDeliveryRequestEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            flowerServiceRequestTable.getTableColumns().add(col);
        }
        flowerServiceRequestTable.setItems(flowerList);
    }

    public void initFoodTable() {
        FoodServiceRequestEntryDaoImpl foodDaoImpl = new FoodServiceRequestEntryDaoImpl();
        ObservableList<FoodServiceRequestEntry> foodList = FXCollections.observableArrayList(foodDaoImpl.getAll().values());

        for (FoodServiceRequestEntry.Field field : FoodServiceRequestEntry.Field.values()) {
            MFXTableColumn<FoodServiceRequestEntry> col = new MFXTableColumn<>(field.getColName(), true);
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            foodServiceRequestTable.getTableColumns().add(col);
        }
        foodServiceRequestTable.setItems(foodList);
    }

    public void initConferenceTable() {
        ConferenceRoomEntryDaoImpl conferenceDaoImpl = new ConferenceRoomEntryDaoImpl();
        ObservableList<ConferenceRoomEntry> conferenceList = FXCollections.observableArrayList(conferenceDaoImpl.getAll().values());

        for (ConferenceRoomEntry.Field field : ConferenceRoomEntry.Field.values()) {
            MFXTableColumn<ConferenceRoomEntry> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(ConferenceRoomEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            conferenceRoomServiceRequestTable.getTableColumns().add(col);
        }
        conferenceRoomServiceRequestTable.setItems(conferenceList);
    }

    public void initOfficeSuppliesTable() {
        OfficeServiceRequestEntryDaoImpl officeDaoImpl = new OfficeServiceRequestEntryDaoImpl();
        ObservableList<OfficeServiceRequestEntry> officeList = FXCollections.observableArrayList(officeDaoImpl.getAll().values());

        for (OfficeServiceRequestEntry.Field field : OfficeServiceRequestEntry.Field.values()) {
            MFXTableColumn<OfficeServiceRequestEntry> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(OfficeServiceRequestEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            officeServiceRequestTable.getTableColumns().add(col);
        }
        officeServiceRequestTable.setItems(officeList);
    }

    public void initFurnitureTable() {
        FurnitureRequestEntryDaoImpl furnitureDaoImpl = new FurnitureRequestEntryDaoImpl();
        ObservableList<FurnitureRequestEntry> furnitureList = FXCollections.observableArrayList(furnitureDaoImpl.getAll().values());

        for (FurnitureRequestEntry.Field field : FurnitureRequestEntry.Field.values()) {
            MFXTableColumn<FurnitureRequestEntry> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(FurnitureRequestEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            furnitureServiceRequestTable.getTableColumns().add(col);
        }
        furnitureServiceRequestTable.setItems(furnitureList);
    }

    public void initNodeTable() {
        NodeDaoImpl nodeDaoImpl = new NodeDaoImpl();
        ObservableList<Node> nodeList = FXCollections.observableArrayList(nodeDaoImpl.getAll().values());

        for (Node.Field field : Node.Field.values()) {
            MFXTableColumn<Node> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(Node::getNodeID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            nodeTable.getTableColumns().add(col);
        }
        nodeTable.setItems(nodeList);
    }

    public void initEdgeTable() {
        EdgeDaoImpl edgeDaoImpl = new EdgeDaoImpl();
        ObservableList<Edge> edgeList = FXCollections.observableArrayList(edgeDaoImpl.getAll().values());

        for (Edge.Field field : Edge.Field.values()) {
            MFXTableColumn<Edge> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(Edge::getUuid));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            edgeTable.getTableColumns().add(col);
        }
        edgeTable.setItems(edgeList);
    }

    public void initMoveTable() {
        MoveDaoImpl moveDaoImpl = new MoveDaoImpl();
        ObservableList<Move> moveList = FXCollections.observableArrayList(moveDaoImpl.getAll().values());

        for (Move.Field field : Move.Field.values()) {
            MFXTableColumn<Move> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(Move::getUuid));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            moveTable.getTableColumns().add(col);
        }
        moveTable.setItems(moveList);
    }

    public void initLocationTable() {
        LocationNameDaoImpl locationDaoImpl = new LocationNameDaoImpl();
        ObservableList<LocationName> locationList = FXCollections.observableArrayList(locationDaoImpl.getAll().values());

        for (LocationName.Field field : LocationName.Field.values()) {
            MFXTableColumn<LocationName> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(LocationName::getUuid));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            locationTable.getTableColumns().add(col);
        }
        locationTable.setItems(locationList);
    }

    public void initTables() {

        initFlowerTable();
        initFoodTable();
        initConferenceTable();
        initOfficeSuppliesTable();
        initFurnitureTable();
        initNodeTable();
        initEdgeTable();
        initMoveTable();
        initLocationTable();


        flowerServiceRequestTable.setVisible(true);
        foodServiceRequestTable.setVisible(false);
        conferenceRoomServiceRequestTable.setVisible(false);
        furnitureServiceRequestTable.setVisible(false);
        officeServiceRequestTable.setVisible(false);


        flowerServiceRequestTable.setManaged(true);
        foodServiceRequestTable.setManaged(false);
        conferenceRoomServiceRequestTable.setManaged(false);
        furnitureServiceRequestTable.setManaged(false);
        officeServiceRequestTable.setManaged(false);

        nodeTable.setVisible(false);
        edgeTable.setVisible(false);
        moveTable.setVisible(false);
        locationTable.setVisible(false);

        nodeTable.setManaged(false);
        edgeTable.setManaged(false);
        moveTable.setManaged(false);
        locationTable.setManaged(false);
    }
}
