package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import lombok.Getter;

import lombok.RequiredArgsConstructor;


import java.util.UUID;

@Getter
public class ConferenceRoomEntry extends RequestEntry {
    private final String beginningTime;
    private final String endTime;

    public ConferenceRoomEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status, String beginningTime, String endTime) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }

    public ConferenceRoomEntry(String roomNumber, String staffAssignment, String additionalNotes, String beginningTime, String endTime) {
        super(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }
    @RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        BEGINNING_TIME("beginningTime"),
        END_TIME("endTime");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry ref){
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
            case BEGINNING_TIME -> getBeginningTime();
            case END_TIME -> getEndTime();
        };
    }

}