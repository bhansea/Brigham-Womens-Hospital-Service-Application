package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ConferenceRoomEntry extends RequestEntry {
    private final String beginningTime;
    private final String endTime;
    private final LocalDate date;
    private final String amountOfParticipants;

    public ConferenceRoomEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText, String beginningTime, String endTime, LocalDate date, String amountOfParticipants) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;
        this.amountOfParticipants = amountOfParticipants;
    }

    public ConferenceRoomEntry(Long locationName, Long staffAssignment, String additionalNotes, String invalidText, String beginningTime, String endTime, LocalDate date, String amountOfParticipants) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;
        this.amountOfParticipants = amountOfParticipants;
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        INVALID_TEXT("invalidText"),
        BEGINNING_TIME("beginningTime"),
        END_TIME("endTime"),
        DATE("date"),
        AMOUNT_OF_PARTICIPANTS("amountOfParticipants");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry ref){
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
            case BEGINNING_TIME -> getBeginningTime();
            case END_TIME -> getEndTime();
            case DATE -> getDate();
            case AMOUNT_OF_PARTICIPANTS -> getAmountOfParticipants();
        };
    }

}