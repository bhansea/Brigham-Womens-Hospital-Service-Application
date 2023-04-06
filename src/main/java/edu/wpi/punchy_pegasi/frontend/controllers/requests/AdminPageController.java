package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.generated.*;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparing;


public class AdminPageController {

    public HBox buttonContainer;
    @FXML Button flowerButton;
    @FXML Button foodButton;
    @FXML Button furnitureButton;
    @FXML Button officeButton;
    @FXML Button conferenceButton;
    @FXML MFXTableView<ConferenceRoomEntry> conferenceRoomServiceRequestTable;
    @FXML MFXTableView<OfficeServiceRequestEntry> officeServiceRequestTable;
    @FXML MFXTableView<FurnitureRequestEntry> furnitureServiceRequestTable;
    @FXML MFXTableView<FoodServiceRequestEntry> foodServiceRequestTable;
    @FXML MFXTableView<FlowerDeliveryRequestEntry> flowerServiceRequestTable;
    @FXML MFXComboBox<String> tableTypesComboBox;
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
        ObservableList<String> tableTypes = FXCollections.observableArrayList("Nodes", "Edges", "Moves", "Location Names");
        tableTypesComboBox.setItems(tableTypes);

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        fileChooser.setTitle("File Chooser");

        initTables();
        // Table View stuff





        flowerButton.setOnAction(e -> {
            showFlowerTable();
        });

        foodButton.setOnAction(e -> {
            showFoodTable();
        });

        furnitureButton.setOnAction(e -> {
            showFurnitureTable();
        });

        conferenceButton.setOnAction(e -> {
            showConferenceTable();
        });

        officeButton.setOnAction(e -> {
            showOfficeSuppliesTable();
        });


