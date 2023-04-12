package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.OfficeServiceRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;

public class OfficeServiceRequestController extends RequestController<OfficeServiceRequestEntry> implements PropertyChangeListener {
    @FXML
    TextField officeRequest;

    public static BorderPane create(String path) {
        return RequestController.create(new OfficeServiceRequestController(), path);
    }

    @FXML
    @Override
    public void init() {
        validateEntry();
    }

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
        requestEntry = new OfficeServiceRequestEntry(locationName.getText(), staffAssignment.getText(), additionalNotes.getText(), officeRequest.getText(), "");
        App.getSingleton().getFacade().saveOfficeServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
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
