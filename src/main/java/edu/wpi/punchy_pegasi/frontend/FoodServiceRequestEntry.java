package edu.wpi.punchy_pegasi.frontend;

import lombok.Data;
import lombok.Getter;

import java.util.List;
@Getter
public class FoodServiceRequestEntry extends RequestEntry {

    private final String foodSelection;
    private final String tempType;
    private final List<String> additionalItems;
    private final String dietaryRestrictions;


    public FoodServiceRequestEntry(
            String patientName,
            String roomName,
            String additionalNotes,
            String foodSelection,
            String tempType,
            List<String> additionalItems,
            String dietaryRestrictions) {
        super(patientName, roomName, additionalNotes);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
