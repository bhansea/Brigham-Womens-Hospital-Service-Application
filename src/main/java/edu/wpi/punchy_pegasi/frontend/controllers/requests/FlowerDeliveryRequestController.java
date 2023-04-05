package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import edu.wpi.punchy_pegasi.generated.FlowerDeliveryRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;

import java.net.URL;

public class FlowerDeliveryRequestController extends RequestController<FlowerDeliveryRequestEntry> {
    @FXML
    MFXComboBox<String> flowerTypeComboBox;
    @FXML
    TextField flowerAmountField;
    @FXML
    ToggleGroup flowerSizeGroup;
    @FXML
    Label flowerName;

    TextField patientName = new TextField();
    Label price = new Label("$0.00");

    public static BorderPane create(URL path) {
        return RequestController.create(new FlowerDeliveryRequestController(), path);
    }

    @FXML
    public void init() {
        ObservableList<String> flowerTypesList = FXCollections.observableArrayList("Rose", "Tulip", "Lavender");
        flowerTypeComboBox.setItems(flowerTypesList);
        addTotal(price);
        addTextField(patientName);
        submit.setDisable(true);
    }

    @FXML
    public void submitEntry() {
        requestEntry = new FlowerDeliveryRequestEntry(patientName.getText(), roomNumber.getText(), staffAssignment.getText(), additionalNotes.getText(), ((RadioButton) flowerSizeGroup.getSelectedToggle()).getId(), flowerAmountField.getText(), flowerTypeComboBox.getSelectedItem());
        FlowerDeliveryRequestEntryDaoImpl request = new FlowerDeliveryRequestEntryDaoImpl();
        request.save(requestEntry);
        Navigation.navigate(Screen.HOME);
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || flowerAmountField.getText().isBlank() || flowerSizeGroup.getSelectedToggle() == null || flowerTypeComboBox.getSelectedItem() == null;
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        flowerAmountField.clear();
        flowerTypeComboBox.clear();
        flowerSizeGroup.selectToggle(null);
    }

    @FXML
    public void updateLabelEntry() {
        validateEntry();
        flowerName.setText(flowerTypeComboBox.getSelectedItem());
    }
}
