package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class FoodServiceRequestController {
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

    @FXML
    public void initialize() {
        ObservableList<String> mealList =
                FXCollections.observableArrayList(
                        "Mac and Cheese", "Steak", "Chicken and Rice", "Meatloaf");
        mealDropdown.setItems(mealList);
    }

    @FXML
    public void submitEntry() {
        String tempType = "";
        ArrayList<String> extras = new ArrayList<String>();
        if (hot.isSelected()) {
            tempType = "hot";
        } else if (warm.isSelected()) {
            tempType = "warm";
        } else if (cold.isSelected()) {
            tempType = "cold";
        }

        if (utensils.isSelected()) {
            extras.add("utensils");
        }
        if (napkins.isSelected()) {
            extras.add("napkins");
        }
        if (glass.isSelected()) {
            extras.add("glass");
        }

        entry =
                new FoodServiceRequestEntry(
                        patientName.getText(), roomNumber.getText(), additionalNotes.getText(), mealDropdown.getSelectedItem(), tempType, extras, dietaryRestrictions.getText());
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    public void clearEntry() {

    }
}
