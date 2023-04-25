package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import javafx.scene.layout.StackPane;
import javafx.scene.input.*;
import org.controlsfx.control.PopOver;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class HomePageController {
    @FXML
    MFXTableView<GenericRequestEntry> requestTable = new MFXTableView<>();
    @FXML
    private VBox tableContainer = new VBox();
    private final Facade facade = App.getSingleton().getFacade();
    private final Map<Long, LocationName> locationNames = facade.getAllLocationName();
    private final Map<Long, Employee> employees = facade.getAllEmployee();
//    @FXML
//    private VBox pie;
    @FXML
    private PieChart piechart = new PieChart();

    @FXML
    private Label dateLabel = new Label();
    @FXML
    private Label timeLabel = new Label();

    @FXML
    private VBox dateTimeBox = new VBox();

    @FXML
    private AnchorPane dateTimeAnchor = new AnchorPane();
    @FXML
    private void initialize() {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        LocalTime currentTimeNoSec = currentTime.truncatedTo(ChronoUnit.MINUTES);

        dateLabel.setText(currentDate.toString());
        timeLabel.setText(currentTimeNoSec.toString());

        dateTimeBox.layoutXProperty().bind(dateTimeAnchor.widthProperty().subtract(dateLabel.widthProperty()).subtract(10.0));
        // Bind layoutYProperty of Label to heightProperty of AnchorPane - 10.0 to position it in the top corner
        dateTimeBox.layoutYProperty().bind(dateTimeAnchor.heightProperty().subtract(dateLabel.heightProperty()).subtract(10.0));



//        dateLabel.layoutXProperty().bind(dateTimeBox.layoutXProperty()
//                        .subtract(dateLabel.widthProperty())
//                        .subtract(10));
//        dateLabel.layoutYProperty().bind(dateTimeBox.layoutYProperty()
//                        .subtract(dateLabel.heightProperty())
//                        .subtract(10));
//
//        timeLabel.layoutXProperty().bind(dateTimeBox.layoutXProperty()
//                .subtract(timeLabel.widthProperty())
//                .subtract(10));
//        timeLabel.layoutYProperty().bind(dateTimeBox.layoutYProperty()
//                .subtract(timeLabel.heightProperty())
//                .subtract(10));

        requestTable.prefWidthProperty().bind(tableContainer.widthProperty());
        requestTable.prefHeightProperty().bind(tableContainer.heightProperty());
        List<RequestEntry> requestEntries = facade.getAllRequestEntry().values().stream().toList();
        int done = requestEntries.stream().mapToInt(r->r.getStatus() == RequestEntry.Status.DONE ? 1 : 0).sum();
        int processing = requestEntries.stream().mapToInt(r->r.getStatus() == RequestEntry.Status.PROCESSING ? 1 : 0).sum();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Done",done),
                new PieChart.Data("Processing",processing));
        piechart.setData(pieChartData);
        piechart.setTitle("Service Request");
        piechart.setLegendVisible(false);

        //pie.getChildren().add(piechart);
        //Scene scene = new Scene(pie, 500, 500);
        //Stage pstage = new Stage();
        //pstage.setTitle("Pie");
        //pstage.setScene(scene);
        //pstage.show();
        initRequestTable();
        showServiceRequestTable(true);

    }

    private void showServiceRequestTable(boolean show) {
        requestTable.setVisible(show);
        requestTable.setManaged(show);
    }

//    @FXML
//    private void openSelectedWindow() {
//        String selectedOption = (String) notificationComboBox.getValue();
//        if (selectedOption != null) {
//            if (selectedOption.equals("Meals")) {
//                Stage window = new Stage();
//                Scene scene = new Scene(new MFXTableView<>());
//                window.setTitle(selectedOption + " Window");
//                window.setScene(scene);
//                window.show();
//            } else if (selectedOption.equals("Flowers")) {
//                Stage window = new Stage();
//                Scene scene = new Scene(new MFXTableView<>());
//                window.setTitle(selectedOption + " Window");
//                window.setScene(scene);
//                window.show();
//            } else if (selectedOption.equals("Conference Room")) {
//                Stage window = new Stage();
//                Scene scene = new Scene(new MFXTableView<>());
//                window.setTitle(selectedOption + " Window");
//                window.setScene(scene);
//                window.show();
//            } else if (selectedOption.equals("Office Supplies")) {
//                Stage window = new Stage();
//                Scene scene = new Scene(new MFXTableView<>());
//                window.setTitle(selectedOption + " Window");
//                window.setScene(scene);
//                window.show();
//            } else if (selectedOption.equals("Furniture")) {
//                Stage window = new Stage();
//                Scene scene = new Scene(new MFXTableView<>());
//                window.setTitle(selectedOption + " Window");
//                window.setScene(scene);
//                window.show();
//            }
//        }

    private void rowClicked(GenericRequestEntry entry) {
        var original = entry.originalEntry;
        var tt = Arrays.stream(TableType.values()).filter(f -> f.getClazz() == original.getClass()).findFirst().get();
    }

    private void initRequestTable() {
        var employeeID = App.getSingleton().getAccount().getEmployeeID();
        ObservableList<RequestEntry> requestEntries = facade.getAllAsListRequestEntry();


        ObservableList<GenericRequestEntry> requestList = FXCollections.observableArrayList(requestEntries.stream()
                .filter(e -> App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel() || Objects.equals(e.getStaffAssignment(), employeeID))
                .map(GenericRequestEntry::new)
                .toList());

        MFXTableColumn<GenericRequestEntry> locationCol = new MFXTableColumn<>("Location", true);
        MFXTableColumn<GenericRequestEntry> employeeCol = new MFXTableColumn<>("Employee", true);
        MFXTableColumn<GenericRequestEntry> additionalCol = new MFXTableColumn<>("Additional Notes", true);
        MFXTableColumn<GenericRequestEntry> statusCol = new MFXTableColumn<>("Status", true);
        MFXTableColumn<GenericRequestEntry> typeCol = new MFXTableColumn<>("Request Type", true);
//        MFXTableColumn<PFXButton> buttonCol = new MFXTableColumn<>();
        locationCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.location));
        employeeCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.assigned));
        additionalCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.additionalNotes));
        statusCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.status));
        typeCol.setRowCellFactory(r -> new MFXTableRowCell<>(r2 -> r2.tableType));
        requestTable.setTableRowFactory(r -> {
            var row = new MFXTableRow<>(requestTable, r);
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                VBox vbox = new VBox();
                var popover = new PopOver();
                popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                vbox.setAlignment(Pos.CENTER);
                PFXButton button = new PFXButton();
                button.setText(r.status.toString());
                vbox.getChildren().add(button);
                vbox.getStyleClass().add("homepage-popup");
                popover.setContentNode(vbox);
                popover.show(row);
            });
            return row;
        });
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