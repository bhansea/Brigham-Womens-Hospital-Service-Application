package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXAlertCard;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage.AdminTablePageController;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import org.phoenicis.javafx.collections.MappedList;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class HomePageController {
    @FXML
    MFXTableView<RequestEntry> requestTable = new MFXTableView<>();
    @FXML
    private VBox tableContainer = new VBox();
    private final Facade facade = App.getSingleton().getFacade();
    private final ObservableMap<Long, LocationName> locationNames = facade.getAllLocationName();
    private final Map<Long, Employee> employees = facade.getAllEmployee();

    @FXML
    private PieChart piechart = new PieChart();

    @FXML
    VBox alertsHolder = new VBox();
    @FXML
    VBox alertsContainer = new VBox();
    @FXML
    private Label timeLabel = new Label();

    @FXML
    private VBox dateTimeBox = new VBox();

    @FXML
    private AnchorPane dateTimeAnchor = new AnchorPane();
    @FXML
    private void initialize() {
        configTimer(1000);


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

        List<Alert> alerts = App.getSingleton().getFacade().getAllAsListAlert();
        for(Alert alert: alerts) {
            if(alerts.isEmpty()){
                alertsContainer.setVisible(false);
                alertsContainer.setManaged(false);
            }
            if(App.getSingleton().getAccount().getEmployeeID().equals(alert.getEmployeeID())) {
                alertsHolder.getChildren().add(new PFXAlertCard(alert));
                alertsContainer.setVisible(true);
                alertsContainer.setManaged(false);
            }
        }



        initRequestTable();
        showServiceRequestTable(true);

    }

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
        ObservableList<RequestEntry> requestEntries = facade.getAllAsListRequestEntry()
                .filtered(e ->
                        App.getSingleton().getAccount().getAccountType().getShieldLevel() >= Account.AccountType.ADMIN.getShieldLevel()
                                || Objects.equals(e.getStaffAssignment(), employeeID));


        ObservableList<GenericRequestEntry> requestList = new MappedList<>(requestEntries,
                GenericRequestEntry::new);
//        requestEntries.addListener((ListChangeListener<? super RequestEntry>) c -> {
//            while(c.next()){
//                System.out.println();
//            }
//        });
//        requestList.addListener((ListChangeListener<? super GenericRequestEntry>) c -> {
//            while(c.next()){
//                System.out.println();
//            }
//        });

        MFXTableColumn<RequestEntry> locationCol = new MFXTableColumn<>("Location", true);
        MFXTableColumn<RequestEntry> employeeCol = new MFXTableColumn<>("Employee", true);
        MFXTableColumn<RequestEntry> additionalCol = new MFXTableColumn<>("Additional Notes", true);
        MFXTableColumn<RequestEntry> statusCol = new MFXTableColumn<>("Status", true);
        MFXTableColumn<RequestEntry> typeCol = new MFXTableColumn<>("Request Type", true);
//        MFXTableColumn<PFXButton> buttonCol = new MFXTableColumn<>();
//        location = locationNames.getOrDefault(re.getLocationName(), new LocationName(null, "Unknown location", "", null)).getLongName();
//        assigned = employees.getOrDefault(re.getStaffAssignment(), new Employee(0L, "Unknown", "Employee")).getFullName();
//        additionalNotes = re.getAdditionalNotes();
//        status = re.getStatus();
//        tableType = Arrays.stream(TableType.values())
//                .filter(tt -> tt.getClazz() == re.getClass())
//                .findFirst()
//                .orElseGet(() -> TableType.GENERIC);
        locationCol.setRowCellFactory(r -> new MFXTableRowCell<>(re -> locationNames.getOrDefault(re.getLocationName(), new LocationName(null, "Unknown location", "", null)).getLongName()));
        employeeCol.setRowCellFactory(r -> new MFXTableRowCell<>(re -> employees.getOrDefault(re.getStaffAssignment(), new Employee(0L, "Unknown", "Employee")).getFullName()));
        additionalCol.setRowCellFactory(r -> new MFXTableRowCell<>(re -> re.getAdditionalNotes()));
        statusCol.setRowCellFactory(r -> new MFXTableRowCell<>(re -> re.getStatus()));
        typeCol.setRowCellFactory(r -> new MFXTableRowCell<>(re -> Arrays.stream(TableType.values())
                .filter(tt -> tt != TableType.REQUESTS && tt.getClazz() == re.getClass())
               .findFirst()
               .orElseGet(() -> TableType.GENERIC)));
        requestTable.setTableRowFactory(r -> {
            var row = new MFXTableRow<>(requestTable, r);
            row.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                VBox vbox = new VBox();
                var popover = new PopOver();
                popover.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                vbox.setAlignment(Pos.CENTER);
                vbox.setPadding(new Insets(10));
                PFXButton button = new PFXButton();
                button.setMinWidth(200);
                button.setMaxHeight(200);

                button.setOnAction(m -> {
                    if (button.getText().toLowerCase().equals("change to done")) {
                        button.setText("Change To Processing");
                        button.setStyle("-fx-background-color: -pfx-danger");
                        r.setStatus(RequestEntry.Status.DONE);
                        facade.updateRequestEntry(r, new RequestEntry.Field[]{RequestEntry.Field.STATUS});
                        requestTable.update();
                    } else if (button.getText().toLowerCase().equals("change to processing")) {
                        button.setText("Change To Done");
                        button.setStyle("-fx-background-color: -pfx-success");
                        r.setStatus(RequestEntry.Status.PROCESSING);
                        facade.updateRequestEntry(r, new RequestEntry.Field[]{RequestEntry.Field.STATUS});
                        requestTable.update();
                    }
                });

                if (r.getStatus() == RequestEntry.Status.DONE) {
                    button.setText("Change To Processing");
                    button.setStyle("-fx-background-color: -pfx-warning");
                } else if (r.getStatus() == RequestEntry.Status.PROCESSING) {
                    button.setText("Change To Done");
                    button.setStyle("-fx-background-color: -pfx-success");
                }

                vbox.getChildren().add(button);
                vbox.getStyleClass().add("homepage-popup");
                popover.setContentNode(vbox);
                popover.show(row);

            });
            return row;
        });
//        requestTable.getSelectionModel().selectionProperty().addListener((observable, oldValue, newValue) -> {
//            newValue.values().stream().findFirst().ifPresent(r -> rowClicked.accept(r));
//        });


        requestTable.getTableColumns().addAll(locationCol, employeeCol, additionalCol, statusCol, typeCol);
        requestTable.setItems(requestEntries);
        requestTable.prefWidthProperty().bind(tableContainer.widthProperty());
        requestTable.prefHeightProperty().bind(tableContainer.heightProperty());
        requestTable.autosizeColumnsOnInitialization();

    }
    private void updateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        timeLabel.setText(formatter.format(date));
    }

    private void configTimer(final long interuptPeriodMill) {
        long currTime = System.currentTimeMillis();
        long startDelay = interuptPeriodMill - (currTime % interuptPeriodMill);
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    updateTime();
                });
            }
        }, startDelay, interuptPeriodMill);
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
        @Setter
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