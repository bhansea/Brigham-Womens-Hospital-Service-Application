package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXAlertCard;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Alert;
import edu.wpi.punchy_pegasi.schema.Employee;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AdminAlertPageController {
    @FXML
    public HBox timePickerContainer;
    @FXML
    public MFXDatePicker endDatePicker;
    @FXML
    private TextField alertDescription;
    @FXML
    private TextField alertTitle;
    @FXML
    private VBox alertsContainer;
    @FXML
        private PFXButton sendButton;
    @FXML
    private VBox activeAlertsContainer;
    @FXML
    private BorderPane container;
    @FXML
    private PFXButton removeButton;
    @FXML
    private MFXComboBox alertTypeComboBox;
    @FXML
    private MFXComboBox endTimeComboBox;
    @FXML
    private HBox employeePickerContainer;
    @FXML
    private MFXFilterComboBox<Employee> employeeComboBox;
    Facade facade = App.getSingleton().getFacade();
    LayoutController layout = new LayoutController();


    private final ObservableList<String> timeList = FXCollections.observableArrayList("12:00am", "12:30am", "1:00am", "1:30am",
            "2:00am", "2:30am",
            "3:00am", "3:30am",
            "4:00am", "4:30am",
            "5:00am", "5:30am",
            "6:00am", "6:30am",
            "7:00am", "7:30am",
            "8:00am", "8:30am",
            "9:00am", "9:30am",
            "10:00am", "10:30am",
            "11:00am", "11:30am",
            "12:00pm", "12:30pm",
            "12:00pm", "12:30pm",
            "1:00pm", "1:30pm",
            "2:00pm", "2:30pm",
            "3:00pm", "3:30pm",
            "4:00pm", "4:30pm",
            "5:00pm", "5:30pm",
            "6:00pm", "6:30pm",
            "7:00pm", "7:30pm",
            "8:00pm", "8:30pm",
            "9:00pm", "9:30pm",
            "10:00pm", "10:30pm",
            "11:00pm", "11:30pm");

    public void initialize() {
        App.getSingleton().getExecutorService().execute( () -> {
            ObservableList<Employee> employees = facade.getAllAsListEmployee();

            var employeeToName = new StringConverter<Employee>() {

                @Override
                public String toString(Employee employee) {
                    if (employee == null) return "";
                    return employee.getFirstName() + " " + employee.getLastName();
                }

                @Override
                public Employee fromString(String string) {
                    return null;
                }
            };
            employeeComboBox.setConverter(employeeToName);

            AdminTable alertTable = new AdminTable("Alert", TableType.ALERT, facade::getAllAsListAlert);
            alertTable.init();

            List<Alert> allAlerts = App.getSingleton().getFacade().getAllAsListAlert();
            List<Alert> alerts = new ArrayList<>();
            ArrayList<PFXAlertCard> alertCards = new ArrayList<>();
            ArrayList<Integer> indexes = new ArrayList<>();
            ObservableList<String> alertTypeList = FXCollections.observableArrayList();

            alertTypeList.addAll("None", "Map", "Map Disabled", "Employee", "Admin", "Service Request");
            alertTypeComboBox.setItems(alertTypeList);
            endTimeComboBox.setItems(timeList);
            employeeComboBox.setItems(employees);
            timePickerContainer.setVisible(false);
            timePickerContainer.setManaged(false);
            employeePickerContainer.setVisible(false);
            employeePickerContainer.setManaged(false);

            activeAlertsContainer.setStyle("-fx-spacing: 15");

            alertTypeComboBox.setOnAction(e -> {
                alerts.clear();
                alertCards.clear();
                activeAlertsContainer.getChildren().clear();
                for(Alert alert: allAlerts)
                {
                    if (alert.getAlertType().toString().toLowerCase().replace('_', ' ').equals(alertTypeComboBox.getSelectedItem().toString().toLowerCase())) {
                        PFXAlertCard alertCard = new PFXAlertCard(alert);
                        activeAlertsContainer.getChildren().add(alertCard);
                        alertCards.add(alertCard);
                        alerts.add(alert);
                    }

                    timePickerContainer.setVisible(false);
                    timePickerContainer.setManaged(false);

                    if (alertTypeComboBox.getSelectedItem().equals("Map") || alertTypeComboBox.getSelectedItem().equals("Map Disabled")) {
                        timePickerContainer.setVisible(true);
                        timePickerContainer.setManaged(true);
                    }
                    if (alertTypeComboBox.getSelectedItem().equals("Employee")){
                        employeePickerContainer.setVisible(true);
                        employeePickerContainer.setManaged(true);
                    }
                }});






            Platform.runLater( () -> {


                removeButton.setOnAction(e -> {
                    int i = 0;
                    for (PFXAlertCard card: alertCards) {
                        if (card.getIsRead()) {
                            activeAlertsContainer.getChildren().remove(card);
                            indexes.add(i);
                        }

                        for (Integer num : indexes) {
                            allAlerts.remove(alertCards.get(i).getAlert());
                            facade.deleteAlert(alertCards.get(i).getAlert());
                            alertCards.remove(num);
                            alerts.remove(num);
                        }
                        i++;
                    }
                });

                sendButton.setOnAction(e -> {
                    Instant dateTime = Instant.now();
                    Alert alert = new Alert();
                    Alert.AlertType alertType = Alert.AlertType.NONE;
                    String time = "";
                    String date = "";
                    if (alertTypeComboBox.getSelectedItem().equals("Map")) {
                        date = endDatePicker.getText();
                        time = endTimeComboBox.getText();
                        alertType = Alert.AlertType.MAP;
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.parse(convertDateTime(date, time))).employeeID(App.getSingleton().getAccount().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();

                    } else if (alertTypeComboBox.getSelectedItem().equals("Map Disabled")) {
                        alertType = Alert.AlertType.MAP_DISABLED;
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.now()).employeeID(App.getSingleton().getAccount().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();

                    } else if (alertTypeComboBox.getSelectedItem().equals("Employee")) {
                        alertType = Alert.AlertType.EMPLOYEE;
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.now()).employeeID(employeeComboBox.getSelectedItem().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();

                    } else if (alertTypeComboBox.getSelectedItem().equals("Admin")) {
                        alertType = Alert.AlertType.ADMIN;
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.now()).employeeID(App.getSingleton().getAccount().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();

                    } else if (alertTypeComboBox.getSelectedItem().equals("Service Request")) {
                        alertType = Alert.AlertType.SERVICE_REQUEST;
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.now()).employeeID(App.getSingleton().getAccount().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();

                    } else {
                        alert = Alert.builder().uuid(UUID.randomUUID()).alertType(alertType).alertTitle(alertTitle.getText()).description(alertDescription.getText()).startDate(dateTime).readStatus(Alert.ReadStatus.UNREAD).startDate(Instant.now()).endDate(Instant.now()).employeeID(App.getSingleton().getAccount().getEmployeeID()).readStatus(Alert.ReadStatus.UNREAD).build();
                    }




                    facade.saveAlert(alert);
                    alerts.add(alert);
                    PFXAlertCard alertCard = new PFXAlertCard(alert);
                    activeAlertsContainer.getChildren().add(alertCard);
                    alertCards.add(alertCard);
                });
            });
        });
    }

    public String convertDateTime(String date, String time) {
        String returnString;
        DateTimeFormatter dateInputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        DateTimeFormatter dateOutputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate tempDate = LocalDate.parse(date, dateInputFormatter);
        returnString = tempDate.format(dateOutputFormatter);

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("h:mma");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime tempTime = LocalTime.parse(time.toUpperCase(), inputFormatter);
        String tempTimeS = tempTime.format(outputFormatter);

        returnString = returnString + "T" + tempTimeS + ".00Z";

        return returnString;
    }
}
