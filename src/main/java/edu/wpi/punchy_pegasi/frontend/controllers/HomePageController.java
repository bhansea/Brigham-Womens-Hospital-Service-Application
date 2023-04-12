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
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Comparator;

public class HomePageController {
    @FXML
    private VBox tableContainer;

    @FXML
    MFXTableView<RequestEntry> requestTable;

    private Facade facade = App.getSingleton().getFacade();

    @FXML
    public void initialize() {
        showServiceRequestTable();
        initRequestTable();
    }

    public void showServiceRequestTable() {
        requestTable.setVisible(true);
        requestTable.setManaged(true);
    }

    public void initRequestTable() {
        ObservableList<RequestEntry> requestList = FXCollections.observableArrayList(facade.getAllFurnitureRequestEntry().values());
        requestList.addAll(facade.getAllConferenceRoomEntry().values());
        requestList.addAll(facade.getAllFlowerDeliveryRequestEntry().values());
        requestList.addAll(facade.getAllOfficeServiceRequestEntry().values());
        for (RequestEntry.Field field : Arrays.stream(RequestEntry.Field.values()).filter(v -> v != RequestEntry.Field.SERVICE_ID).toList()) {
            MFXTableColumn<RequestEntry> col = new MFXTableColumn<>(field.getColName(), true);
            col.setRowCellFactory(p -> new MFXTableRowCell<>(field::getValue));
            requestTable.getTableColumns().add(col);
        }
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

    @FXML
    private void resizeColumns() {
        requestTable.autosizeColumns();
    }
}