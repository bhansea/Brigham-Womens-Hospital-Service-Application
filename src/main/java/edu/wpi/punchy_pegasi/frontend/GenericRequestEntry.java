package edu.wpi.punchy_pegasi.frontend;

import lombok.Getter;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(
            String patientName,
            String roomName,
            String additionalNotes) {
        super(patientName, roomName, additionalNotes);
    }
}
