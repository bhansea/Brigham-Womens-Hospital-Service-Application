package edu.wpi.punchy_pegasi.schema;


import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntry {

    @com.jsoniter.annotation.JsonProperty("serviceid")
    protected UUID serviceID;
    @com.jsoniter.annotation.JsonProperty("locationname")
    protected Long locationName;
    @com.jsoniter.annotation.JsonProperty("staffassignment")
    protected Long staffAssignment;
    @com.jsoniter.annotation.JsonProperty("additionalnotes")
    protected String additionalNotes;
    @Setter
    @com.jsoniter.annotation.JsonProperty("status")
    protected Status status;
    @com.jsoniter.annotation.JsonProperty("employeeid")
    protected Long employeeID;

    public enum Status {
        NONE,
        PROCESSING,
        DONE
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.RequestEntry> {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        EMPLOYEE_ID("employeeID");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.RequestEntry ref) {
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
            case EMPLOYEE_ID -> getEmployeeID();
        };
    }

}