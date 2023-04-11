package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
    }

    public GenericRequestEntry(String roomNumber, String staffAssignment, String additionalNotes) {
        super(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING);
    }
    @lombok.RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.GenericRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case ROOM_NUMBER -> getRoomNumber();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
        };
    }

}