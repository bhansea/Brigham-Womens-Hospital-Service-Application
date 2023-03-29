package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.FlowerDeliveryRequestEntry;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;

public class FlowerDeliveryController {

  @FXML MFXButton submitButton;
  @FXML MFXComboBox<String> flowerTypeComboBox;
  @FXML javafx.scene.control.TextField flowerAmountField;
  @FXML javafx.scene.control.TextField patientNameField;
  @FXML javafx.scene.control.TextField roomNumberField;
  @FXML javafx.scene.control.TextField additionalNotesField;
  @FXML RadioButton flowerSizeSelector;

  @FXML
  private void initialize() {

    ObservableList<String> flowerTypesList =
        FXCollections.observableArrayList("Rose", "Tulip", "Lavender");
    flowerTypeComboBox.setItems(flowerTypesList);

    this.submitButton.setOnAction(
        event -> {
          FlowerDeliveryRequestEntry requestEntry =
              new FlowerDeliveryRequestEntry(
                  patientNameField.getText(),
                  additionalNotesField.getText(),
                  flowerSizeSelector.getToggleGroup().getSelectedToggle().toString(),
                  roomNumberField.getText(),
                  flowerAmountField.getText(),
                  flowerTypeComboBox.getSelectedItem());

          requestEntry.printFlowerReq();
        });
  }
}
