package edu.wpi.punchy_pegasi.frontend;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestEntry {
    protected final UUID serviceID;
    protected final String patientName;
    protected final String roomNumber;
    protected final String staffAssignment;
    protected final String additionalNotes;
    protected enum Status {
        NONE,
        PROCESSING,
        DONE
    }
    protected final Status status;

    public RequestEntry(String patientName, String roomNumber, String staffAssignment, String additionalNotes) {
        this.serviceID = UUID.randomUUID();
        this.patientName = patientName;
        this.roomNumber = roomNumber;
        this.staffAssignment = staffAssignment;
        this.additionalNotes = additionalNotes;
        this.status = Status.PROCESSING;
    }
}
