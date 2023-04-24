package edu.wpi.punchy_pegasi.frontend.controllers.requests.adminPage;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.w3c.dom.Text;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Long.parseLong;


public class AdminTablePageController {

    private final Facade facade = App.getSingleton().getFacade();
    private final Map<String, AdminTable> tables = new LinkedHashMap<>() {{
        put("Node", new AdminTable<>("Node", TableType.NODES, facade::getAllAsListNode));
        put("Location", new AdminTable<>("Location", TableType.LOCATIONNAMES, facade::getAllAsListLocationName));
        put("Edge", new AdminTable<>("Edge", TableType.EDGES, facade::getAllAsListEdge));
        put("Move", new AdminTable<>("Move", TableType.MOVES, facade::getAllAsListMove));
        put("Employee", new AdminTable<>("Employee", TableType.EMPLOYEES, facade::getAllAsListEmployee));
        put("Account", new AdminTable<>("Account", TableType.ACCOUNTS, facade::getAllAsListAccount));
        put("Conference", new AdminTable<>("Conference Room Service Request", TableType.CONFERENCEREQUESTS, facade::getAllAsListConferenceRoomEntry));
        put("Office", new AdminTable<>("Office Supplies Service Request", TableType.OFFICEREQUESTS, facade::getAllAsListOfficeServiceRequestEntry));
        put("Furniture", new AdminTable<>("Furniture Service Request", TableType.FURNITUREREQUESTS, facade::getAllAsListFurnitureRequestEntry));
        put("Food", new AdminTable<>("Food Service Request", TableType.FOODREQUESTS, facade::getAllAsListFoodServiceRequestEntry));
        put("Flower", new AdminTable<>("Flower Service Request", TableType.FLOWERREQUESTS, facade::getAllAsListFlowerDeliveryRequestEntry));
    }};
    public PFXButton importButton;
    public PFXButton exportButton;
    public PFXButton clearButton;
    @FXML
    private MFXComboBox<String> displayTableTypeComboBox;
    @FXML
    private VBox tableContainer;
    @FXML
    private PFXButton submitEditButton = new PFXButton("Submit Changes");
    @FXML
    private PFXButton addButton = new PFXButton("Add New Entry");
    @FXML
    private PFXButton removeButton = new PFXButton("Remove");
    @FXML
    private FlowPane editContainer;
    private AdminTable currentTable;

    private ArrayList<TextArea> textAreas = new ArrayList<>();

    // import export stuff
    private String filePath = "";
    private File selectedFile = new File("");
    private File selectedDir = new File("");
    @FXML
    private Label fileText = new Label();

    FileChooser fileChooser = new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    PdbController pdb = App.getSingleton().getPdb();

