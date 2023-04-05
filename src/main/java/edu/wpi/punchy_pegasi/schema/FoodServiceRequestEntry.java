package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import lombok.Getter;

import lombok.RequiredArgsConstructor;


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
    @RequiredArgsConstructor
    public enum Field {
        FOOD_SELECTION("foodSelection"),
        TEMP_TYPE("tempType"),
        ADDITIONAL_ITEMS("additionalItems"),
        DIETARY_RESTRICTIONS("dietaryRestrictions"),
        PATIENT_NAME("patientName"),
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case FOOD_SELECTION -> getFoodSelection();
            case TEMP_TYPE -> getTempType();
            case ADDITIONAL_ITEMS -> getAdditionalItems();
            case DIETARY_RESTRICTIONS -> getDietaryRestrictions();
            case PATIENT_NAME -> getPatientName();
            case SERVICE_ID -> getServiceID();
            case ROOM_NUMBER -> getRoomNumber();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
        };
    }

}