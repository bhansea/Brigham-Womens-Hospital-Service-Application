package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.generated.FlowerDeliveryRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;


public class AdminPageController {

    @FXML MFXComboBox<String> tableTypesComboBox;
    @FXML MFXTableView<FlowerDeliveryRequestEntry> serviceRequestTable;
    @FXML
    MFXTextField servSearchBar;

    @FXML
    Button submit;

    @FXML
    Button importButton;
    @FXML
    Button exportButton;

    @FXML
    Label fileText;

    String filePath = "";
    File selectedFile = new File("");
    File selectedDir = new File("");



    // @FXML MFXButton back;
    @Getter
    @Setter
    private ArrayList<String> requests = new ArrayList<>(); //store requests in list to search through

    public void initialize() {
        final PdbController pdb = App.getSingleton().getPdb();

        ObservableList<String> tableTypes = FXCollections.observableArrayList("Nodes", "Edges", "Moves", "Location Names");
        tableTypesComboBox.setItems(tableTypes);
        try {
            pdb.initTableByType(TableType.NODES);
//            pdb.initTableByType(TableType.EDGES);
            pdb.initTableByType(TableType.LOCATIONNAMES);
            pdb.initTableByType(TableType.MOVES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }

        //flowerTypeComboBox.setItems(flowerTypesList);
//        FlowerDeliveryRequestEntryDaoImpl FlowerDaoImp = new FlowerDeliveryRequestEntryDaoImpl();
//        Object[] keySet = FlowerDaoImp.getAll().keySet().toArray();
//        Object[] entrySet = FlowerDaoImp.getAll().entrySet().toArray();
//
//
//        for (Object entry: entrySet) {
//            MFXTableColumn<String> col = new MFXTableColumn<String>();
////            serviceRequestTable.getTableColumns().addAll(col);
//        }
//        ObservableList<FlowerDeliveryRequestEntry> flowerReqEntryList = FXCollections.observableArrayList();
//
////        serviceRequestTable.setItems();

        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        fileChooser.setTitle("File Chooser");
//        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", ".csv"));

//        selectFileButton.setOnAction(e -> {
//            selectedFile = fileChooser.showOpenDialog(stage);
//            filePath = selectedFile.getAbsolutePath();
//            fileText.setText(filePath);
//        });

        importButton.setOnAction(e -> {

            selectedFile = fileChooser.showOpenDialog(stage);
            filePath = selectedFile.getAbsolutePath();
            fileText.setText(filePath);

            while(selectedFile != null) {
                if (tableTypesComboBox.getSelectedItem().equals("Nodes")) {
                    try {
                        pdb.importTable(TableType.NODES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Edges")) {
                    try {
                        pdb.importTable(TableType.EDGES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Moves")) {
                    try {
                        pdb.importTable(TableType.MOVES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Location Names")) {
                    try {
                        pdb.importTable(TableType.LOCATIONNAMES, filePath);
                        selectedFile = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        exportButton.setOnAction(e -> {
            selectedDir = directoryChooser.showDialog(stage);
            fileText.setText(selectedDir.getAbsolutePath());

            while (selectedDir != null) {
                if (tableTypesComboBox.getSelectedItem().equals("Nodes")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Nodes.csv", TableType.NODES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Edges")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Edges.csv", TableType.EDGES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Moves")) {
                    try {
                        pdb.exportTable(selectedDir + "\\Moves.csv", TableType.MOVES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                } else if (tableTypesComboBox.getSelectedItem().equals("Location Names")) {
                    try {
                        pdb.exportTable(selectedDir + "\\LocationName.csv", TableType.LOCATIONNAMES);
                        selectedDir = null;
                        break;
                    } catch (PdbController.DatabaseException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }

        });
    }

    @FXML
    String showReq() {
        String temp = "";
        if (submit.isPressed()) {
            temp = servSearchBar.getText();
            for (int i = 0; i < requests.size(); i++) { //does not loop because theres nothing in requests so no size
                //so i++ is never used as well
                if (temp.matches(requests.get(i))) {
                    return requests.get(i);
                } else {
                    throw new RuntimeException("No such request found.");
                }
            }
        }

        //match case
        //throw error if no request found
        //display request
        //display back button/somehow reset the search bar for a new search?

        return temp;


    }
}
