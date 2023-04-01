package edu.wpi.punchy_pegasi.frontend;

public class RequestEntry {
    protected final String patientName;
    protected final String roomNumber;
    protected final String additionalNotes;

    public RequestEntry(String patientName, String roomNumber, String additionalNotes) {
        this.patientName = patientName;
        this.roomNumber = roomNumber;
        this.additionalNotes = additionalNotes;
    }
}
