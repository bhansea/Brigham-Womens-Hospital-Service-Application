package edu.wpi.punchy_pegasi.frontend;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FoodServiceRequestEntry extends RequestEntry {

    private final String foodSelection;
    private final String tempType;
    private final List<String> additionalItems;
    private final String dietaryRestrictions;


    public FoodServiceRequestEntry(
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            String foodSelection,
            String tempType,
            List<String> additionalItems,
            String dietaryRestrictions) {
        this(UUID.randomUUID(),
                patientName,
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                foodSelection,
                tempType,
                additionalItems,
                dietaryRestrictions);
    }

    public FoodServiceRequestEntry(
            UUID serviceID,
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status,
            String foodSelection,
            String tempType,
            List<String> additionalItems,
            String dietaryRestrictions) {
        super(serviceID, patientName, roomNumber, staffAssignment, additionalNotes, status);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
