package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

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
}
