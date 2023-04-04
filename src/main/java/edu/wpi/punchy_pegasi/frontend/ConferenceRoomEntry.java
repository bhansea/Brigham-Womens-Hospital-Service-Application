package edu.wpi.punchy_pegasi.frontend;

import java.util.UUID;

public class ConferenceRoomEntry extends RequestEntry {

    private final String beginningTime;
    private final String endTime;

    //String patientName, String roomNumber, String additionalNotes
    public ConferenceRoomEntry(UUID serviceID,
                               String patientName,
                               String roomNumber,
                               String additionalNotes,
                               String staffAssignment,
                               Status status,
                               String beginningTime,
                               String endTime){
        super(serviceID, patientName, roomNumber,additionalNotes,staffAssignment,status);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
    }


    public ConferenceRoomEntry(
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            String beginningTime,
            String endTime) {
        this(UUID.randomUUID(),
                patientName,
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                beginningTime,
                endTime);
    }

}
