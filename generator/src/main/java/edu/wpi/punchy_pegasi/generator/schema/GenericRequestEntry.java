package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, String locationName, String staffAssignment, String additionalNotes, Status status) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
    }

    public GenericRequestEntry(String locationName, String staffAssignment, String additionalNotes) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING);
    }
}
