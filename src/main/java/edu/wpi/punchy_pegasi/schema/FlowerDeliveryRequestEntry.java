package edu.wpi.punchy_pegasi.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
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
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry> {
        SERVICE_ID("serviceID", true, false),
        LOCATION_NAME("locationName", false, false),
        STAFF_ASSIGNMENT("staffAssignment", false, false),
        ADDITIONAL_NOTES("additionalNotes", false, false),
        STATUS("status", false, false),
        EMPLOYEE_ID("employeeID", false, false),
        FLOWER_SIZE("flowerSize", false, false),
        FLOWER_TYPE("flowerType", false, false),
        FLOWER_AMOUNT("flowerAmount", false, false),
        PATIENT_NAME("patientName", false, false);
        @lombok.Getter
        private final String colName;
        @lombok.Getter
        private final boolean primaryKey;
        @lombok.Getter
        private final boolean unique;

        public Object getValue(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry ref) {
            return ref.getFromField(this);
        }

        public String getValueAsString(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry ref) {
            return ref.getFromFieldAsString(this);
        }

        public void setValueFromString(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry ref, String value) {
            ref.setFieldFromString(this, value);
        }

        public int oridinal() {
            return ordinal();
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

    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case SERVICE_ID -> setServiceID(UUID.fromString(value));
            case LOCATION_NAME -> setLocationName(Long.parseLong(value));
            case STAFF_ASSIGNMENT -> setStaffAssignment(Long.parseLong(value));
            case ADDITIONAL_NOTES -> setAdditionalNotes(value);
            case STATUS -> setStatus(Status.valueOf(value));
            case EMPLOYEE_ID -> setEmployeeID(Long.parseLong(value));
            case FLOWER_SIZE -> setFlowerSize(value);
            case FLOWER_TYPE -> setFlowerType(value);
            case FLOWER_AMOUNT -> setFlowerAmount(value);
            case PATIENT_NAME -> setPatientName(value);
        }
    }

    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID().toString();
            case LOCATION_NAME -> Long.toString(getLocationName());
            case STAFF_ASSIGNMENT -> Long.toString(getStaffAssignment());
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus().name();
            case EMPLOYEE_ID -> Long.toString(getEmployeeID());
            case FLOWER_SIZE -> getFlowerSize();
            case FLOWER_TYPE -> getFlowerType();
            case FLOWER_AMOUNT -> getFlowerAmount();
            case PATIENT_NAME -> getPatientName();
        };
    }

}