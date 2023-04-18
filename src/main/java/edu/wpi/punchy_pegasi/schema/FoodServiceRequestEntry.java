package edu.wpi.punchy_pegasi.schema;

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

    public FoodServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText, String foodSelection, String tempType, List<String> additionalItems, String beverage, String dietaryRestrictions, String patientName) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
        this.beverage = beverage;
    }

    public FoodServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String invalidText, String foodSelection, String tempType, List<String> additionalItems, String beverage, String dietaryRestrictions, String patientName) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText);
        this.foodSelection = foodSelection;
        this.tempType = tempType;
        this.additionalItems = additionalItems;
        this.dietaryRestrictions = dietaryRestrictions;
        this.patientName = patientName;
        this.beverage = beverage;
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        INVALID_TEXT("invalidText"),
        FOOD_SELECTION("foodSelection"),
        TEMP_TYPE("tempType"),
        ADDITIONAL_ITEMS("additionalItems"),
        DIETARY_RESTRICTIONS("dietaryRestrictions"),
        PATIENT_NAME("patientName"),
        BEVERAGE("beverage");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case INVALID_TEXT -> getInvalidText();
            case FOOD_SELECTION -> getFoodSelection();
            case TEMP_TYPE -> getTempType();
            case ADDITIONAL_ITEMS -> getAdditionalItems();
            case DIETARY_RESTRICTIONS -> getDietaryRestrictions();
            case PATIENT_NAME -> getPatientName();
            case BEVERAGE -> getBeverage();
        };
    }

}