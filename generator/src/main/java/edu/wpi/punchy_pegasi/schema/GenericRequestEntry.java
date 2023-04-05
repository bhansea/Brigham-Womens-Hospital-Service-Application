package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(
            UUID serviceID,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
    }
    public GenericRequestEntry(
            String roomNumber,
            String staffAssignment,
            String additionalNotes) {
        super(UUID.randomUUID(),roomNumber, staffAssignment, additionalNotes, Status.PROCESSING);
    }
}