        importButton.setOnAction(e -> {

            selectedFile = fileChooser.showOpenDialog(stage);
            filePath = selectedFile.getAbsolutePath();
            fileText.setText(filePath);

            while(selectedFile != null) {
                if (tableTypesComboBox.getSelectedItem().equals("Nodes")) {
                    try {
                        pdb.importTable(TableType.NODES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Edges")) {
                    try {
                        pdb.importTable(TableType.EDGES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Moves")) {
                    try {
                        pdb.importTable(TableType.MOVES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Location Names")) {
                    try {
                        pdb.importTable(TableType.LOCATIONNAMES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        exportButton.setOnAction(e -> {
            selectedDir = directoryChooser.showDialog(stage);
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
    }

    public void initTables() {

        FlowerDeliveryRequestEntryDaoImpl flowerDaoImp = new FlowerDeliveryRequestEntryDaoImpl();

        ObservableList<FlowerDeliveryRequestEntry> flowerList = FXCollections.observableArrayList(flowerDaoImp.getAll().values());
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerServiceCol = new MFXTableColumn<>("Service ID", true, Comparator.comparing(FlowerDeliveryRequestEntry::getServiceID));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerPatientNameCol = new MFXTableColumn<>("Patient Name", true, Comparator.comparing(FlowerDeliveryRequestEntry::getPatientName));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerRoomNumberCol = new MFXTableColumn<>("Room Number", true, Comparator.comparing(FlowerDeliveryRequestEntry::getRoomNumber));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerStaffAssignCol = new MFXTableColumn<>("Staff Assignment", true, Comparator.comparing(FlowerDeliveryRequestEntry::getStaffAssignment));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerStatusCol = new MFXTableColumn<>("Status", true, Comparator.comparing(FlowerDeliveryRequestEntry::getStatus));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerSizeCol = new MFXTableColumn<>("Flower Size", true, Comparator.comparing(FlowerDeliveryRequestEntry::getFlowerSize));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerAmountCol = new MFXTableColumn<>("Flower Amount", true, Comparator.comparing(FlowerDeliveryRequestEntry::getFlowerAmount));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerTypeCol = new MFXTableColumn<>("Flower Type", true, Comparator.comparing(FlowerDeliveryRequestEntry::getFlowerType));
        MFXTableColumn<FlowerDeliveryRequestEntry> flowerAdditionalNotesCol = new MFXTableColumn<>("Additional Notes", true, Comparator.comparing(FlowerDeliveryRequestEntry::getAdditionalNotes));


        flowerStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getServiceID));
        flowerPatientNameCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getPatientName));
        flowerRoomNumberCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getRoomNumber));
        flowerStaffAssignCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getStaffAssignment));
        flowerStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getStatus));
        flowerSizeCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getFlowerSize));
        flowerAmountCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getFlowerAmount));
        flowerTypeCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getFlowerType));
        flowerAdditionalNotesCol.setRowCellFactory(p -> new MFXTableRowCell<>(FlowerDeliveryRequestEntry::getAdditionalNotes));

        flowerServiceRequestTable.setItems(flowerList);
        flowerServiceRequestTable.getTableColumns().addAll(flowerServiceCol, flowerPatientNameCol, flowerRoomNumberCol, flowerStaffAssignCol, flowerStatusCol, flowerAmountCol, flowerTypeCol, flowerAdditionalNotesCol);

        FoodServiceRequestEntryDaoImpl foodDaoImpl = new FoodServiceRequestEntryDaoImpl();

        ObservableList<FoodServiceRequestEntry> foodList = FXCollections.observableArrayList(foodDaoImpl.getAll().values());
        MFXTableColumn<FoodServiceRequestEntry> foodServiceCol = new MFXTableColumn<>("Service ID", true, Comparator.comparing(FoodServiceRequestEntry::getServiceID));
        MFXTableColumn<FoodServiceRequestEntry> foodPatientNameCol = new MFXTableColumn<>("Patient Name", true, Comparator.comparing(FoodServiceRequestEntry::getPatientName));
        MFXTableColumn<FoodServiceRequestEntry> foodRoomNumberCol = new MFXTableColumn<>("Room Number", true, Comparator.comparing(FoodServiceRequestEntry::getRoomNumber));
        MFXTableColumn<FoodServiceRequestEntry> foodStaffAssignCol = new MFXTableColumn<>("Staff Assignment", true, Comparator.comparing(FoodServiceRequestEntry::getStaffAssignment));
        MFXTableColumn<FoodServiceRequestEntry> foodStatusCol = new MFXTableColumn<>("Status", true, Comparator.comparing(FoodServiceRequestEntry::getStatus));
        MFXTableColumn<FoodServiceRequestEntry> foodSelectionCol = new MFXTableColumn<>("Food Selection", true, Comparator.comparing(FoodServiceRequestEntry::getFoodSelection));
        MFXTableColumn<FoodServiceRequestEntry> foodTempCol = new MFXTableColumn<>("Food Temperature", true, Comparator.comparing(FoodServiceRequestEntry::getTempType));
//        MFXTableColumn<FoodServiceRequestEntry> additionalItemsCol = new MFXTableColumn<>("Additional Items", true, Comparator.comparing(FoodServiceRequestEntry::getAdditionalItems));
        MFXTableColumn<FoodServiceRequestEntry> foodRestrictionCol = new MFXTableColumn<>("Food Restriction", true, Comparator.comparing(FoodServiceRequestEntry::getDietaryRestrictions));
        MFXTableColumn<FoodServiceRequestEntry> foodAdditionalNotesCol = new MFXTableColumn<>("Additional Notes", true, Comparator.comparing(FoodServiceRequestEntry::getAdditionalNotes));


        foodServiceCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getServiceID));
        foodPatientNameCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getPatientName));
        foodRoomNumberCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getRoomNumber));
        foodStaffAssignCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getStaffAssignment));
        foodStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getStatus));
        foodSelectionCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getFoodSelection));
        foodTempCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getTempType));
