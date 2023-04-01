package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;

public class FlowerDeliveryRequestController extends RequestController<FlowerDeliveryRequestEntry> {
    @FXML
    MFXComboBox<String> flowerTypeComboBox;
    @FXML
    javafx.scene.control.TextField flowerAmountField;
    @FXML
    RadioButton small;
    @FXML
    RadioButton medium;
    @FXML
    RadioButton large;

    public static BorderPane create() {
        var controller = new FlowerDeliveryRequestController();
        return RequestController.create(controller, "views/FlowerDeliveryRequest.fxml");
    }

    @FXML
    public void init() {
        ObservableList<String> flowerTypesList = FXCollections.observableArrayList("Rose", "Tulip", "Lavender");
        flowerTypeComboBox.setItems(flowerTypesList);
    }

    @FXML
    public void submitEntry() {
        String name = "", notes = "", room = "";
        String flowerAmount;
        String size = "";

        if (small.isSelected()) {
            size = "Small";
        } else if (medium.isSelected()) {
            size = "Medium";
        } else if (large.isSelected()) {
            size = "Large";
        }


        try {
            flowerAmount = flowerAmountField.getText();
        } catch (NullPointerException e) {
            flowerAmount = "";
        }

        if (this.checkSumbit()) return;
        requestEntry = new FlowerDeliveryRequestEntry(name, notes, size, room, flowerAmount, flowerTypeComboBox.getSelectedItem());

        Navigation.navigate(Screen.HOME);
    }
}
