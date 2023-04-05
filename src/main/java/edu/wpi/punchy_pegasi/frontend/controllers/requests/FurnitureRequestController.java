package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import org.controlsfx.control.CheckComboBox;

import java.net.URL;

public class FurnitureRequestController extends RequestController<FurnitureRequestEntry> {

    @FXML
    CheckComboBox<String> selectFurniture;

    public static BorderPane create(URL path) {
        return RequestController.create(new FurnitureRequestController(), path);
    }

    @FXML
    public void init(){
        ObservableList<String> furnitureList =
                FXCollections.observableArrayList("Desk","Bed","Lamp","Chair",
                "Office Chair","Pillow", "Picture Frame", "Rug", "Bench", "Blanket", "Night Stand");
        selectFurniture.getItems().addAll(furnitureList);
        submit.setDisable(true);
    }

    //@FXML
    //public

    @Override
    protected void clearEntry() {
        clearGeneric();
        for (int i = 0; i < selectFurniture.getItems().size(); i++) {
            selectFurniture.getItemBooleanProperty(i).set(false);
        }
    }

    @FXML
    public void submitEntry(){
        requestEntry = new FurnitureRequestEntry(
                roomNumber.getText(),
                staffAssignment.getText(),
                additionalNotes.getText(),
                selectFurniture.getItems());
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry(){
        boolean val = validateGeneric() || selectFurniture.getItems() == null;
        submit.setDisable(val);
    }

}
