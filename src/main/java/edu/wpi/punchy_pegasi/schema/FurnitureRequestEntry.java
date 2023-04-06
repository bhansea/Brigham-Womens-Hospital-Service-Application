package edu.wpi.punchy_pegasi.schema;

import lombok.Getter;
import lombok.Getter;

import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.UUID;

@Getter
public class FurnitureRequestEntry extends RequestEntry {

    private final List<String> selectFurniture;

    public FurnitureRequestEntry(String roomNumber, String staffAssignment, String additionalNotes, List<String> selectFurniture) {
        this(UUID.randomUUID(), roomNumber, staffAssignment, additionalNotes, Status.PROCESSING, selectFurniture);
    }

    public FurnitureRequestEntry(UUID serviceID, String roomNumber, String staffAssignment, String additionalNotes, Status status, List<String> selectFurniture) {
        super(serviceID, roomNumber, staffAssignment, additionalNotes, status);
        this.selectFurniture = selectFurniture;
    }

    @RequiredArgsConstructor
    public enum Field {
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status"),
        SELECT_FURNITURE("selectFurniture");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry ref){
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
            case SELECT_FURNITURE -> getSelectFurniture();
        };
    }

}