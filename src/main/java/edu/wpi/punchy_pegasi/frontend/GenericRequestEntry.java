package edu.wpi.punchy_pegasi.frontend;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(
            UUID serviceID,
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status) {
        super(serviceID, patientName, roomNumber, staffAssignment, additionalNotes, status);
    }
    public GenericRequestEntry(
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes) {
        super(UUID.randomUUID(), patientName, roomNumber, staffAssignment, additionalNotes, Status.PROCESSING);
    }
}
