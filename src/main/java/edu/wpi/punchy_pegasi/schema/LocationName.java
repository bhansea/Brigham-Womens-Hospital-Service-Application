package edu.wpi.punchy_pegasi.schema;

import edu.wpi.punchy_pegasi.backend.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationName {
    @SchemaID
    @com.jsoniter.annotation.JsonProperty("uuid")
    private Long uuid;
    @com.jsoniter.annotation.JsonProperty("longname")
    private String longName;
    @com.jsoniter.annotation.JsonProperty("shortname")
    private String shortName;
    @com.jsoniter.annotation.JsonProperty("nodetype")
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
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.LocationName>{
        UUID("uuid"),
        LONG_NAME("longName"),
        SHORT_NAME("shortName"),
        NODE_TYPE("nodeType");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.LocationName ref){
    return ref.getFromField(this);
}
public String getValueAsString(edu.wpi.punchy_pegasi.schema.LocationName ref){
    return ref.getFromFieldAsString(this);
}
    public void setValueFromString(edu.wpi.punchy_pegasi.schema.LocationName ref, String value){
            ref.setFieldFromString(this, value);
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
    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case UUID -> setUuid(Long.parseLong(value));
            case LONG_NAME -> setLongName(value);
            case SHORT_NAME -> setShortName(value);
            case NODE_TYPE -> setNodeType(NodeType.valueOf(value));
        };
    }
    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case UUID -> Long.toString(getUuid());
            case LONG_NAME -> getLongName();
            case SHORT_NAME -> getShortName();
            case NODE_TYPE -> getNodeType().name();
        };
    }

}