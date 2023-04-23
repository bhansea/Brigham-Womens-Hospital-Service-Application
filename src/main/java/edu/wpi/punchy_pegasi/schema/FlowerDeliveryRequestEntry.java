package edu.wpi.punchy_pegasi.schema;

import com.jsoniter.annotation.JsonCreator;
import com.jsoniter.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class FlowerDeliveryRequestEntry extends RequestEntry {
    @com.jsoniter.annotation.JsonProperty("flowersize")
    private String flowerSize;
    @com.jsoniter.annotation.JsonProperty("flowertype")
    private String flowerType;
    @com.jsoniter.annotation.JsonProperty("floweramount")
    private String flowerAmount;
    @com.jsoniter.annotation.JsonProperty("patientname")
    private String patientName;

    public FlowerDeliveryRequestEntry(UUID serviceID, String patientName, Long locationName, Long staffAssignment, String additionalNotes, Status status, String flowerSize, String flowerAmount, String flowerType, Long employeeID) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, employeeID);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(String patientName, Long locationName, Long staffAssignment, String additionalNotes, String flowerSize, String flowerAmount, String flowerType, Long employeeID) {
        this(UUID.randomUUID(), patientName, locationName, staffAssignment, additionalNotes, Status.PROCESSING, flowerSize, flowerAmount, flowerType, employeeID);
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        EMPLOYEE_ID("employeeID"),
        FLOWER_SIZE("flowerSize"),
        FLOWER_TYPE("flowerType"),
        FLOWER_AMOUNT("flowerAmount"),
        PATIENT_NAME("patientName");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry ref){
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
            case EMPLOYEE_ID -> getEmployeeID();
            case FLOWER_SIZE -> getFlowerSize();
            case FLOWER_TYPE -> getFlowerType();
            case FLOWER_AMOUNT -> getFlowerAmount();
            case PATIENT_NAME -> getPatientName();
        };
    }

}