package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OfficeServiceRequestEntry extends RequestEntry {
    private final String officeRequest;
    private final String employeeName;
    public OfficeServiceRequestEntry(
            UUID serviceID,
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            Status status,
            String officeRequest,
            String employeeName) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.officeRequest = officeRequest;
        this.employeeName = employeeName;
    }
    public OfficeServiceRequestEntry(
            String roomNumber,
            String staffAssignment,
            String additionalNotes,
            String officeRequest,
            String employeeName) {
        this(UUID.randomUUID(),
                roomNumber,
                staffAssignment,
                additionalNotes,
                Status.PROCESSING,
                officeRequest,
                employeeName);
    }

    public void printOfficeReq() {
        System.out.println("Patient Name: " + employeeName);
        System.out.println("Additional Notes: " + additionalNotes);
        System.out.println("Office Request: " + officeRequest);
        System.out.println("Room Number: " + roomNumber);
    }
}
