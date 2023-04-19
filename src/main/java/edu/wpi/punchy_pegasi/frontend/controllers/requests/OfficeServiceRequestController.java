package edu.wpi.punchy_pegasi.frontend.controllers.requests;


import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHorizontal;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class OfficeServiceRequestController extends RequestController<OfficeServiceRequestEntry> implements PropertyChangeListener {
    @FXML
    private PFXCardVertical pencils = new PFXCardVertical();
    @FXML
    private PFXCardVertical pens = new PFXCardVertical();
    @FXML
    private PFXCardVertical paper = new PFXCardVertical();
    @FXML
    private PFXCardVertical stapler = new PFXCardVertical();
    @FXML
    private PFXCardVertical staples = new PFXCardVertical();
    @FXML
    private PFXCardVertical paperclips = new PFXCardVertical();

    ArrayList<PFXCardVertical> items = new ArrayList<>();

    public static BorderPane create(String path) {
        return RequestController.create(new OfficeServiceRequestController(), path);
    }

    @Override
    public void init() {
        submit.setDisable(true);
        ImageView pencilPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label pencilLabel = new Label("Pencils");
        Label pencilInfo = new Label("A thing made of wood and graphite");
        Label pencilQuantity = new Label("50");
        pencils = new PFXCardVertical(pencilLabel, pencilInfo, pencilQuantity, pencilPic.getImage());

        ImageView penPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label penLabel = new Label("Pens");
        Label penInfo = new Label("A thing that has ink.");
        Label penQuantity = new Label("30");
        pens = new PFXCardVertical(penLabel, penInfo, penQuantity, penPic.getImage());

        ImageView paperPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label paperLabel = new Label("Paper");
        Label paperInfo = new Label("A thing made of wood that's really thin and soft");
        Label paperQuantity = new Label("100");
        paper = new PFXCardVertical(paperLabel, paperInfo, paperQuantity, paperPic.getImage());

        ImageView staplerPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label staplerLabel = new Label("Paper");
        Label staplerInfo = new Label("A thing made of wood that's really thin and soft");
        Label staplerQuantity = new Label("100");
        stapler = new PFXCardVertical(staplerLabel, staplerInfo, staplerQuantity, staplerPic.getImage());

        ImageView staplesPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label staplesLabel = new Label("Paper");
        Label staplesInfo = new Label("A thing made of wood that's really thin and soft");
        Label staplesQuantity = new Label("100");
        staples = new PFXCardVertical(staplesLabel, staplesInfo, staplesQuantity, staplesPic.getImage());

        ImageView paperclipsPic = new ImageView("edu/wpi/punchy_pegasi/frontend/assets/officeSuppliesPic.jpg");
        Label paperclipsLabel = new Label("Paper");
        Label paperclipsInfo = new Label("A thing made of wood that's really thin and soft");
        Label paperclipsQuantity = new Label("100");
        paperclips = new PFXCardVertical(paperclipsLabel, paperclipsInfo, paperclipsQuantity, paperclipsPic.getImage());

        items.add(pencils);
        items.add(pens);
        items.add(paper);
        items.add(stapler);
        items.add(staples);
        items.add(paperclips);
        setHeaderText("Office Supplies Service Request");
}

    @FXML
    public void submitEntry() {
//        StringBuilder reqString = new StringBuilder();
//        for (int i = 0; i < items.size(); i++)
//            reqString.append(items.get(i).getTitle()).append(" - ").append(items.get(i).getQuantity()).append("; ");

        //makes sure shared fields aren't empty
        //requestEntry = new OfficeServiceRequestEntry(locationName.getSelectedItem().getUuid(), staffAssignment.getSelectedItem().getEmployeeID(), additionalNotes.getText(), reqString.toString().trim(), "");
        //App.getSingleton().getFacade().saveOfficeServiceRequestEntry(requestEntry);
        //App.getSingleton().navigate(Screen.HOME);
    }

    @Override
    protected void fieldChanged(String id, Object oldValue, Object newValue) {
        validateEntry();
    }

    @FXML
    public void validateEntry() {
//        var validate = validateGeneric() || items.stream().filter(CheckBox::isSelected).toList().size() == 0;
//        for (int i = 0; i < items.size(); i++)
//            validate |= itemsAmount.get(i).getText().isBlank() && items.get(i).isSelected();
//        submit.setDisable(validate);
        submit.setDisable(false);
    }

    @FXML
    public void clearEntry() {
        clearGeneric();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().endsWith("TextChanged")) validateEntry();
    }
}