//        additionalItemsCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getAdditionalItems));
        foodRestrictionCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getDietaryRestrictions));
        foodAdditionalNotesCol.setRowCellFactory(p -> new MFXTableRowCell<>(FoodServiceRequestEntry::getAdditionalNotes));

        foodServiceRequestTable.setItems(foodList);
        foodServiceRequestTable.getTableColumns().addAll(foodServiceCol, foodPatientNameCol, foodRoomNumberCol, foodStaffAssignCol, foodStatusCol, foodSelectionCol, foodTempCol,foodRestrictionCol ,foodAdditionalNotesCol);

        ConferenceRoomEntryDaoImpl conferenceDaoImpl = new ConferenceRoomEntryDaoImpl();

        ObservableList<ConferenceRoomEntry> conferenceList = FXCollections.observableArrayList(conferenceDaoImpl.getAll().values());
        MFXTableColumn<ConferenceRoomEntry> conferenceServiceCol = new MFXTableColumn<>("Service ID", true, Comparator.comparing(ConferenceRoomEntry::getServiceID));
        MFXTableColumn<ConferenceRoomEntry> conferenceRoomNumberCol = new MFXTableColumn<>("Room Number", true, Comparator.comparing(ConferenceRoomEntry::getRoomNumber));
        MFXTableColumn<ConferenceRoomEntry> conferenceStaffAssignCol = new MFXTableColumn<>("Staff Assignment", true, Comparator.comparing(ConferenceRoomEntry::getStaffAssignment));
        MFXTableColumn<ConferenceRoomEntry> conferenceStatusCol = new MFXTableColumn<>("Status", true, Comparator.comparing(ConferenceRoomEntry::getStatus));
        MFXTableColumn<ConferenceRoomEntry> beginTimeCol = new MFXTableColumn<>("Beginning Time", true, Comparator.comparing(ConferenceRoomEntry::getBeginningTime));
        MFXTableColumn<ConferenceRoomEntry> endTimeCol = new MFXTableColumn<>("Ending Time", true, Comparator.comparing(ConferenceRoomEntry::getEndTime));
        MFXTableColumn<ConferenceRoomEntry> conferenceAdditionalNotesCol = new MFXTableColumn<>("Additional Notes", true, Comparator.comparing(ConferenceRoomEntry::getAdditionalNotes));


        conferenceServiceCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getServiceID));
        conferenceRoomNumberCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getRoomNumber));
        conferenceStaffAssignCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getStaffAssignment));
        conferenceStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getStatus));
        beginTimeCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getBeginningTime));
        endTimeCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getEndTime));
        conferenceAdditionalNotesCol.setRowCellFactory(p -> new MFXTableRowCell<>(ConferenceRoomEntry::getAdditionalNotes));

        conferenceRoomServiceRequestTable.setItems(conferenceList);
        conferenceRoomServiceRequestTable.getTableColumns().addAll(conferenceServiceCol, conferenceRoomNumberCol, conferenceStaffAssignCol, conferenceStatusCol, beginTimeCol, endTimeCol ,conferenceAdditionalNotesCol);

        FurnitureRequestEntryDaoImpl furnitureDaoImpl = new FurnitureRequestEntryDaoImpl();

        ObservableList<FurnitureRequestEntry> furnitureList = FXCollections.observableArrayList(furnitureDaoImpl.getAll().values());
        MFXTableColumn<FurnitureRequestEntry> furnitureServiceCol = new MFXTableColumn<>("Service ID", true, Comparator.comparing(FurnitureRequestEntry::getServiceID));
        MFXTableColumn<FurnitureRequestEntry> furnitureRoomNumberCol = new MFXTableColumn<>("Room Number", true, Comparator.comparing(FurnitureRequestEntry::getRoomNumber));
        MFXTableColumn<FurnitureRequestEntry> furnitureStaffAssignCol = new MFXTableColumn<>("Staff Assignment", true, Comparator.comparing(FurnitureRequestEntry::getStaffAssignment));
        MFXTableColumn<FurnitureRequestEntry> furnitureStatusCol = new MFXTableColumn<>("Status", true, Comparator.comparing(FurnitureRequestEntry::getStatus));
