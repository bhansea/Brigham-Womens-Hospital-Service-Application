package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class FurnitureRequestEntry extends RequestEntry {

    private final List<String> selectFurniture;

    public FurnitureRequestEntry(Long locationName, Long staffAssignment, String additionalNotes, List<String> selectFurniture) {
        this(UUID.randomUUID(), locationName, staffAssignment, additionalNotes, Status.PROCESSING, selectFurniture);
    }

    public FurnitureRequestEntry(UUID serviceID, Long locationName, Long staffAssignment, String additionalNotes, Status status, List<String> selectFurniture) {
        super(serviceID, locationName, staffAssignment, additionalNotes, status);
        this.selectFurniture = selectFurniture;
    }

    @lombok.RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        LOCATION_NAME("locationName"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        SELECT_FURNITURE("selectFurniture");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry ref){
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
            case SELECT_FURNITURE -> getSelectFurniture();
        };
    }

}