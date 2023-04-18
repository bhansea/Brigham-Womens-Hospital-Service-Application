package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
    }

    public GenericRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String invalidText) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText);
    }
}
