package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FoodServiceRequestEntry extends RequestEntry {
    private final String foodSelection;
    private final String tempType;
    private final List<String> additionalItems;
    private final String dietaryRestrictions;
    private final String patientName;

    public FoodServiceRequestEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status, String foodSelection, String tempType, List<String> additionalItems, String dietaryRestrictions, String patientName) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
    }

    public FoodServiceRequestEntry(String roomNumber, String staffAssignment, String additionalNotes, String foodSelection, String tempType, List<String> additionalItems, String dietaryRestrictions, String patientName) {
        super(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
    }
}
