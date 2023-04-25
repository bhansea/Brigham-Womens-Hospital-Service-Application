package edu.wpi.punchy_pegasi.generator.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class FlowerDeliveryRequestEntry extends RequestEntry {
    private String flowerSize;
    private String flowerType;
    private String flowerAmount;
    private String patientName;

    public FlowerDeliveryRequestEntry(UUID serviceID, String patientName, Long locationName, Long staffAssignment, String additionalNotes, Status status, String flowerSize, String flowerAmount, String flowerType, Long employeeID) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, employeeID);
        this.flowerSize = flowerSize;
        this.flowerAmount = flowerAmount;
        this.flowerType = flowerType;
        this.patientName = patientName;
    }

    public FlowerDeliveryRequestEntry(String patientName, Long locationName, Long staffAssignment, String additionalNotes, String flowerSize, String flowerAmount, String flowerType, Long employeeID) {
        this(UUID.randomUUID(), patientName, locationName, staffAssignment, additionalNotes, Status.PROCESSING, flowerSize, flowerAmount, flowerType, employeeID);
    }
}
