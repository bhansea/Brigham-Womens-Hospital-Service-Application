package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class OfficeServiceRequestController extends RequestController<OfficeServiceRequestEntry> implements PropertyChangeListener {
    @FXML
    CheckBox pencils;
    @FXML
    CheckBox pens;
    @FXML
    CheckBox paper;
    @FXML
    CheckBox stapler;
    @FXML
    CheckBox staples;
    @FXML
    CheckBox paperclips;
    @FXML
    CheckBox other;
    @FXML
    TextField pencilAmount;
    @FXML
    TextField penAmount;
    @FXML
    TextField paperAmount;
    @FXML
    TextField staplerAmount;
    @FXML
    TextField stapleAmount;
    @FXML
    TextField paperclipAmount;
    @FXML
    TextField otherItems;
    ArrayList<CheckBox> items = new ArrayList<>();
    ArrayList<TextField> itemsAmount;

    public static BorderPane create(String path) {
        return RequestController.create(new OfficeServiceRequestController(), path);
    }

    @FXML
    @Override
    public void init() {
        //validateEntry();
    }

    @FXML
    public void submitEntry() {
        //makes sure shared fields aren't empty
        requestEntry = new OfficeServiceRequestEntry(locationName.getItems().get(0).getUuid(), staffAssignment.getItems().get(0).getEmployeeID(), additionalNotes.getText(),
                pencils.isSelected(), pencilAmount.getText(), pens.isSelected(), penAmount.getText(), paper.isSelected(), paperAmount.getText(),
                stapler.isSelected(), staplerAmount.getText(), staples.isSelected(), stapleAmount.getText(), paperclips.isSelected(),
                paperclipAmount.getText(), other.isSelected(), otherItems.getText());
        App.getSingleton().getFacade().saveOfficeServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        items.addAll(List.of(new CheckBox[]{pencils,
                pens,
                paper,
                stapler,
                staples,
                paperclips,
                other}));
        itemsAmount.add(pencilAmount);
        itemsAmount.add(penAmount);
        itemsAmount.add(paperAmount);
        itemsAmount.add(staplerAmount);
        itemsAmount.add(stapleAmount);
        itemsAmount.add(paperclipAmount);
        itemsAmount.add(otherItems);

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                if (itemsAmount.get(i).getText().isBlank()) {
                    submit.setDisable(true);
                }
            }
        }
    }

    @FXML
    public void clearEntry() {
        clearGeneric();
        pencils.setSelected(false);
        pencilAmount.clear();
        pens.setSelected(false);
        penAmount.clear();
        paper.setSelected(false);
        paperAmount.clear();
        stapler.setSelected(false);
        staplerAmount.clear();
        staples.setSelected(false);
        stapleAmount.clear();
        paperclips.setSelected(false);
        paperclipAmount.clear();
        other.setSelected(false);
        otherItems.clear();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
