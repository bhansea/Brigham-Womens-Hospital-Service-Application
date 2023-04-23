package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import javax.print.attribute.standard.ColorSupported;
import java.util.ArrayList;
import java.util.Arrays;

public class FlowerDeliveryRequestController extends RequestController<FlowerDeliveryRequestEntry> {
    TextField patientName = new TextField();
    @FXML
    PFXCardHolder cardHolder;
    @FXML
    VBox container = new VBox();
    @FXML
    private BorderPane root;


    public static BorderPane create(String path) {
        return RequestController.create(new FlowerDeliveryRequestController(), path);
    }

    @FXML
    public void init() {
        addTextField(patientName);
        submit.setDisable(true);
        patientName.setOnKeyTyped(e -> validateEntry());
        PFXCardVertical card1 = new PFXCardVertical("Daisy", "Beautiful flower", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/flower/daisy.jpg"));
        PFXCardVertical card2 = new PFXCardVertical("Lavendar", "Amazing smell!", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/flower/lavendar.jpg"));
        PFXCardVertical card3 = new PFXCardVertical("Red Rose", "Flower of love", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/flower/red-roses.jpg"));
        PFXCardVertical card4 = new PFXCardVertical("Sunflower", "Looks great!", 20, new Image("edu/wpi/punchy_pegasi/frontend/assets/flower/sunflower.jpg"));
        var flowPane = new FlowPane(card1, card2, card3, card4);
        flowPane.setHgap(10);
        flowPane.setVgap(10);
        flowPane.setStyle("-fx-border-width: 0px; -fx-background-color: -pfx-background;");
        flowPane.setAlignment(Pos.CENTER);
        var scrollPane = new ScrollPane(flowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-border-width: 0px; -fx-background-color: -pfx-background;");
        root.setCenter(scrollPane);
    }

    @FXML
    public void submitEntry() {
        // TODO: need a way to get the employeeID of the person making the request entry
        requestEntry = new FlowerDeliveryRequestEntry(patientName.getText(), locationName.getSelectedItem().getUuid(), staffAssignment.getSelectedItem().getEmployeeID(), additionalNotes.getText(), "", "", "", 1L);
        App.getSingleton().getFacade().saveFlowerDeliveryRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
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
//        cardHolder.clear();
    }
}
