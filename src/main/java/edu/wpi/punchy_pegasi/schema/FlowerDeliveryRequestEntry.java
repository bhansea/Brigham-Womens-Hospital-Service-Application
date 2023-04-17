package edu.wpi.punchy_pegasi.schema;

import edu.wpi.punchy_pegasi.generator.schema.IField;
import lombok.Getter;

import java.util.UUID;

@Getter
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private final String flowerSize;
    private final String flowerType;
    private final String flowerAmount;
    private final String patientName;

    public FlowerDeliveryRequestEntry(UUID serviceID, String patientName, Long locationName, Long staffAssignment, String additionalNotes, Status status, String flowerSize, String flowerAmount, String flowerType) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(String patientName, Long locationName, Long staffAssignment, String additionalNotes, String flowerSize, String flowerAmount, String flowerType) {
        this(UUID.randomUUID(), patientName, locationName, staffAssignment, additionalNotes, Status.PROCESSING, flowerSize, flowerAmount, flowerType);
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case FLOWER_SIZE -> getFlowerSize();
            case FLOWER_TYPE -> getFlowerType();
            case FLOWER_AMOUNT -> getFlowerAmount();
            case PATIENT_NAME -> getPatientName();
        };
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<FlowerDeliveryRequestEntry> {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
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

}