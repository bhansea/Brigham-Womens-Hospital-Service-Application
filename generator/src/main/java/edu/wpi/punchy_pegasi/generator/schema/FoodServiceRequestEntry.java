package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class FoodServiceRequestEntry extends RequestEntry {
    private String foodSelection;
    private String tempType;
    private List<String> additionalItems;
    private String dietaryRestrictions;
    private String patientName;
    private String beverage;

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
