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
    ArrayList<TextField> itemsAmount = new ArrayList<>();

    public static BorderPane create(String path) {
        return RequestController.create(new OfficeServiceRequestController(), path);
    }

    @FXML
    @Override
    public void init() {
        submit.setDisable(true);
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

        items.forEach(i -> i.setOnAction(e -> validateEntry()));
        itemsAmount.forEach(i -> i.setOnKeyTyped(e -> validateEntry()));
        setHeaderText("Office Supplies Service Request");
}

    @FXML
    public void submitEntry() {
        StringBuilder reqString = new StringBuilder();
        for (int i = 0; i < items.size(); i++)
            if (items.get(i).isSelected())
                reqString.append(items.get(i).getText()).append(" - ").append(itemsAmount.get(i)).append("; ");

        //makes sure shared fields aren't empty
        requestEntry = new OfficeServiceRequestEntry(locationName.getSelectedItem().getUuid(), staffAssignment.getSelectedItem().getEmployeeID(), additionalNotes.getText(),invalidText.getText(), reqString.toString().trim(), "");
        App.getSingleton().getFacade().saveOfficeServiceRequestEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
        var validate = validateGeneric() || items.stream().filter(CheckBox::isSelected).toList().size() == 0;
        for (int i = 0; i < items.size(); i++)
            validate |= itemsAmount.get(i).getText().isBlank() && items.get(i).isSelected();
        submit.setDisable(validate);
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
