package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OfficeServiceRequestEntry extends RequestEntry {
    private final String officeRequest;
    private final String employeeName;

    public OfficeServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String officeRequest, String employeeName) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.officeRequest = officeRequest;
        this.employeeName = employeeName;
    }

    public OfficeServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String officeRequest, String employeeName) {
        this(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, officeRequest, employeeName);
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case OFFICE_REQUEST -> getOfficeRequest();
            case EMPLOYEE_NAME -> getEmployeeName();
        };
    }

    @lombok.RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        OFFICE_REQUEST("officeRequest"),
        EMPLOYEE_NAME("employeeName");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref) {
            return ref.getFromField(this);
        }
    }

}