package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private final String flowerSize;
    private final String flowerType;
    private final String flowerAmount;
    private final String patientName;

    public FlowerDeliveryRequestEntry(UUID serviceID, String patientName, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText, String flowerSize, String flowerAmount, String flowerType) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(String patientName, Long locationName, Long staffAssignment, String additionalNotes, String invalidText, String flowerSize, String flowerAmount, String flowerType) {
        this(UUID.randomUUID(), patientName, locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText, flowerSize, flowerAmount, flowerType);
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry> {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        INVALID_TEXT("invalidText"),
        FLOWER_SIZE("flowerSize"),
        FLOWER_TYPE("flowerType"),
        FLOWER_AMOUNT("flowerAmount"),
        PATIENT_NAME("patientName");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry ref) {
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
            case FLOWER_SIZE -> getFlowerSize();
            case FLOWER_TYPE -> getFlowerType();
            case FLOWER_AMOUNT -> getFlowerAmount();
            case PATIENT_NAME -> getPatientName();
        };
    }

}