    public void initialize() {
        fileChooser.setTitle("File Chooser");

        ObservableList<String> displayTableTypeList = FXCollections.observableArrayList();
        tables.values().forEach(f -> {
            displayTableTypeList.add(f.humanReadableName);
        });
        displayTableTypeComboBox.setItems(displayTableTypeList);

        initTables();
        displayEditComponent();


        displayTableTypeComboBox.setOnAction(e -> {
            String name = displayTableTypeComboBox.getSelectedItem();
            tables.values().stream().filter(f -> Objects.equals(f.humanReadableName, name)).forEach(f -> {
                showTable(f);
                currentTable = f;
            });
            displayEditComponent();
        });

        tables.values().forEach(t -> {
            t.setRowClicked(r -> {
                textAreas.clear();
                int componentIndex = 1;
                int fieldIndex = 0;

                for (Object field : t.getTableType().getFieldEnum().getEnumConstants()) {
                    var iField = (IField) field;
                    textAreas.add((TextArea)editContainer.getChildren().get(componentIndex));
                    textAreas.get(fieldIndex).setText(iField.getValue(r).toString());
                    componentIndex+=2;
                    fieldIndex++;
                }
            });
        });

        submitEditButton.setOnAction(e -> {
            if (currentTable.humanReadableName.toLowerCase().contains("request")) {
                RequestEntry.Status status = null;
                        if (textAreas.get(4).getText() == "NONE") {
                            status = RequestEntry.Status.NONE;
                        } else if (textAreas.get(4).getText() == "PROCESSING") {
                            status = RequestEntry.Status.PROCESSING;
                        } else if (textAreas.get(4).getText() == "DONE") {
                            status = RequestEntry.Status.DONE;
                        }
                var requestEntry = new RequestEntry(textAreas.get(0).getText(), Long.parseLong(textAreas.get(1).getText()), Long.parseLong(textAreas.get(2).getText()), textAreas.get(3).getText(), status, Long.parseLong(textAreas.get(5).getText()));
                facade.updateRequestEntry(requestEntry, new RequestEntry.Field[]{RequestEntry.Field.SERVICE_ID, RequestEntry.Field.LOCATION_NAME, RequestEntry.Field.STAFF_ASSIGNMENT, RequestEntry.Field.ADDITIONAL_NOTES, RequestEntry.Field.STATUS, RequestEntry.Field.EMPLOYEE_ID});
                currentTable.reload();
                currentTable.table.update();
            }
            else if (currentTable.humanReadableName.toLowerCase().contains("employee")) {
                var employee = new Employee(Long.parseLong(textAreas.get(0).getText()), textAreas.get(1).getText(), textAreas.get(2).getText());
                facade.updateEmployee(employee, new Employee.Field[]{Employee.Field.EMPLOYEE_ID, Employee.Field.FIRST_NAME, Employee.Field.LAST_NAME});
                currentTable.reload();
                currentTable.table.update();
            } else if (currentTable.humanReadableName.toLowerCase().contains("account")) {
                Account.AccountType status = null;
                if (textAreas.get(4).getText() == "NONE") {
                    status = Account.AccountType.NONE;
                } else if (textAreas.get(4).getText() == "ADMIN") {
                    status = Account.AccountType.ADMIN;
                } else if (textAreas.get(4).getText() == "STAFF") {
                    status = Account.AccountType.STAFF;
                }
                var account = new Account(Long.parseLong(textAreas.get(0).getText()), textAreas.get(1).getText(),textAreas.get(2).getText(), Long.parseLong(textAreas.get(3).getText()), status);
                facade.updateAccount(account, new Account.Field[]{Account.Field.UUID, Account.Field.USERNAME, Account.Field.PASSWORD, Account.Field.EMPLOYEE_ID, Account.Field.ACCOUNT_TYPE});
                currentTable.reload();
                currentTable.table.update();

            } else if (currentTable.humanReadableName.toLowerCase().contains("node")) {
                var node = new Node(Long.parseLong(textAreas.get(0).getText()), Integer.parseInt(textAreas.get(1).getText()), Integer.parseInt(textAreas.get(2).getText()), textAreas.get(3).getText(), textAreas.get(0).getText());
                facade.updateNode(node, new Node.Field[]{Node.Field.NODE_ID, Node.Field.XCOORD, Node.Field.YCOORD, Node.Field.FLOOR, Node.Field.BUILDING});
                currentTable.reload();
                currentTable.table.update();

            } else if (currentTable.humanReadableName.toLowerCase().contains("edge")) {
                var edge = new Edge(Long.parseLong(textAreas.get(0).getText()), Long.parseLong(textAreas.get(1).getText()), Long.parseLong(textAreas.get(2).getText()));
                facade.updateEdge(edge, new Edge.Field[]{Edge.Field.UUID, Edge.Field.START_NODE, Edge.Field.END_NODE});
                currentTable.reload();
                currentTable.table.update();

            } else if (currentTable.humanReadableName.toLowerCase().contains("move")) {
                var move = new Move(Long.parseLong(textAreas.get(0).getText()), Long.parseLong(textAreas.get(1).getText()), Long.parseLong(textAreas.get(2).getText()), textAreas.get(3).getText());
                facade.updateMove(move, new Move.Field[]{Move.Field.UUID, Move.Field.NODE_ID, Move.Field.LOCATION_ID, Move.Field.DATE});
                currentTable.reload();
                currentTable.table.update();

            } else if (currentTable.humanReadableName.toLowerCase().contains("location")) {
                var edge = new Edge(Long.parseLong(textAreas.get(0).getText()), Long.parseLong(textAreas.get(1).getText()), Long.parseLong(textAreas.get(2).getText()));
                facade.updateEdge(edge, new Edge.Field[]{Edge.Field.UUID, Edge.Field.START_NODE, Edge.Field.END_NODE});
                currentTable.reload();
                currentTable.table.update();

            }
        });

//        if (r instanceof RequestEntry) {
//                    RequestEntry entry = (RequestEntry) r;
//                    submitRequestEditButton.setOnAction(e -> {
//                        RequestEntry.Status status = null;
//                        if (statusType.getSelectedItem() == "NONE") {
//                            status = RequestEntry.Status.NONE;
//                        } else if (statusType.getSelectedItem() == "PROCESSING") {
//                            status = RequestEntry.Status.PROCESSING;
//                        } else if (statusType.getSelectedItem() == "DONE") {
//                            status = RequestEntry.Status.DONE;
//                        }
//                        if (status == null) return;
//                        entry.setStatus(status);
//                        facade.updateRequestEntry(entry, new RequestEntry.Field[]{RequestEntry.Field.STATUS});
////                        t.reload();
//                        t.table.update();
//                    });
//                } else if (r instanceof Employee) {
//                    Employee employee = (Employee) r;
//                    firstNameField.setText(employee.getFirstName());
//                    lastNameField.setText(employee.getLastName());
//                    submitEmployeeEditButton.setOnAction(e -> {
//                        var em = new Employee(((Employee) r).getEmployeeID(), firstNameField.getText(), lastNameField.getText());
//                        facade.updateEmployee(em, new Employee.Field[]{Employee.Field.FIRST_NAME, Employee.Field.LAST_NAME});
////                        t.reload();
//                        t.table.update();
//                    });
//                }



        importButton.setOnAction(e -> {
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            selectedFile = fileChooser.showOpenDialog(App.getSingleton().getPopupStage());

            if (selectedFile != null && displayTableTypeComboBox.getSelectedItem() != null) {
                filePath = selectedFile.getAbsolutePath();
                if (filePath.contains("Node.csv") || filePath.contains("Move.csv") || filePath.contains("LocationName.csv") || filePath.contains("Edge.csv")) {
                    fileText.setText(filePath);
                    try {
                        pdb.importTable(currentTable.tableType, filePath);
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        exportButton.setOnAction(e -> {
            selectedDir = directoryChooser.showDialog(App.getSingleton().getPopupStage());
            if (selectedDir != null) {
                fileText.setText(selectedDir.getAbsolutePath());
                try {
                    pdb.exportTable(selectedDir + "\\" + currentTable.humanReadableName + ".csv", currentTable.tableType);
                    selectedDir = null;
                } catch (PdbController.DatabaseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        clearButton.setOnAction(e -> {
//            pdb.deleteQuery(currentTable, );
        });


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

    public void displayEditComponent() {
        HBox hbox = new HBox();
        editContainer.getChildren().clear();
        for (Object field : currentTable.tableType.getFieldEnum().getEnumConstants()) {
            Label fieldLabel = new Label();
            TextArea fieldArea = new TextArea();

            fieldLabel.setText(field.toString());
            editContainer.getChildren().add(fieldLabel);
            editContainer.getChildren().add(fieldArea);
        }
        editContainer.getChildren().add(hbox);
        editContainer.getStyleClass().add("admin-edit-container");
    }



}