//        MFXTableColumn<FurnitureRequestEntry> furnitureSelCol = new MFXTableColumn<>("Furniture Selection", true, Comparator.comparing(FurnitureRequestEntry::getSelectFurniture));
        MFXTableColumn<FurnitureRequestEntry> furnitureAdditionalNotesCol = new MFXTableColumn<>("Additional Notes", true, Comparator.comparing(FurnitureRequestEntry::getAdditionalNotes));


        furnitureServiceCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getServiceID));
        furnitureRoomNumberCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getRoomNumber));
        furnitureStaffAssignCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getStaffAssignment));
        furnitureStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getStatus));
//        furnitureSelCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getSelectFurniture));
        furnitureAdditionalNotesCol.setRowCellFactory(p -> new MFXTableRowCell<>(FurnitureRequestEntry::getAdditionalNotes));

        furnitureServiceRequestTable.setItems(furnitureList);
        furnitureServiceRequestTable.getTableColumns().addAll(furnitureServiceCol, furnitureRoomNumberCol, furnitureStaffAssignCol, furnitureStatusCol ,furnitureAdditionalNotesCol);


        OfficeServiceRequestEntryDaoImpl officeDaoImpl = new OfficeServiceRequestEntryDaoImpl();

        ObservableList<OfficeServiceRequestEntry> officeList = FXCollections.observableArrayList(officeDaoImpl.getAll().values());
        MFXTableColumn<OfficeServiceRequestEntry> officeServiceCol = new MFXTableColumn<>("Service ID", true, Comparator.comparing(OfficeServiceRequestEntry::getServiceID));
        MFXTableColumn<OfficeServiceRequestEntry> officeRoomNumberCol = new MFXTableColumn<>("Room Number", true, Comparator.comparing(OfficeServiceRequestEntry::getRoomNumber));
        MFXTableColumn<OfficeServiceRequestEntry> officeStaffAssignCol = new MFXTableColumn<>("Staff Assignment", true, Comparator.comparing(OfficeServiceRequestEntry::getStaffAssignment));
        MFXTableColumn<OfficeServiceRequestEntry> officeStatusCol = new MFXTableColumn<>("Status", true, Comparator.comparing(OfficeServiceRequestEntry::getStatus));
        MFXTableColumn<OfficeServiceRequestEntry> officeReqCol = new MFXTableColumn<>("Office Supplies", true, Comparator.comparing(OfficeServiceRequestEntry::getOfficeRequest));
        MFXTableColumn<OfficeServiceRequestEntry> employeeOrderCol = new MFXTableColumn<>("Employee Order Name", true, Comparator.comparing(OfficeServiceRequestEntry::getEmployeeName));
        MFXTableColumn<OfficeServiceRequestEntry> additionalNotesCol = new MFXTableColumn<>("Additional Notes", true, Comparator.comparing(OfficeServiceRequestEntry::getAdditionalNotes));


        officeServiceCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getServiceID));
        officeRoomNumberCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getRoomNumber));
        officeStaffAssignCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getStaffAssignment));
        officeStatusCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getStatus));
        officeReqCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getOfficeRequest));
        employeeOrderCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getEmployeeName));
        additionalNotesCol.setRowCellFactory(p -> new MFXTableRowCell<>(OfficeServiceRequestEntry::getAdditionalNotes));

        officeServiceRequestTable.setItems(officeList);
        officeServiceRequestTable.getTableColumns().addAll(officeServiceCol, officeRoomNumberCol, officeStaffAssignCol, officeStatusCol, officeReqCol, employeeOrderCol, additionalNotesCol);

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

    }


    @FXML
    String showReq() {
        String temp = "";
        if (submit.isPressed()) {
            temp = servSearchBar.getText();
            for (int i = 0; i < requests.size(); i++) { //does not loop because theres nothing in requests so no size
                //so i++ is never used as well
                if (temp.matches(requests.get(i))) {
                    return requests.get(i);
                } else {
                    throw new RuntimeException("No such request found.");
                }
            }
        }

        //match case
        //throw error if no request found
        //display request
        //display back button/somehow reset the search bar for a new search?

        return temp;


    }
}
