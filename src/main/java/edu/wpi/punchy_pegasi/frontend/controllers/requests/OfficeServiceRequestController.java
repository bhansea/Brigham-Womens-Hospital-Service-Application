package edu.wpi.punchy_pegasi.frontend.controllers.requests;


import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.components.PFXAlert;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

public class OfficeServiceRequestController extends RequestController<OfficeServiceRequestEntry> implements PropertyChangeListener {
    @FXML
    PFXCardHolder cardHolder;
    @FXML
    VBox container = new VBox();
    ArrayList<PFXCardVertical> items = new ArrayList<>();
    @FXML
    private PFXCardVertical pencils = new PFXCardVertical();
    @FXML
    private PFXCardVertical pens = new PFXCardVertical();
    @FXML
    private PFXCardVertical paper = new PFXCardVertical();
    @FXML
    private PFXCardVertical stapler = new PFXCardVertical();

    public static BorderPane create(String path) {
        return RequestController.create(new OfficeServiceRequestController(), path);
    }

    @FXML
    @Override
    public void init() {
        submit.setDisable(true);
        ImageView pencilPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSupplies/colored-pencils.jpg");
        pencils = new PFXCardVertical("Pencils", "wood and graphite", 50, pencilPic.getImage());

        ImageView penPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSupplies/pen.jpg");
        pens = new PFXCardVertical("Pens", "It has ink", 30, penPic.getImage());

        ImageView paperPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSupplies/paper.jpg");
        paper = new PFXCardVertical("Paper", "Thin and soft", 100, paperPic.getImage());

        ImageView staplerPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSupplies/stapler.jpg");
        stapler = new PFXCardVertical("Stapler", "Staples papers together", 5, staplerPic.getImage());

        cardHolder = new PFXCardHolder(new ArrayList<>(Arrays.asList(pencils, pens, paper, stapler)));

        container.getChildren().add(cardHolder);
        submit.setDisable(true);
        this.addPropertyChangeListener(this);
        container.setAlignment(Pos.CENTER);
    }

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
        requestEntry = new OfficeServiceRequestEntry(locationName.getSelectedItem().getUuid(), staffAssignment.getSelectedItem().getEmployeeID(), additionalNotes.getText(), cardHolder.getChosenItems(), 1L);
        App.getSingleton().getFacade().saveOfficeServiceRequestEntry(requestEntry);
        new PFXAlert("Your request has been submitted!");
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        var validate = validateGeneric();
        submit.setDisable(validate);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        cardHolder.clear();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
