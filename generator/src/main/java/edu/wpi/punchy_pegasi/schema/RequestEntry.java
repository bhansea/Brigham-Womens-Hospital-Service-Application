package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class RequestEntry {
    @SchemaID
    protected final UUID serviceID;
    protected final String patientName;
    protected final String roomNumber;
    protected final String staffAssignment;
    protected final String additionalNotes;
    protected final Status status;

    public enum Status {
        NONE,
        PROCESSING,
        DONE
    }
}
