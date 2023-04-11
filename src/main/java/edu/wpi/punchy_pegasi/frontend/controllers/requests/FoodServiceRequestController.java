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

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> implements PropertyChangeListener {
    FoodServiceRequestEntry entry;
    @FXML
    TextField dietaryRestrictions;
    @FXML
    CheckBox utensils, napkins, glass;
    @FXML
    MFXComboBox<String> mealDropdown;
    @FXML
    ToggleGroup temp;
    @FXML
    Label mealName;

    TextField patientName = new TextField();
    Label price = new Label("$0.00");

    public static BorderPane create(String path) {
        return RequestController.create(new FoodServiceRequestController(), path);
    }

    @FXML
    public void init() {
        ObservableList<String> mealList = FXCollections.observableArrayList("Mac and Cheese", "Steak", "Chicken and Rice", "Meatloaf");
        mealDropdown.setItems(mealList);
        addTextField(patientName);
        addTotal(price);
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

        //makes sure shared fields aren't empty
        requestEntry = entry = new FoodServiceRequestEntry(patientName.getText(), roomNumber.getText(), staffAssignment.getText(), additionalNotes.getText(), mealDropdown.getSelectedItem(), extras, ((RadioButton) temp.getSelectedToggle()).getId(), dietaryRestrictions.getText());
        App.getSingleton().getFacade().saveFoodServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || temp.getSelectedToggle() == null || mealDropdown.getSelectedItem() == null;
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
        temp.selectToggle(null);
    }

    @FXML
    public void updateLabelEntry() {
        validateEntry();
        mealName.setText(mealDropdown.getSelectedItem());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
