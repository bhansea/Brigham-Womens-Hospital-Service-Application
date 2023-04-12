package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.extern.slf4j.Slf4j;
import io.github.palexdev.materialfx.controls.MFXComboBox;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import static com.sun.javafx.font.FontFactory.DEFAULT_FULLNAME;

@Slf4j
public abstract class RequestController<T extends RequestEntry> {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @FXML
    protected T requestEntry;

    @FXML
    MFXComboBox<String> locationName;
    @FXML
    protected TextField staffAssignment;
    @FXML
    protected TextField additionalNotes;
    @FXML
    protected Button submit;
    @FXML
    protected VBox inputContainer;
    @FXML
    protected VBox totalContainer;

    public static BorderPane create(RequestController controller, String path) {
        try {
            Parent l = App.getSingleton().loadWithCache(path, controller);
            BorderPane g = App.getSingleton().loadWithCache("frontend/layouts/Request.fxml", controller);
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
        return submit != null;
    }

    // This is an alternative to the built-in propertyChange
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
    }

    @FXML
    protected final void initialize() {
        if (!isLoaded()) return;
        for (var node : new TextField[]{locationName, staffAssignment, additionalNotes})
            node.textProperty().addListener((obs, oldText, newText) -> {
                support.firePropertyChange(node.getId() + "TextChanged", oldText, newText);
                fieldChanged(node.getId() + "TextChanged", oldText, newText);
            });
        init();
    }

    public abstract void init();

    protected boolean validateGeneric() {
        return (locationName.getText().isBlank() || staffAssignment.getText().isBlank());
    }

    protected void clearGeneric() {
        locationName.clear();
        staffAssignment.clear();
        additionalNotes.clear();
    }

    @FXML
    protected abstract void clearEntry();

    @FXML
    protected abstract void validateEntry();

    @FXML
    protected void addTextField(TextField field) {
        HBox hbox = new HBox();
        Label label = new Label("Patient Name");
        inputContainer.getChildren().add(0, hbox);
        inputContainer.getChildren().add(0,label);
        hbox.getChildren().add(0, field);
        hbox.setAlignment(Pos.CENTER);
        label.setFont(new Font(DEFAULT_FULLNAME, 24));
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.color(1,1,1));
        field.setPromptText("Enter Patient Name");
        field.setAlignment(Pos.CENTER);
        field.setFont(new Font(DEFAULT_FULLNAME, 24));
        inputContainer.setPadding(new Insets(20,20,20,20));
        hbox.setPadding(new Insets(0,0,0,0));
    }

    @FXML
    protected void addTotal(Label price){
        HBox hbox = new HBox();
        Label total = new Label("Total:");
        totalContainer.getChildren().add(0,hbox);
        hbox.getChildren().add(0, total);
        hbox.getChildren().add(1,price);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(10,0,0,0));
        price.setFont(new Font(DEFAULT_FULLNAME, 24));
        total.setFont(new Font(DEFAULT_FULLNAME, 24));
        total.setTextFill(Color.color(1,1,1));
        price.setTextFill(Color.color(1,1,1));
        hbox.setSpacing(150);
    }
}
