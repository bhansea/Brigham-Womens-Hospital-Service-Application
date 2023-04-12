package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
    }

    public GenericRequestEntry(Long locationName, Long staffAssignment, String additionalNotes) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING);
    }
}
