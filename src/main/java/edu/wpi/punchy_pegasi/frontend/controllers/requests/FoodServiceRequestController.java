package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.frontend.components.PFXPopup;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Value;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> implements PropertyChangeListener {
    FoodServiceRequestEntry entry;

    TextField patientName = new TextField();
    @FXML
    PFXCardHolder cardHolder;
    @FXML
    VBox container = new VBox();


    public static BorderPane create(String path) {
        return RequestController.create(new FoodServiceRequestController(), path);
    }

    @FXML
    public void init() {
        PFXCardVertical card1 = new PFXCardVertical("Mac and Cheese", "Delicious mac", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/mac-and-cheese.jpg"));
        PFXCardVertical card2 = new PFXCardVertical("Chicken and Rice", "Artisan-crafted meal", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/chicken-and-rice.jpg"));
        PFXCardVertical card3 = new PFXCardVertical("Meatloaf", "A delightful dish", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/meatloaf.jpg"));
        PFXCardVertical card4 = new PFXCardVertical("Steak", "Pan-seared goodness", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/steak.jpg"));
        cardHolder = new PFXCardHolder(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)));

        container.getChildren().add(cardHolder);
        addTextField(patientName);
        submit.setDisable(true);
        this.addPropertyChangeListener(this);
        container.setAlignment(Pos.CENTER);
    }

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
        requestEntry = entry = new FoodServiceRequestEntry(
                locationName.getSelectedItem().getUuid(),
                staffAssignment.getSelectedItem().getEmployeeID(),
                additionalNotes.getText(),
                cardHolder.getChosenItems(),
                "",
                new ArrayList<>(List.of("")),
                "",
                "",
                patientName.getText(), 1L);
        App.getSingleton().getFacade().saveFoodServiceRequestEntry(requestEntry);
        Stage stage = App.getSingleton().getPrimaryStage();
        PFXPopup pfxPopup = new PFXPopup(stage, "Your request has been submitted!");
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || patientName.getText().isBlank();
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        patientName.clear();
        cardHolder.clear();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
