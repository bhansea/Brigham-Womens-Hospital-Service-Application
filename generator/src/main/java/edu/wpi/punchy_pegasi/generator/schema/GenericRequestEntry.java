package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class GenericRequestEntry extends RequestEntry {
    public GenericRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, Long employeeID) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, employeeID);
    }

    public GenericRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, Long employeeID) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, employeeID);
    }
}
