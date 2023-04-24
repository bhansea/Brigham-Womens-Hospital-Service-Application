package edu.wpi.punchy_pegasi.schema;

import edu.wpi.punchy_pegasi.backend.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signage {
    @SchemaID
    @com.jsoniter.annotation.JsonProperty("uuid")
    private Long uuid;
    @com.jsoniter.annotation.JsonProperty("signname")
    private String signName;
    @com.jsoniter.annotation.JsonProperty("longname")
    private String longName;
    @com.jsoniter.annotation.JsonProperty("directiontype")
    private DirectionType directionType;

    public enum DirectionType{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        HERE
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Signage>{
        UUID("uuid"),
        SIGN_NAME("signName"),
        LONG_NAME("longName"),
        DIRECTION_TYPE("directionType");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.Signage ref){
    return ref.getFromField(this);
}
public String getValueAsString(edu.wpi.punchy_pegasi.schema.Signage ref){
    return ref.getFromFieldAsString(this);
}
    public void setValueFromString(edu.wpi.punchy_pegasi.schema.Signage ref, String value){
            ref.setFieldFromString(this, value);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case UUID -> getUuid();
            case SIGN_NAME -> getSignName();
            case LONG_NAME -> getLongName();
            case DIRECTION_TYPE -> getDirectionType();
        };
    }
    public void setFieldFromString(Field field, String value) {
        switch (field) {
            case UUID -> setUuid(Long.parseLong(value));
            case SIGN_NAME -> setSignName(value);
            case LONG_NAME -> setLongName(value);
            case DIRECTION_TYPE -> setDirectionType(DirectionType.valueOf(value));
        };
    }
    public String getFromFieldAsString(Field field) {
        return switch (field) {
            case UUID -> Long.toString(getUuid());
            case SIGN_NAME -> getSignName();
            case LONG_NAME -> getLongName();
            case DIRECTION_TYPE -> getDirectionType().name();
        };
    }

}