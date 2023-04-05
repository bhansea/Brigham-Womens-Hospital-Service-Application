package edu.wpi.punchy_pegasi.schema;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ConferenceRoomEntry extends RequestEntry {

    private final String beginningTime;
    private final String endTime;

    //String patientName, String roomNumber, String additionalNotes
    public ConferenceRoomEntry(UUID serviceID,
                               String roomNumber,
                               String additionalNotes,
                               String staffAssignment,
                               Status status,
                               String beginningTime,
                               String endTime){
        super(serviceID, roomNumber,additionalNotes,staffAssignment,status);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }


    public ConferenceRoomEntry(
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            String beginningTime,
            String endTime) {
        this(UUID.randomUUID(),
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                beginningTime,
                endTime);
    }

}
