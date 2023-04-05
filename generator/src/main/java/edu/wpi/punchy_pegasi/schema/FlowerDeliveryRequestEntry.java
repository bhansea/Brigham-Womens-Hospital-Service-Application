package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private final String flowerSize;
    private final String flowerType;
    private final String flowerAmount;
    private final String patientName;

    public FlowerDeliveryRequestEntry(
            UUID serviceID,
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status,
            String flowerSize,
            String flowerAmount,
            String flowerType) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(
            String patientName,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            String flowerSize,
            String flowerAmount,
            String flowerType) {
        this(UUID.randomUUID(),
                patientName,
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                flowerSize,
                flowerAmount,
                flowerType);
    }


    public void printFlowerReq() {
        System.out.println("Patient Name: " + patientName);
        System.out.println("Additional Notes: " + additionalNotes);
        System.out.println("Flower Size: " + flowerSize);
        System.out.println("Room Number: " + roomNumber);
        System.out.println("Flower Amount: " + flowerAmount);
        System.out.println("Flower Type: " + flowerType);
    }
}
