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
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class HomePageController {
    @FXML
    MFXTableView<GenericRequestEntry> requestTable;
    @FXML
    private VBox tableContainer;
    @FXML
    private VBox pie;
    private final Facade facade = App.getSingleton().getFacade();
    private final Map<Long, LocationName> locationNames = facade.getAllLocationName();
    private final Map<Long, Employee> employees = facade.getAllEmployee();
    @FXML
    private PieChart piechart = new PieChart();

    //facade method to get field name and get the field back
    //make an upadate = set item --  list, make the pie chart, set the items at the end

    /**
     *  public void reload(){
     *         var thread = new Thread(() -> {
     *             var list = getAll.get();
     *             Platform.runLater(() -> {
     *                 ObservableList<T> tableList = FXCollections.observableList(list);
     *                 table.setItems(tableList);
     *             });
     *         });
     *         thread.setDaemon(true);
     *         thread.start();
     *     }
     */

    @FXML
    private void initialize() {
        List<RequestEntry> requestEntries = facade.getAllRequestEntry().values().stream().toList();
        int done = requestEntries.stream().mapToInt(r->r.getStatus() == RequestEntry.Status.DONE ? 1 : 0).sum();
        int processing = requestEntries.stream().mapToInt(r->r.getStatus() == RequestEntry.Status.PROCESSING ? 1 : 0).sum();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Done",done),
                new PieChart.Data("Processing",processing));
        piechart.setData(pieChartData);
        showServiceRequestTable(false);
        piechart.setTitle("Service Request");

//        pie.getChildren().add(piechart);
//        Scene scene = new Scene(pie, 500, 500);
//        Stage pstage = new Stage();
//        pstage.setTitle("Pie");
//        pstage.setScene(scene);
//        pstage.show();
    }

    /*
    List<RequestEntry> requestEntries = new ArrayList<>();
        requestEntries.addAll(facade.getAllFurnitureRequestEntry().values());
        requestEntries.addAll(facade.getAllConferenceRoomEntry().values());
        requestEntries.addAll(facade.getAllFlowerDeliveryRequestEntry().values());
        requestEntries.addAll(facade.getAllOfficeServiceRequestEntry().values());
        requestEntries.addAll(facade.getAllFoodServiceRequestEntry().values());

        private void setAccount(Account account) {
        sidebarItems.forEach(s -> {
            var screen = s.getScreen();
            if (screen == null) return;
            if (account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()) {
                s.setVisible(true);
                s.setManaged(true);
            } else {
                s.setVisible(false);
                s.setManaged(false);
            }
        });
        var loggedIn = account.getAccountType() != Account.AccountType.NONE;
        logout.setVisible(loggedIn);
        logout.setManaged(loggedIn);
        }
     */


    private void showServiceRequestTable(boolean show) {
        requestTable.setVisible(show);
        requestTable.setManaged(show);
    }

    private void rowClicked(GenericRequestEntry entry) {
        var original = entry.originalEntry;
        var tt = Arrays.stream(TableType.values()).filter(f -> f.getClazz() == original.getClass()).findFirst().get();
    }

    private void initRequestTable() {
        var employeeID = App.getSingleton().getAccount().getEmployeeID();
        List<RequestEntry> requestEntries = new ArrayList<>();
        requestEntries.addAll(facade.getAllFurnitureRequestEntry().values());
        requestEntries.addAll(facade.getAllConferenceRoomEntry().values());
        requestEntries.addAll(facade.getAllFlowerDeliveryRequestEntry().values());
        requestEntries.addAll(facade.getAllOfficeServiceRequestEntry().values());
        requestEntries.addAll(facade.getAllFoodServiceRequestEntry().values());

        ObservableList<GenericRequestEntry> requestList = FXCollections.observableArrayList(requestEntries.stream()
                .filter(e -> App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel() || Objects.equals(e.getStaffAssignment(), employeeID))
                .map(GenericRequestEntry::new)
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
//        requestTable.setTableRowFactory(r -> {
//            var row = new MFXTableRow<>(requestTable, r);
//            row.setStyle("-fx-background-color: red");
//            row.setOnMouseClicked(e -> {
//                rowClicked(r);
//            });
//            return row;
//        });
        requestTable.getTableColumns().addAll(locationCol, employeeCol, additionalCol, statusCol, typeCol);
        requestTable.setItems(requestList);
        requestTable.prefWidthProperty().bind(tableContainer.widthProperty());
        requestTable.prefHeightProperty().bind(tableContainer.heightProperty());
        requestTable.autosizeColumnsOnInitialization();
    }

    @FXML
    private void resizeColumns() {
        requestTable.autosizeColumns();
    }

    private class GenericRequestEntry {
        RequestEntry originalEntry;
        String location;
        String assigned;
        String additionalNotes;
        RequestEntry.Status status;
        TableType tableType;

        GenericRequestEntry(RequestEntry re) {
            originalEntry = re;
            location = locationNames.getOrDefault(re.getLocationName(), new LocationName(null, "Unknown location", "", null)).getLongName();
            assigned = employees.getOrDefault(re.getStaffAssignment(), new Employee(0L, "Unknown", "Employee")).getFullName();
            additionalNotes = re.getAdditionalNotes();
            status = re.getStatus();
            tableType = Arrays.stream(TableType.values())
                    .filter(tt -> tt.getClazz() == re.getClass())
                    .findFirst()
                    .orElseGet(() -> TableType.GENERIC);
        }
    }
}