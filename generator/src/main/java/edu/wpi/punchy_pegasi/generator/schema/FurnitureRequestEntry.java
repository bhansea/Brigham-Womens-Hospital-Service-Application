package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FurnitureRequestEntry extends RequestEntry {

    private final List<String> selectFurniture;

    public FurnitureRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String invalidText, List<String> selectFurniture) {
        this(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText, selectFurniture);
    }

    public FurnitureRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText, List<String> selectFurniture) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
        this.selectFurniture = selectFurniture;
    }

}
