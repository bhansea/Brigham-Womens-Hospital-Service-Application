package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
    }

    public GenericRequestEntry(Long locationName, Long staffAssignment, String additionalNotes) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING);
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.GenericRequestEntry> {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.GenericRequestEntry ref) {
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
        };
    }

}