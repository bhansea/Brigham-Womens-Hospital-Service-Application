package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.RequiredArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class LocationName {
        private Long uuid;
    private String longName;
    private String shortName;
    private NodeType nodeType;
    public enum NodeType {
        HALL,
        ELEV,
        REST,
        STAI,
        DEPT,
        LABS,
        INFO,
        CONF,
        EXIT,
        RETL,
        SERV,
        BATH
    }
    @RequiredArgsConstructor
    public enum Field {
        UUID("uuid"),
        LONG_NAME("longName"),
        SHORT_NAME("shortName"),
        NODE_TYPE("nodeType");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.LocationName ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case UUID -> getUuid();
            case LONG_NAME -> getLongName();
            case SHORT_NAME -> getShortName();
            case NODE_TYPE -> getNodeType();
        };
    }

}