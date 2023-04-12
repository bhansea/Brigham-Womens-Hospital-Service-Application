package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class FlowerDeliveryRequestController extends RequestController<FlowerDeliveryRequestEntry> {
    @FXML
    MFXComboBox<String> flowerTypeComboBox, flowerSize;
    @FXML
    TextField flowerAmountField;
    TextField patientName = new TextField();
    Label price = new Label("$0.00");

    public static BorderPane create(String path) {
        return RequestController.create(new FlowerDeliveryRequestController(), path);
    }

    @FXML
    public void init() {
        ObservableList<String> flowerTypesList = FXCollections.observableArrayList("Rose", "Tulip", "Lavender");
        flowerTypeComboBox.setItems(flowerTypesList);
        ObservableList<String> flowerSizeList = FXCollections.observableArrayList("Small", "Medium", "Large");
        flowerSize.setItems(flowerSizeList);
        addLabel(price);
        addTextField(patientName);
        submit.setDisable(true);
        setHeaderText("Flower Delivery Request");
        flowerAmountField.setOnKeyTyped(e -> validateEntry());
        patientName.setOnKeyTyped(e -> validateEntry());
        flowerTypeComboBox.setOnAction(e -> validateEntry());
        flowerSize.setOnAction(e -> validateEntry());
    }

    @FXML
    public void submitEntry() {
        requestEntry = new FlowerDeliveryRequestEntry(patientName.getText(), locationName.getSelectedItem().getUuid(),
                staffAssignment.getSelectedItem().getEmployeeID(),additionalNotes.getText(), flowerSize.getSelectedItem(), flowerAmountField.getText(), flowerTypeComboBox.getSelectedItem());
        App.getSingleton().getFacade().saveFlowerDeliveryRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || patientName.getText().isBlank() || flowerAmountField.getText().isBlank() || flowerSize.getSelectedItem() == null || flowerTypeComboBox.getSelectedItem() == null;
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        flowerAmountField.clear();
        flowerTypeComboBox.clearSelection();
        patientName.clear();
        flowerSize.clearSelection();
    }
}
