package edu.wpi.punchy_pegasi.schema;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Move {

    @com.jsoniter.annotation.JsonProperty("uuid")
    private Long uuid;
    @com.jsoniter.annotation.JsonProperty("nodeid")
    private Long nodeID;
    @com.jsoniter.annotation.JsonProperty("locationid")
    private Long locationID;
    @com.jsoniter.annotation.JsonProperty("date")
    private LocalDate date;

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Move> {
        UUID("uuid"),
        NODE_ID("nodeID"),
        LOCATION_ID("locationID"),
        DATE("date");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Move ref) {
            return ref.getFromField(this);
        }
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case UUID -> getUuid();
            case NODE_ID -> getNodeID();
            case LOCATION_ID -> getLocationID();
            case DATE -> getDate();
        };
    }

}