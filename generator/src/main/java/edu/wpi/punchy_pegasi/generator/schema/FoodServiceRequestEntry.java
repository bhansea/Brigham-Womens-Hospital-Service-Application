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
    private final String beverage;

    public FoodServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String foodSelection, String tempType, List<String> additionalItems, String beverage, String dietaryRestrictions, String patientName, Long employeeID) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, employeeID);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
        this.beverage = beverage;
    }

    public FoodServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String foodSelection, String tempType, List<String> additionalItems, String beverage, String dietaryRestrictions, String patientName, Long employeeID) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, employeeID);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
        this.beverage = beverage;
    }
}
