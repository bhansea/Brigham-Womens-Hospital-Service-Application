package edu.wpi.punchy_pegasi.frontend;

import lombok.Getter;

@Getter
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private final String flowerSize;
    private final String flowerType;
    private final String flowerAmount;

    public FlowerDeliveryRequestEntry(
            String patientName,
            String additionalNotes,
            String flowerSize,
            String roomNumber,
            String flowerAmount,
            String flowerType) {
        super(patientName, roomNumber, additionalNotes);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
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
