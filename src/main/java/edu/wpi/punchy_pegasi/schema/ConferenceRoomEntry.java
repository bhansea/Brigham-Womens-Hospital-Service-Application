package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ConferenceRoomEntry extends RequestEntry {
    private final String beginningTime;
    private final String endTime;
    private final LocalDate date;

    public ConferenceRoomEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String beginningTime, String endTime, LocalDate date) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;

    }

    public ConferenceRoomEntry(Long locationName, Long staffAssignment, String additionalNotes, String beginningTime, String endTime, LocalDate date) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;
    }
    @lombok.RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        BEGINNING_TIME("beginningTime"),
        END_TIME("endTime"),
        DATE("date");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry ref) {
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
            case BEGINNING_TIME -> getBeginningTime();
            case END_TIME -> getEndTime();
            case DATE -> getDate();
        };
    }

}