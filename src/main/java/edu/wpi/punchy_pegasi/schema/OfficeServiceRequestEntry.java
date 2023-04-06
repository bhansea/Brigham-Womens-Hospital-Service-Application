package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import lombok.Getter;

import lombok.RequiredArgsConstructor;


import java.util.UUID;

@Getter
public class OfficeServiceRequestEntry extends RequestEntry {
    private final String officeRequest;
    private final String employeeName;

    public OfficeServiceRequestEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status, String officeRequest, String employeeName) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.officeRequest = officeRequest;
        this.employeeName = employeeName;
    }

    public OfficeServiceRequestEntry(String roomNumber, String staffAssignment, String additionalNotes, String officeRequest, String employeeName) {
        this(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING, officeRequest, employeeName);
    }
    @RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        OFFICE_REQUEST("officeRequest"),
        EMPLOYEE_NAME("employeeName");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case ROOM_NUMBER -> getRoomNumber();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case OFFICE_REQUEST -> getOfficeRequest();
            case EMPLOYEE_NAME -> getEmployeeName();
        };
    }

}