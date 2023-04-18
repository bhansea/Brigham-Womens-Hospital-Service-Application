package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodServiceRequestController extends RequestController<FoodServiceRequestEntry> implements PropertyChangeListener {
    FoodServiceRequestEntry entry;
    @FXML
    TextArea dietaryRestrictions;
    @FXML
    MFXComboBox<String> beverageDropdown;

    TextField patientName = new TextField();
    @FXML
    PFXCardHolder cardHolder;

    public static BorderPane create(String path) {
        return RequestController.create(new FoodServiceRequestController(), path);
    }

    @FXML
    public void init() {
        ObservableList<String> beverageList = FXCollections.observableArrayList("Water", "Coffee", "Lemonade", "Milk", "Vitamin Water", "Dr. Pepper", "Chocolate Milk", "Apple Juice", "Orange Juice", "Cranberry Juice");
        beverageDropdown.setItems(beverageList);
        PFXCardVertical card1 = new PFXCardVertical("Mac and Cheese", "Delicious macaroni and cheese", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/mac-and-cheese.jpg"));
        PFXCardVertical card2 = new PFXCardVertical("Chicken and Rice", "Artisan-crafted meal", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/chicken-and-rice.jpg"));
        PFXCardVertical card3 = new PFXCardVertical("Meatloaf", "A delightful dish", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/meatloaf.jpg"));
        PFXCardVertical card4 = new PFXCardVertical("Steak", "Pan-seared goodness", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/food/steak.jpg"));
        cardHolder = new PFXCardHolder(new ArrayList<>(Arrays.asList(card1, card2, card3, card4)));

        addTextField(patientName);
        setHeaderText("Food Service Request");
        submit.setDisable(true);
        this.addPropertyChangeListener(this);


        beverageDropdown.setOnAction(e -> validateEntry());
        dietaryRestrictions.setOnKeyTyped(e -> validateEntry());
    }

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
//        requestEntry = entry = new FoodServiceRequestEntry(
//                locationName.getSelectedItem().getUuid(),
//                staffAssignment.getSelectedItem().getEmployeeID(),
//                additionalNotes.getText(),
//                mealDropdown.getSelectedItem(),
//                tempDropdown.getSelectedItem(),
//                extras,
//                beverageDropdown.getSelectedItem(),
//                dietaryRestrictions.getText(),
//                patientName.getText());
        App.getSingleton().getFacade().saveFoodServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        boolean validate = validateGeneric() || patientName.getText().isBlank() || beverageDropdown.getSelectedItem() == null;
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        patientName.clear();
        dietaryRestrictions.clear();
        beverageDropdown.clearSelection();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
