package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
public class ConferenceRoomEntry extends RequestEntry {
    private final String beginningTime;
    private final String endTime;
    private final LocalDate date;

    public ConferenceRoomEntry(UUID serviceID, String locationName, String staffAssignment, String additionalNotes, Status status, String beginningTime, String endTime, LocalDate date) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;

    }

    public ConferenceRoomEntry(String locationName, String staffAssignment, String additionalNotes, String beginningTime, String endTime, LocalDate date) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING);
        this.beginningTime = beginningTime;
        this.endTime = endTime;
        this.date = date;
    }
}
