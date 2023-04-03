package edu.wpi.punchy_pegasi.frontend;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(
            UUID serviceID,
            String patientName,
            String roomNumber,
            String additionalNotes) {
        super(serviceID, patientName, roomNumber, additionalNotes);
    }
    public GenericRequestEntry(
            String patientName,
            String roomNumber,
            String additionalNotes) {
        super(UUID.randomUUID(), patientName, roomNumber, additionalNotes);
    }
}
