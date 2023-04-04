package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.frontend.OfficeServiceRequestEntry;
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

public class OfficeServiceRequestController extends RequestController<OfficeServiceRequestEntry> implements PropertyChangeListener {
    @FXML
    TextField officeRequest;

    public static BorderPane create() {
        var cont = new OfficeServiceRequestController();
        return RequestController.create(new OfficeServiceRequestController(), "views/OfficeServiceRequest.fxml");
    }
    @FXML
    public void init() {}

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
        requestEntry = new OfficeServiceRequestEntry(patientName.getText(), roomNumber.getText(), staffAssignment.getText(), additionalNotes.getText(), officeRequest.getText(), "");
        Navigation.navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || officeRequest.getText().isBlank();
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        officeRequest.clear();
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
