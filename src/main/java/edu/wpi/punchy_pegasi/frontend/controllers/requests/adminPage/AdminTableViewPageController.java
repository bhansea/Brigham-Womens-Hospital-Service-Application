package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Employee;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;


public class AdminTableViewPageController {

    private final Facade facade = App.getSingleton().getFacade();
    private final Map<String, AdminTable> tables = new LinkedHashMap<>() {{
        put("Node", new AdminTable<>("Node", TableType.NODES, () -> facade.getAllNode().values().stream().toList()));
        put("Location", new AdminTable<>("Location", TableType.LOCATIONNAMES, () -> facade.getAllLocationName().values().stream().toList()));
        put("Edge", new AdminTable<>("Edge", TableType.EDGES, () -> facade.getAllEdge().values().stream().toList()));
        put("Move", new AdminTable<>("Move", TableType.MOVES, () -> facade.getAllMove().values().stream().toList()));
        put("Employee", new AdminTable<>("Employee", TableType.EMPLOYEES, () -> facade.getAllEmployee().values().stream().toList()));
        put("Conference", new AdminTable<>("Conference Room Service Request", TableType.CONFERENCEREQUESTS, () -> facade.getAllConferenceRoomEntry().values().stream().toList()));
        put("Office", new AdminTable<>("Office Supplies Service Request", TableType.OFFICEREQUESTS, () -> facade.getAllOfficeServiceRequestEntry().values().stream().toList()));
        put("Furniture", new AdminTable<>("Furniture Service Request", TableType.FURNITUREREQUESTS, () -> facade.getAllFurnitureRequestEntry().values().stream().toList()));
        put("Food", new AdminTable<>("Food Service Request", TableType.FOODREQUESTS, () -> facade.getAllFoodServiceRequestEntry().values().stream().toList()));
        put("Flower", new AdminTable<>("Flower Service Request", TableType.FLOWERREQUESTS, () -> facade.getAllFlowerDeliveryRequestEntry().values().stream().toList()));
    }};
    public VBox viewTablePageContainer;
    @FXML
    Button displayButton;
    @FXML
    MFXComboBox<String> displayTableTypeComboBox;

    MFXComboBox<String> statusType = new MFXComboBox<>();
    @FXML
    private VBox tableContainer;
    @FXML
    private Label employeeId = new Label();
    @FXML
    private Label firstName = new Label();
    @FXML
    private Label lastName = new Label();

    @FXML
    private TextField employeeIdTextField = new TextField();

    @FXML
    private TextField firstNameField = new TextField();
    @FXML
    private TextField lastNameField = new TextField();

    @FXML
    private VBox employeeContainer = new VBox();

    @FXML
    private VBox requestEditContainer = new VBox();
    @FXML
    PFXButton submitRequestEditButton = new PFXButton("Submit Changes");
    @FXML
    PFXButton submitEmployeeEditButton = new PFXButton("Submit Changes");

    @FXML
    PFXButton removeButton = new PFXButton("Remove");


    @FXML
    private VBox editContainer;
    private AdminTable currentTable;
    PdbController pdb = App.getSingleton().getPdb();

