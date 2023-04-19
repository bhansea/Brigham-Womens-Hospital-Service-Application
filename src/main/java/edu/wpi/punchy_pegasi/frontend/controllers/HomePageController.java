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
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class HomePageController {
    private final Facade facade = App.getSingleton().getFacade();
    private final Map<Long, LocationName> locationNames = facade.getAllLocationName();
    private final Map<Long, Employee> employees = facade.getAllEmployee();

    @FXML
    MFXTableView<GenericRequestEntry> requestTable;

    @FXML
    private VBox tableContainer;
//    @FXML
//    MFXTableView<String> meals;
//
//
//    @FXML
//    MFXTableView<String> flowers;
//
//    @FXML
//    MFXTableView<String> furniture;
//
//    @FXML
//    MFXTableView<String> office;

//    @FXML
//    MFXTableView<String> conference;


    @FXML
    MFXComboBox<String> notificationComboBox;
//    @FXML
//    private PieChart piechart;
    @FXML
    private LineChart lineChart;

//    public HomePageController(PieChart piechart) {
//        this.piechart = piechart;
//    }
//    public HomePageController(LineChart lineChart){
//        this.lineChart = lineChart;
//    }

//   @FXML
//   private void initialize() {
//       showServiceRequestTable(true);
//       initRequestTable();
//   }

//    private void showServiceRequestTable(boolean show) {
//        requestTable.setVisible(show);
//        requestTable.setManaged(show);
//    }
    @FXML
    private void initCharts() {
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Placeholder Data");

        //Placeholder data
        series.getData().add(new XYChart.Data<>(1, 10));
        series.getData().add(new XYChart.Data<>(2, 20));
        series.getData().add(new XYChart.Data<>(3, 30));
        series.getData().add(new XYChart.Data<>(4, 40));
        series.getData().add(new XYChart.Data<>(5, 50));

       // this.lineChart.getData().add(series);

//        PieChart.Data slice1 = new PieChart.Data("Placeholder Category1", 50);
//        PieChart.Data slice2 = new PieChart.Data("Placeholder Category2", 20);
//        PieChart.Data slice3 = new PieChart.Data("Placeholder Category3", 30);

//        this.piechart.getData().addAll(slice1, slice2, slice3);
//
//        this.piechart.setVisible(true);

        this.lineChart.setVisible(true);

    }

    private void rowClicked(GenericRequestEntry entry) {
        var original = entry.originalEntry;
        var tt = Arrays.stream(TableType.values()).filter(f -> f.getClazz() == original.getClass()).findFirst().get();
    }

    private void initRequestTable() {
        var employeeID = App.getSingleton().getAccount().getEmployeeID();
        List<RequestEntry> requestEntries = facade.getAllRequestEntry().values().stream().toList();

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

//    @FXML
//    private void resizeColumns() {
//        requestTable.autosizeColumns();
//    }

    @FXML
    private void openSelectedWindow() {
        String selectedOption = notificationComboBox.getValue();
        if(selectedOption != null){
            Stage window = new Stage();
            System.out.print("Display " + selectedOption + "table here.");
            //displayTables(selectedOption);
//            if(selectedOption == "Meals"){
//                Scene scene = new Scene();
//            }
//            if(selectedOption == "Flowers"){
//
//            }
//            if(selectedOption == "Furniture"){
//
//            }
//            if(selectedOption == "Office Supplies"){
//
//            }
//            if(selectedOption == "Conference Room"){
//
//            }
            window.setTitle(selectedOption + " Notification Window");
            //showServiceRequestTable(true);
            window.show();

        }
    }
//    private void displayTables(String type){
//        if("Meals".equals(type)){
//            meals.setVisible(true);
//            office.setVisible(false);
//            conference.setVisible(false);
//            flowers.setVisible(false);
//            furniture.setVisible(false);
//        }
//        else if("Office".equals(type)){
//            meals.setVisible(false);
//            office.setVisible(true);
//            conference.setVisible(false);
//            flowers.setVisible(false);
//            furniture.setVisible(false);
//        }
//        else if("Conference".equals(type)){
//            meals.setVisible(false);
//            office.setVisible(false);
//            conference.setVisible(true);
//            flowers.setVisible(false);
//            furniture.setVisible(false);
//        }
//        else if("Flowers".equals(type)){
//            meals.setVisible(false);
//            office.setVisible(false);
//            conference.setVisible(false);
//            flowers.setVisible(true);
//            furniture.setVisible(false);
//        }
//        else if("Furniture".equals(type)){
//            meals.setVisible(false);
//            office.setVisible(false);
//            conference.setVisible(false);
//            flowers.setVisible(false);
//            furniture.setVisible(true);
//        }
//    }

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

