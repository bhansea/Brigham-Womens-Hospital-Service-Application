package edu.wpi.punchy_pegasi.schema;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Signage {

    private String longName;
    private DirectionType directionType;

    public Object getFromField(Field field) {
        return switch (field) {
            case LONG_NAME -> getLongName();
            case DIRECTION_TYPE -> getDirectionType();
        };
    }

    public enum DirectionType {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    @lombok.RequiredArgsConstructor
    public enum Field {
        LONG_NAME("longName"),
        DIRECTION_TYPE("directionType");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Signage ref) {
            return ref.getFromField(this);
        }
    }

}