package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Employee;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.collections.FXCollections;
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
import javafx.util.StringConverter;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

import static edu.wpi.punchy_pegasi.frontend.utils.FacadeUtils.isDestination;

@Slf4j
public abstract class RequestController<T extends RequestEntry> {
    private static final String DEFAULT_FONT = "Nunito Sans Regular";
    protected final Facade facade = App.getSingleton().getFacade();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @FXML
    protected T requestEntry;
    @FXML
    protected TextField additionalNotes;
    @FXML
    protected Button submit;
    @FXML
    protected VBox inputContainer;
    @FXML
    protected VBox requestInfoContainer;
    @FXML
    VBox filterContainer = new VBox();
    @FXML
    protected Label headerText;
    @FXML
    MFXFilterComboBox<LocationName> locationName;
    @FXML
    MFXFilterComboBox<Employee> staffAssignment;
    PFXButton filter = new PFXButton("Filter Category");
    @FXML
    PFXCardHolder cardHolder;

    public static BorderPane create(RequestController controller, String path) {
        try {
            Parent l = App.getSingleton().loadWithCache(path, controller).getRoot();
            BorderPane g = App.getSingleton().loadWithCache("frontend/layouts/Request.fxml", controller).getRoot();
            g.setCenter(l);
            return g;
        } catch (IOException e) {
            log.error("create error", e);
            return null;
        }
    }

    protected void setHeaderText(String headerText) {
        this.headerText.setText(headerText);
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
//        componentHolder.getStyleClass().add("pfx-request-component-holder");
        locationName.setItems(facade.getAllAsListLocationName().filtered(isDestination).sorted(Comparator.comparing(LocationName::getLongName)));
        staffAssignment.setItems(facade.getAllAsListEmployee());
        locationName.setOnAction(p -> validateEntry());
        staffAssignment.setOnAction(p -> validateEntry());
        var employeeToName = new StringConverter<Employee>() {

            @Override
            public String toString(Employee employee) {
                if (employee == null) return "";
                return employee.getFirstName() + " " + employee.getLastName();
            }

            @Override
            public Employee fromString(String string) {
                return null;
            }
        };
        var locationToLongName = new StringConverter<LocationName>() {
            @Override
            public String toString(LocationName object) {
                if (object == null) return "";
                return object.getLongName();
            }

            @Override
            public LocationName fromString(String string) {
                return null;
            }
        };
        staffAssignment.setConverter(employeeToName);
        locationName.setConverter(locationToLongName);
        staffAssignment.setOnAction(e -> validateEntry());
        locationName.setOnAction(e -> validateEntry());
        for (var node : new TextField[]{locationName, staffAssignment, additionalNotes})
            node.textProperty().addListener((obs, oldText, newText) -> {
                support.firePropertyChange(node.getId() + "TextChanged", oldText, newText);
                fieldChanged(node.getId() + "TextChanged", oldText, newText);
            });
        init();
    }

    public abstract void init();

    protected boolean validateGeneric() {
        return (locationName.getSelectedItem() == null || staffAssignment.getSelectedItem() == null);
    }

    protected void clearGeneric() {
        locationName.clearSelection();
        staffAssignment.clearSelection();
        additionalNotes.clear();
    }

    @FXML
    protected abstract void clearEntry();

    @FXML
    protected abstract void validateEntry();

    @FXML
    protected void addTextField(TextField field) {
        Label label = new Label("Patient Name");
        inputContainer.getChildren().add(0, field);
        inputContainer.getChildren().add(0, label);
        field.setPromptText("Enter Patient Name");
        field.setAlignment(Pos.CENTER_LEFT);
        field.setOnKeyTyped(a -> validateEntry());
    }

    @FXML
    protected VBox addFilter(ArrayList<PFXCardVertical> cards, String filterName) {
        filterContainer.setAlignment(Pos.TOP_LEFT);
        cardHolder = new PFXCardHolder(cards, filterName);
        filterContainer.getChildren().add(cardHolder);
        filterContainer.setPadding(new Insets(10, 10, 10, 23));
        return filterContainer;
    }
}
