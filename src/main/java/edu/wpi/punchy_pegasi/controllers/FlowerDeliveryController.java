package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;

public class FlowerDeliveryController {

    @FXML
    Button submitButton;
    @FXML
    MFXComboBox<String> flowerTypeComboBox;
    @FXML
    javafx.scene.control.TextField flowerAmountField;
    @FXML
    javafx.scene.control.TextField patientNameField;
    @FXML
    javafx.scene.control.TextField roomNumberField;
    @FXML
    javafx.scene.control.TextField additionalNotesField;
    @FXML
    RadioButton small;
    @FXML
    RadioButton medium;
    @FXML
    RadioButton large;

    FlowerDeliveryRequestEntry requestEntry;

    @FXML
    public void initialize() {

        ObservableList<String> flowerTypesList =
                FXCollections.observableArrayList("Rose", "Tulip", "Lavender");
        flowerTypeComboBox.setItems(flowerTypesList);
    }

    @FXML
    public void submitEntry() {
        String name;
        String notes;
        String room;
        String flowerAmount;
        String size = "";

        if (small.isSelected()) {
            size = "Small";
        } else if (medium.isSelected()) {
            size = "Medium";
        } else if (large.isSelected()) {
            size = "Large";
        }

        try {
            name = patientNameField.getText();
        } catch (NullPointerException e) {
            name = "";
        }
        try {
            room = roomNumberField.getText();
        } catch (NullPointerException e) {
            room = "";
        }
        try {
            notes = additionalNotesField.getText();
        } catch (NullPointerException e) {
            notes = "";
        }
        try {
            flowerAmount = flowerAmountField.getText();
        } catch (NullPointerException e) {
            flowerAmount = "";
        }


        requestEntry = new FlowerDeliveryRequestEntry(name, notes, size, room, flowerAmount, flowerTypeComboBox.getSelectedItem());

        Navigation.navigate(Screen.HOME);
    }
}
