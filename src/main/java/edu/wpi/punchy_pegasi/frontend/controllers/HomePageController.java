package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.FlowerDeliveryRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Comparator;

public class HomePageController {
    @FXML MFXTableView<RequestEntry> requestTable;
    @FXML
    MFXTableView<RequestEntry> openRequestTable;

    @FXML
    public void initialize() {
        showServiceRequestTable();
    }

    public void showServiceRequestTable() {
        openRequestTable.setVisible(true);
        openRequestTable.setManaged(true);
    }

    public void initRequestTable() {
        RequestEntryDaoImpl requestDaoImpl = new RequestEntryDaoImpl();
        ObservableList<RequestEntry> requestList = FXCollections.observableArrayList(requestDaoImpl.getAll().values());

        for (RequestEntry.Status status : RequestEntry.Status.values()) {
            MFXTableColumn<RequestEntry> col = new MFXTableColumn<>(status.name(), true, Comparator.comparing(RequestEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(status::valueOf));
            requestTable.getTableColumns().add(col);
        }
        requestTable.setItems(requestList);
    }
}