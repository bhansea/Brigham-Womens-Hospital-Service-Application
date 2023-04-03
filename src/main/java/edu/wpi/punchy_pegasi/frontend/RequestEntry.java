package edu.wpi.punchy_pegasi.frontend;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestEntry {
    protected final UUID serviceID;
    protected final String patientName;
    protected final String roomNumber;
    protected final String additionalNotes;

    public RequestEntry(String patientName, String roomNumber, String additionalNotes) {
        this.serviceID = UUID.randomUUID();
        this.patientName = patientName;
        this.roomNumber = roomNumber;
        this.additionalNotes = additionalNotes;
    }
}
