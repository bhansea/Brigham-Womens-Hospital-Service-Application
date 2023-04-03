package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.RequestEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

@Slf4j
public abstract class RequestController<T extends RequestEntry> {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @FXML
    protected T requestEntry;
    @FXML
    protected TextField patientName;
    @FXML
    protected TextField roomNumber;
    @FXML
    protected TextField staffAssignment;
    @FXML
    protected TextField additionalNotes;
    @FXML
    protected Button submit;

    public static BorderPane create(RequestController controller, String path) {
        final var genericResource = App.class.getResource("components/Request.fxml");
        FXMLLoader generic = new FXMLLoader(genericResource);
        final var resource = App.class.getResource(path);
        FXMLLoader loader = new FXMLLoader(resource);

        generic.setController(controller);
        loader.setController(controller);
        try {
            Parent l = loader.load();
            BorderPane g = generic.load();
            g.setCenter(l);
            return g;
        } catch (IOException e) {
            log.error("create error", e);
            return null;
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @FXML
    protected void submitEntry() {
    }

    protected boolean isLoaded() {
        return patientName != null;
    }

    // This is an alternative to the built-in propertyChange
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
    }

    @FXML
    protected final void initialize() {
        if (!isLoaded()) return;
        for (var node : new TextField[]{patientName, roomNumber, additionalNotes})
            node.textProperty().addListener((obs, oldText, newText) -> {
                support.firePropertyChange(node.getId() + "TextChanged", oldText, newText);
                fieldChanged(node.getId() + "TextChanged", oldText, newText);
            });
        init();
    }

    public abstract void init();

    protected boolean validateGeneric() {
        return (patientName.getText().isBlank() || roomNumber.getText().isBlank() || staffAssignment.getText().isBlank());
    }

    protected void clearGeneric() {
        patientName.clear();
        roomNumber.clear();
        staffAssignment.clear();
        additionalNotes.clear();
    }
}
