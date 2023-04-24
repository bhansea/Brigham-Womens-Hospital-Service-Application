package edu.wpi.punchy_pegasi.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class FoodServiceRequestEntry extends RequestEntry {
    @com.jsoniter.annotation.JsonProperty("foodselection")
    private String foodSelection;
    @com.jsoniter.annotation.JsonProperty("temptype")
    private String tempType;
    @com.jsoniter.annotation.JsonProperty("additionalitems")
    private List<String> additionalItems;
    @com.jsoniter.annotation.JsonProperty("dietaryrestrictions")
    private String dietaryRestrictions;
    @com.jsoniter.annotation.JsonProperty("patientname")
    private String patientName;
    @com.jsoniter.annotation.JsonProperty("beverage")
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
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        EMPLOYEE_ID("employeeID"),
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
public String getValueAsString(edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry ref){
    return ref.getFromFieldAsString(this);
}
    public void setValueFromString(edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry ref, String value){
            ref.setFieldFromString(this, value);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case EMPLOYEE_ID -> getEmployeeID();
            case FOOD_SELECTION -> getFoodSelection();
            case TEMP_TYPE -> getTempType();
            case ADDITIONAL_ITEMS -> getAdditionalItems();
            case DIETARY_RESTRICTIONS -> getDietaryRestrictions();
            case PATIENT_NAME -> getPatientName();
            case BEVERAGE -> getBeverage();
        };
    }
    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case SERVICE_ID -> setServiceID(UUID.fromString(value));
            case LOCATION_NAME -> setLocationName(Long.parseLong(value));
            case STAFF_ASSIGNMENT -> setStaffAssignment(Long.parseLong(value));
            case ADDITIONAL_NOTES -> setAdditionalNotes(value);
            case STATUS -> setStatus(Status.valueOf(value));
            case EMPLOYEE_ID -> setEmployeeID(Long.parseLong(value));
            case FOOD_SELECTION -> setFoodSelection(value);
            case TEMP_TYPE -> setTempType(value);
            case ADDITIONAL_ITEMS -> setAdditionalItems(new java.util.ArrayList<>(java.util.Arrays.asList(value.split("\\s*,\\s*"))));
            case DIETARY_RESTRICTIONS -> setDietaryRestrictions(value);
            case PATIENT_NAME -> setPatientName(value);
            case BEVERAGE -> setBeverage(value);
        };
    }
    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID().toString();
            case LOCATION_NAME -> Long.toString(getLocationName());
            case STAFF_ASSIGNMENT -> Long.toString(getStaffAssignment());
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus().name();
            case EMPLOYEE_ID -> Long.toString(getEmployeeID());
            case FOOD_SELECTION -> getFoodSelection();
            case TEMP_TYPE -> getTempType();
            case ADDITIONAL_ITEMS -> String.join(", ", getAdditionalItems());
            case DIETARY_RESTRICTIONS -> getDietaryRestrictions();
            case PATIENT_NAME -> getPatientName();
            case BEVERAGE -> getBeverage();
        };
    }

}