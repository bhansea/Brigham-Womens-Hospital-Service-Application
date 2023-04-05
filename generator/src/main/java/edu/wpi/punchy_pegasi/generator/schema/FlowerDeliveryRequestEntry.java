package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private final String flowerSize;
    private final String flowerType;
    private final String flowerAmount;
    private final String patientName;

    public FlowerDeliveryRequestEntry(UUID serviceID, String patientName, String roomNumber, String staffAssignment, String additionalNotes, Status status, String flowerSize, String flowerAmount, String flowerType) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(String patientName, String roomNumber, String staffAssignment, String additionalNotes, String flowerSize, String flowerAmount, String flowerType) {
        this(UUID.randomUUID(), patientName, roomNumber, staffAssignment, additionalNotes, Status.PROCESSING, flowerSize, flowerAmount, flowerType);
    }
}
