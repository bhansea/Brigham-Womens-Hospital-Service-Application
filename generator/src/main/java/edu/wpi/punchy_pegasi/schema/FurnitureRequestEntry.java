package edu.wpi.punchy_pegasi.schema;

import javax.swing.text.TableView;
import java.util.List;
import java.util.UUID;

public class FurnitureRequestEntry extends RequestEntry{

    private final List<String> selectFurniture;

    public FurnitureRequestEntry(
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            List<String> selectFurniture) {
        this(UUID.randomUUID(),
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                selectFurniture);
    }

    public FurnitureRequestEntry(
            UUID serviceID,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status,
            List<String> selectFurniture) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.selectFurniture = selectFurniture;
    }

}
