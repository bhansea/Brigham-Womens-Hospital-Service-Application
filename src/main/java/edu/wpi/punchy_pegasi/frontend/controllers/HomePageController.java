package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.Facade;
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
    @FXML
    MFXTableView<RequestEntry> requestTable;
    @FXML
    MFXTableView<RequestEntry> openRequestTable;

    private Facade facade = App.getSingleton().getFacade();

    @FXML
    public void initialize() {
        showServiceRequestTable();
    }

    public void showServiceRequestTable() {
        openRequestTable.setVisible(true);
        openRequestTable.setManaged(true);
    }

    public void initRequestTable() {
        facade.getAllFurnitureRequestEntry();
        facade.getAllConferenceRoomEntry();
        facade.getAllFlowerDeliveryRequestEntry();
        facade.getAllOfficeServiceRequestEntry();
        ObservableList<RequestEntry> requestList = FXCollections.observableArrayList(facade.getAllFurnitureRequestEntry().values());
        requestList.addAll(facade.getAllConferenceRoomEntry().values());
        requestList.addAll(facade.getAllFlowerDeliveryRequestEntry().values());
        requestList.addAll(facade.getAllOfficeServiceRequestEntry().values());
        //FIX THIS
        for (RequestEntry.Field field : RequestEntry.Field.values()) {
            MFXTableColumn<RequestEntry> col = new MFXTableColumn<>(field.getColName(), true, Comparator.comparing(FlowerDeliveryRequestEntry::getServiceID));
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            flowerServiceRequestTable.getTableColumns().add(col);
        }
        requestTable.setItems(requestList);
    }
}