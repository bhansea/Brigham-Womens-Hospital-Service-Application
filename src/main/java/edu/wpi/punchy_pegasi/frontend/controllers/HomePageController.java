package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class HomePageController {
    @FXML
    MFXTableView<GenericRequestEntry> requestTable;
    @FXML
    private VBox tableContainer;
    private final Facade facade = App.getSingleton().getFacade();
    private final Map<Long, LocationName> locationNames = facade.getAllLocationName();
    private final Map<Long, Employee> employees = facade.getAllEmployee();

    @FXML
    private MFXComboBox notificationComboBox;

    @FXML
    private NumberAxis x = new NumberAxis();

    @FXML
    private NumberAxis y = new NumberAxis();

    @FXML
    @Getter
    @Setter
    private LineChart lineChart1 = new LineChart<>(x, y);

    @FXML
    @Getter
    @Setter
    private LineChart lineChart2 = new LineChart<>(x, y);

    @FXML
    @Getter
    @Setter
    private LineChart lineChart3 = new LineChart<>(x, y);

//    @FXML
//    PFXButton openWindow;

    @FXML
    private void initializeLineCharts(){
        XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
        series1.setName("Placeholder Name 1");

        //place holder data
        series1.getData().add(new XYChart.Data<>(1, 10));
        series1.getData().add(new XYChart.Data<>(2, 20));
        series1.getData().add(new XYChart.Data<>(3, 30));
        series1.getData().add(new XYChart.Data<>(4, 40));
        series1.getData().add(new XYChart.Data<>(5, 50));

        lineChart1.getData().add(series1);

        XYChart.Series<Number, Number> series2 = new XYChart.Series<>();
        series1.setName("Placeholder Name 2");

        //place holder data
        series2.getData().add(new XYChart.Data<>(5, 8));
        series2.getData().add(new XYChart.Data<>(2, 2));
        series2.getData().add(new XYChart.Data<>(3, 10));
        series2.getData().add(new XYChart.Data<>(9, 1));
        series2.getData().add(new XYChart.Data<>(5, 5));

        lineChart2.getData().add(series2);

        XYChart.Series<Number, Number> series3 = new XYChart.Series<>();
        series1.setName("Placeholder Name 3");

        //place holder data
        series3.getData().add(new XYChart.Data<>(7, 10));
        series3.getData().add(new XYChart.Data<>(20, 20));
        series3.getData().add(new XYChart.Data<>(57, 30));
        series3.getData().add(new XYChart.Data<>(70, 40));
        series3.getData().add(new XYChart.Data<>(77, 50));

        lineChart3.getData().add(series3);


    }

    @FXML
    private void initialize() {
        initializeLineCharts();
        getLineChart1();
        getLineChart2();
        getLineChart3();
    }

//    private void showServiceRequestTable(boolean show) {
//        requestTable.setVisible(show);
//        requestTable.setManaged(show);
//    }

    @FXML
    private void openSelectedWindow() {
        String selectedOption = (String) notificationComboBox.getValue();
        if (selectedOption != null) {
            if (selectedOption.equals("Meals")) {
                Stage window = new Stage();
                Scene scene = new Scene(new MFXTableView<>());
                window.setTitle(selectedOption + " Window");
                window.setScene(scene);
                window.show();
            } else if (selectedOption.equals("Flowers")) {
                Stage window = new Stage();
                Scene scene = new Scene(new MFXTableView<>());
                window.setTitle(selectedOption + " Window");
                window.setScene(scene);
                window.show();
            } else if (selectedOption.equals("Conference Room")) {
                Stage window = new Stage();
                Scene scene = new Scene(new MFXTableView<>());
                window.setTitle(selectedOption + " Window");
                window.setScene(scene);
                window.show();
            } else if (selectedOption.equals("Office Supplies")) {
                Stage window = new Stage();
                Scene scene = new Scene(new MFXTableView<>());
                window.setTitle(selectedOption + " Window");
                window.setScene(scene);
                window.show();
            } else if (selectedOption.equals("Furniture")) {
                Stage window = new Stage();
                Scene scene = new Scene(new MFXTableView<>());
                window.setTitle(selectedOption + " Window");
                window.setScene(scene);
                window.show();
            }
        }
    }
//            Stage window = new Stage();
//            Scene scene = new Scene(new MFXTableView<>());
//            window.setTitle(selectedOption + " Window");
//            window.setScene(scene);
//            window.show();

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