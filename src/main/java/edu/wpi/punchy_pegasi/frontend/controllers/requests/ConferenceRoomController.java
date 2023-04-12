package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.generated.ConferenceRoomEntryDaoImpl;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.net.URL;

public class ConferenceRoomController extends RequestController<ConferenceRoomEntry> {

    @FXML
    MFXComboBox<String> beginningTime;
    @FXML
    MFXComboBox<String> endTime;
    @FXML
    MFXDatePicker calendar;


    public static BorderPane create(String path) {
        return RequestController.create(new ConferenceRoomController(), path);
    }

    private ObservableList<String> timeList = FXCollections.observableArrayList("12:00am", "12:30am", "1:00am", "1:30am",
            "2:00am", "2:30am",
            "3:00am", "3:30am",
            "4:00am", "4:30am",
            "5:00am", "5:30am",
            "6:00am", "6:30am",
            "7:00am", "7:30am",
            "8:00am", "8:30am",
            "9:00am", "9:30am",
            "10:00am", "10:30am",
            "11:00am", "11:30am",
            "12:00pm", "12:30pm",
            "12:00pm", "12:30pm",
            "1:00pm", "1:30pm",
            "2:00pm", "2:30pm",
            "3:00pm", "3:30pm",
            "4:00pm", "4:30pm",
            "5:00pm", "5:30pm",
            "6:00pm", "6:30pm",
            "7:00pm", "7:30pm",
            "8:00pm", "8:30pm",
            "9:00pm", "9:30pm",
            "10:00pm", "10:30pm",
            "11:00pm", "11:30pm");

    @FXML
    public void init() {
        beginningTime.setItems(timeList);
        endTime.setItems(timeList);
        endTime.setDisable(true);
        submit.setDisable(true);
        beginningTime.setOnAction(e->validateEntry());
        endTime.setOnAction(e-> validateEntry());
        setHeaderText("Conference Room Request");
    }

    @Override
    protected void clearEntry() {
        clearGeneric();
        beginningTime.clearSelection();
        endTime.clearSelection();
        calendar.clear();
    }

    //validates the bottom box if the front one is filled in
    @FXML
    public void validateField() {
        boolean selected = beginningTime.getSelectedItem() == null;
        endTime.setDisable(selected);
        validateEntry();
    }

    @Override
    protected void validateEntry() {
        boolean val = validateGeneric() || !timeCheck();
        submit.setDisable(val);
    }

    //will check to make sure that the end time is after the beginning time
    public boolean timeCheck() {
        String btHolder = beginningTime.getSelectedItem();
        String etHolder = endTime.getSelectedItem();
        return timeList.indexOf(btHolder) < timeList.indexOf(etHolder);
    }

    @FXML
    public void submitEntry() {
        requestEntry =
                new ConferenceRoomEntry(
                        locationName.getSelectedItem().getUuid(),
                        staffAssignment.getSelectedItem().getEmployeeID(),
                        additionalNotes.getText(),
                        beginningTime.getText(),
                        endTime.getText(),
                        calendar.getValue());
        App.getSingleton().getFacade().saveConferenceRoomEntry(requestEntry);
        App.getSingleton().navigate(Screen.HOME);
    }


}
