package edu.wpi.punchy_pegasi.schema;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class OfficeServiceRequestEntry extends RequestEntry {
    @com.jsoniter.annotation.JsonProperty("officerequest")
    private String officeRequest;

    public OfficeServiceRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, String officeRequest, Long employeeID) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status, employeeID);
        this.officeRequest = officeRequest;
    }

    public OfficeServiceRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, String officeRequest, Long employeeID) {
        super(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, employeeID);
        this.officeRequest = officeRequest;
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry>{
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        EMPLOYEE_ID("employeeID"),
        OFFICE_REQUEST("officeRequest");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref){
    return ref.getFromField(this);
}
public String getValueAsString(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref){
    return ref.getFromFieldAsString(this);
}
    public void setValueFromString(edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry ref, String value){
            ref.setFieldFromString(this, value);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID();
            case LOCATION_NAME -> getLocationName();
            case STAFF_ASSIGNMENT -> getStaffAssignment();
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus();
            case EMPLOYEE_ID -> getEmployeeID();
            case OFFICE_REQUEST -> getOfficeRequest();
        };
    }
    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case SERVICE_ID -> setServiceID(UUID.fromString(value));
            case LOCATION_NAME -> setLocationName(Long.parseLong(value));
            case STAFF_ASSIGNMENT -> setStaffAssignment(Long.parseLong(value));
            case ADDITIONAL_NOTES -> setAdditionalNotes(value);
            case STATUS -> setStatus(Status.valueOf(value));
            case EMPLOYEE_ID -> setEmployeeID(Long.parseLong(value));
            case OFFICE_REQUEST -> setOfficeRequest(value);
        };
    }
    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case SERVICE_ID -> getServiceID().toString();
            case LOCATION_NAME -> Long.toString(getLocationName());
            case STAFF_ASSIGNMENT -> Long.toString(getStaffAssignment());
            case ADDITIONAL_NOTES -> getAdditionalNotes();
            case STATUS -> getStatus().name();
            case EMPLOYEE_ID -> Long.toString(getEmployeeID());
            case OFFICE_REQUEST -> getOfficeRequest();
        };
    }

}