package edu.wpi.punchy_pegasi;

public class FlowerDeliveryRequestEntry {

    private final String patientName;
    private final String additionalNotes;
    private final String flowerSize;
    private final String flowerType;
    private final String roomNumber;
    private final String flowerAmount;

    public FlowerDeliveryRequestEntry(
            String patientName,
            String additionalNotes,
            String flowerSize,
            String roomNumber,
            String flowerAmount,
            String flowerType) {
        this.patientName = patientName;
        this.additionalNotes = additionalNotes;
        this.flowerSize = flowerSize;
        this.roomNumber = roomNumber;
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
