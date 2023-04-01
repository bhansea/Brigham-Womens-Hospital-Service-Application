package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;

import java.util.ArrayList;

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> {
    FoodServiceRequestEntry entry;
    @FXML TextField dietaryRestrictions;
    @FXML TextField patientName;
    @FXML TextField roomNumber;
    @FXML TextField additionalNotes;
    @FXML TextField staffAssignment;
    @FXML RadioButton hot;
    @FXML RadioButton warm;
    @FXML RadioButton cold;
    @FXML CheckBox utensils;
    @FXML CheckBox napkins;
    @FXML CheckBox glass;
    @FXML MFXComboBox<String> mealDropdown;
    @FXML Button submit;
    @FXML ToggleGroup temp;

    public static BorderPane create() {
        return RequestController.create(new FoodServiceRequestController(), "views/FoodServiceRequest.fxml");
    }

    @FXML
    public void init() {
        ObservableList<String> mealList =
                FXCollections.observableArrayList(
                        "Mac and Cheese", "Steak", "Chicken and Rice", "Meatloaf");
        mealDropdown.setItems(mealList);
        submit.setDisable(true);
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
        if (this.checkSumbit())
            return;
        requestEntry =

        entry =
                new FoodServiceRequestEntry(
                        patientName.getText(), roomNumber.getText(), staffAssignment.getText(), additionalNotes.getText(), mealDropdown.getSelectedItem(), ((RadioButton) temp.getSelectedToggle()).getId(), extras, dietaryRestrictions.getText());
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry() {
        boolean validate = patientName.getText().isBlank()
                || patientName.getText().isBlank()
                || roomNumber.getText().isBlank()
                || staffAssignment.getText().isBlank()
                || additionalNotes.getText().isBlank()
                || dietaryRestrictions.getText().isBlank()
                || temp.getSelectedToggle() == null
                || mealDropdown.getSelectedItem() == null;
        submit.setDisable(validate);
    }
    @FXML
    public void clearEntry() {
        mealDropdown.clear();
        patientName.clear();
        roomNumber.clear();
        staffAssignment.clear();
        additionalNotes.clear();
        dietaryRestrictions.clear();
        napkins.setSelected(false);
        utensils.setSelected(false);
        glass.setSelected(false);
        hot.setSelected(false);
        warm.setSelected(false);
        cold.setSelected(false);
    }
}
