package edu.wpi.punchy_pegasi;

import java.util.List;

public class FoodServiceRequestEntry {
    private final String foodSelection;
    private final String tempType;
    private final List<String> additionalItems;
    private final String dietaryRestrictions;
    private final String patientName;
    private final String roomName;
    private final String additionalNotes;

    public FoodServiceRequestEntry(
            String patientName,
            String roomName,
            String additionalNotes,
            String foodSelection,
            String tempType,
            List<String> additionalItems,
            String dietaryRestrictions) {
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
        this.roomName = roomName;
        this.additionalNotes = additionalNotes;
    }
}
