package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.UUID;

@Getter
public class OfficeServiceRequestEntry extends RequestEntry {
    private final String officeRequest;
    private final String employeeName;

    public OfficeServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String invalidText, String officeRequest, String employeeName) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, invalidText);
        this.officeRequest = officeRequest;
        this.employeeName = employeeName;
    }

    public OfficeServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String invalidText, String officeRequest, String employeeName) {
        this(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, invalidText, officeRequest, employeeName);
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        INVALID_TEXT("invalidText"),
        OFFICE_REQUEST("officeRequest"),
        EMPLOYEE_NAME("employeeName");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case INVALID_TEXT -> getInvalidText();
            case OFFICE_REQUEST -> getOfficeRequest();
            case EMPLOYEE_NAME -> getEmployeeName();
        };
    }

}