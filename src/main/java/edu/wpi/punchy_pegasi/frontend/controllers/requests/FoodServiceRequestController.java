package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.FoodServiceRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> implements PropertyChangeListener {
    FoodServiceRequestEntry entry;
    @FXML
    TextArea dietaryRestrictions;
    @FXML
    MFXComboBox<String> mealDropdown, tempDropdown, beverageDropdown;
    @FXML
    CheckBox utensils, napkins, straw;

    TextField patientName = new TextField();
    Label price = new Label("$0.00");

    public static BorderPane create(String path) {
        return RequestController.create(new FoodServiceRequestController(), path);
    }

    @FXML
    public void init() {
        ObservableList<String> mealList = FXCollections.observableArrayList("Mac and Cheese", "Steak", "Chicken and Rice", "Meatloaf");
        mealDropdown.setItems(mealList);
        ObservableList<String> beverageList = FXCollections.observableArrayList("Water", "Coffee", "Lemonade", "Milk", "Vitamin Water", "Dr. Pepper", "Chocolate Milk", "Apple Juice", "Orange Juice", "Cranberry Juice");
        beverageDropdown.setItems(beverageList);
        ObservableList<String> tempType = FXCollections.observableArrayList("Hot", "Warm", "Cold");
        tempDropdown.setItems(tempType);
        addTextField(patientName);
        addLabel(price);
        setHeaderText("Food Service Request");
        submit.setDisable(true);
        this.addPropertyChangeListener(this);
    }

    @FXML
    public void submitEntry() {
        ArrayList<String> extras = new ArrayList<String>();
        if (utensils.isSelected()) {
            extras.add("utensils");
        }
        if (napkins.isSelected()) {
            extras.add("napkins");
        }
        if (straw.isSelected()) {
            extras.add("straw");
        }

        //makes sure shared fields aren't empty
        requestEntry = entry = new FoodServiceRequestEntry(
                locationName.getItems().get(0).getUuid(),
                staffAssignment.getItems().get(0).getEmployeeID(),
                additionalNotes.getText(),
                mealDropdown.getSelectedItem(),
                ((RadioButton) temp.getSelectedToggle()).getId(),
                extras,
                beverageDropdown.getSelectedItem(),
                dietaryRestrictions.getText(),
                patientName.getText());
        App.getSingleton().getFacade().saveFoodServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || mealDropdown.getSelectedItem() == null || tempDropdown.getSelectedItem() == null;
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        mealDropdown.clear();
        patientName.clear();
        dietaryRestrictions.clear();
        tempDropdown.clear();
        napkins.setSelected(false);
        utensils.setSelected(false);
        straw.setSelected(false);
        beverageDropdown.clear();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
