package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FurnitureRequestEntry extends RequestEntry {

    private final List<String> selectFurniture;

    public FurnitureRequestEntry(String roomNumber, String staffAssignment, String additionalNotes, List<String> selectFurniture) {
        this(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING, selectFurniture);
    }

    public FurnitureRequestEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status, List<String> selectFurniture) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.selectFurniture = selectFurniture;
    }

}
