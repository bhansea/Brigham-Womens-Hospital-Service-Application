package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> implements PropertyChangeListener {
    FoodServiceRequestEntry entry;
    @FXML
    TextField dietaryRestrictions;
    @FXML
    TextField patientName;
    @FXML
    TextField roomNumber;
    @FXML
    TextField additionalNotes;
    @FXML
    RadioButton hot;
    @FXML
    RadioButton warm;
    @FXML
    RadioButton cold;
    @FXML
    CheckBox utensils;
    @FXML
    CheckBox napkins;
    @FXML
    CheckBox glass;
    @FXML
    MFXComboBox<String> mealDropdown;
    @FXML
    Button submit;
    @FXML
    ToggleGroup temp;

    public static BorderPane create() {
        var cont = new FoodServiceRequestController();
        return RequestController.create(new FoodServiceRequestController(), "views/FoodServiceRequest.fxml");
    }

    @FXML
    public void init() {
        ObservableList<String> mealList = FXCollections.observableArrayList("Mac and Cheese", "Steak", "Chicken and Rice", "Meatloaf");
        mealDropdown.setItems(mealList);
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
        if (glass.isSelected()) {
            extras.add("glass");
        }
        String restrictions;
        try {
            restrictions = dietaryRestrictions.getText();
        } catch (NullPointerException e) {
            restrictions = "";
        }

        //makes sure shared fields aren't empty
        requestEntry = entry = new FoodServiceRequestEntry(patientName.getText(), roomNumber.getText(), additionalNotes.getText(), mealDropdown.getSelectedItem(), ((RadioButton) temp.getSelectedToggle()).getId(), extras, dietaryRestrictions.getText());
        Navigation.navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || dietaryRestrictions.getText().isBlank() || temp.getSelectedToggle() == null || mealDropdown.getSelectedItem() == null;
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        mealDropdown.clear();
        dietaryRestrictions.clear();
        napkins.setSelected(false);
        utensils.setSelected(false);
        glass.setSelected(false);
        hot.setSelected(false);
        warm.setSelected(false);
        cold.setSelected(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
