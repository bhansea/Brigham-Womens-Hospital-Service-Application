package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signage {
    @SchemaID
    private Long uuid;
    private String longName;
    private DirectionType directionType;

    public enum DirectionType{
        UP,
        DOWN,
        LEFT,
        RIGHT,
        HERE
    }
}
