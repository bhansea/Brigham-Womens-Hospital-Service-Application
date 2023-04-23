package edu.wpi.punchy_pegasi.schema;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signage {

    @com.jsoniter.annotation.JsonProperty("longname")
    private String longName;
    @com.jsoniter.annotation.JsonProperty("directiontype")
    private DirectionType directionType;

    public enum DirectionType {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        HERE
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Signage> {
        LONG_NAME("longName"),
        DIRECTION_TYPE("directionType");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Signage ref) {
            return ref.getFromField(this);
        }
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case LONG_NAME -> getLongName();
            case DIRECTION_TYPE -> getDirectionType();
        };
    }

}