    public void initialize() {
        ObservableList<String> displayTableTypeList = FXCollections.observableArrayList();
        tables.values().forEach(f -> {
            displayTableTypeList.add(f.humanReadableName);
        });
        displayTableTypeComboBox.setItems(displayTableTypeList);

        ObservableList<String> statusTypeList = FXCollections.observableArrayList("NONE", "PROCESSING", "DONE");
        statusType.setItems(statusTypeList);
        initTables();

        displayButton.setOnAction(e -> {
            String name = displayTableTypeComboBox.getSelectedItem();
            tables.values().stream().filter(f -> Objects.equals(f.humanReadableName, name)).forEach(f -> {
                if (f.humanReadableName.toLowerCase().contains("request")) {
                    requestEditContainer.setVisible(true);
                    requestEditContainer.setManaged(true);
                    employeeContainer.setVisible(false);
                    employeeContainer.setManaged(false);
                } else if (f.humanReadableName.toLowerCase().contains("employee")) {
                    requestEditContainer.setVisible(false);
                    requestEditContainer.setManaged(false);
                    employeeContainer.setVisible(true);
                    employeeContainer.setManaged(true);
                } else {
                    requestEditContainer.setVisible(false);
                    requestEditContainer.setManaged(false);
                    employeeContainer.setVisible(false);
                    employeeContainer.setManaged(false);
                }
                showTable(f);
            });
        });

        employeeId.setText("Employee ID");
        firstName.setText("First Name");
        lastName.setText("Last name");

        employeeContainer.getChildren().add(firstName);
        employeeContainer.getChildren().add(firstNameField);
        employeeContainer.getChildren().add(lastName);
        employeeContainer.getChildren().add(lastNameField);
        employeeContainer.getChildren().add(submitEmployeeEditButton);
        employeeContainer.getChildren().add(removeButton);


        requestEditContainer.getChildren().add(statusType);
        requestEditContainer.getChildren().add(submitRequestEditButton);

        employeeContainer.setVisible(false);
        employeeContainer.setManaged(false);
        requestEditContainer.setVisible(false);
        requestEditContainer.setManaged(false);

        editContainer.getChildren().add(employeeContainer);
        editContainer.getChildren().add(requestEditContainer);
        editContainer.getStyleClass().add("admin-edit-container");

        tables.values().forEach(t -> {
            t.setRowClicked(r -> {
                if (r instanceof RequestEntry) {
                    RequestEntry entry = (RequestEntry) r;
                    submitRequestEditButton.setOnAction(e -> {
                        RequestEntry.Status status = null;
                        if (statusType.getSelectedItem() == "NONE") {
                            status = RequestEntry.Status.NONE;
                        } else if (statusType.getSelectedItem() == "PROCESSING") {
                            status = RequestEntry.Status.PROCESSING;
                        } else if (statusType.getSelectedItem() == "DONE") {
                            status = RequestEntry.Status.DONE;
                        }
                        if (status == null) return;
                        entry.setStatus(status);
                        facade.updateRequestEntry(entry, new RequestEntry.Field[]{RequestEntry.Field.STATUS});
                        t.reload();
                        t.table.update();
                    });
                } else if (r instanceof Employee) {
                    Employee employee = (Employee) r;
                    firstNameField.setText(employee.getFirstName());
                    lastNameField.setText(employee.getLastName());
                    submitEmployeeEditButton.setOnAction(e -> {
                        var em = new Employee(((Employee) r).getEmployeeID(), firstNameField.getText(), lastNameField.getText());
                        facade.updateEmployee(em, new Employee.Field[]{Employee.Field.FIRST_NAME, Employee.Field.LAST_NAME});
                        t.reload();
                        t.table.update();
                    });
                }
            });
        });

        submitEmployeeEditButton.setOnAction(e -> {
            if (currentTable.getRowClicked() instanceof Employee) {
                // submit changes to edit a employee, or add new employee

            }
        });

        submitRequestEditButton.setOnAction(e -> {
            if (currentTable.getRowClicked() instanceof Employee) {
                // submit changes to edit a employee, or add new employee

            }
        });

        removeButton.setOnAction(e -> {
            if (currentTable.getRowClicked() instanceof Employee) {
                // remove table
            }

        });

        viewTablePageContainer.getStyleClass().add("admin-table-choose-container");
    }


    public void showTable(AdminTable tableType) {
        currentTable = tableType;
        currentTable.getTable().setVisible(true);
        currentTable.getTable().setManaged(true);

        tables.values().stream().filter(f -> !Objects.equals(f.tableType, tableType.tableType)).forEach(f -> {
            f.getTable().setVisible(false);
            f.getTable().setManaged(false);
        });
    }

    public void initTables() {
        tables.values().forEach(AdminTable::init);
        tableContainer.getChildren().addAll(tables.values().stream().map(AdminTable::getTable).toList());
        showTable(tables.get("Node"));
    }


}
