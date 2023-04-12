package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HomePageController {
    @FXML
    private VBox tableContainer;

    @FXML
    MFXTableView<GenericRequestEntry> requestTable;

    private Facade facade = App.getSingleton().getFacade();

    @FXML
    private void initialize() {
        showServiceRequestTable();
        initRequestTable();
    }

    private void showServiceRequestTable() {
        requestTable.setVisible(true);
        requestTable.setManaged(true);
    }

    private void initRequestTable() {
        var employeeID = App.getSingleton().getAccount().getEmployeeID();
        List<RequestEntry> requestEntries = new ArrayList<>();
        requestEntries.addAll(facade.getAllFurnitureRequestEntry().values());
        requestEntries.addAll(facade.getAllConferenceRoomEntry().values());
        requestEntries.addAll(facade.getAllFlowerDeliveryRequestEntry().values());
        requestEntries.addAll(facade.getAllOfficeServiceRequestEntry().values());

        var locationNames = facade.getAllLocationName();
        var employees = facade.getAllEmployee();


        ObservableList<GenericRequestEntry> requestList = FXCollections.observableArrayList(requestEntries.stream()
                .filter(e -> App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel() || Objects.equals(e.getStaffAssignment(), employeeID))
                .map(re -> new GenericRequestEntry(
                        locationNames.getOrDefault(re.getLocationName(), new LocationName(null, "Unknown location", "", null)).getLongName(),
                        employees.getOrDefault(re.getStaffAssignment(), new Employee(0L, "Unknown", "Employee")).getFullName(),
                        re.getAdditionalNotes(),
                        re.getStatus(),
                        Arrays.stream(TableType.values())
                                .filter(tt -> tt.getClazz() == re.getClass())
                                .findFirst()
                                .orElseGet(() -> TableType.GENERIC)))
                .toList());

        MFXTableColumn<GenericRequestEntry> locationCol = new MFXTableColumn<>("Location", true);
        MFXTableColumn<GenericRequestEntry> employeeCol = new MFXTableColumn<>("Employee", true);
        MFXTableColumn<GenericRequestEntry> additionalCol = new MFXTableColumn<>("Additional Notes", true);
        MFXTableColumn<GenericRequestEntry> statusCol = new MFXTableColumn<>("Status", true);
        MFXTableColumn<GenericRequestEntry> typeCol = new MFXTableColumn<>("Request Type", true);
        locationCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.location));
        employeeCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.assigned));
        additionalCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.additionalNotes));
        statusCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.status));
        typeCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.tableType));
        requestTable.getTableColumns().addAll(locationCol, employeeCol, additionalCol, statusCol, typeCol);
        requestTable.setItems(requestList);
        requestTable.prefWidthProperty().bind(tableContainer.widthProperty());
        requestTable.prefHeightProperty().bind(tableContainer.heightProperty());
        var thread = new Thread(() -> {
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            resizeColumns();
        });
        thread.setDaemon(true);
        thread.start();
    }

    @AllArgsConstructor
    private class GenericRequestEntry {
        String location;
        String assigned;
        String additionalNotes;
        RequestEntry.Status status;
        TableType tableType;
    }

    @FXML
    private void resizeColumns() {
        requestTable.autosizeColumns();
    }
}