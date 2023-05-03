package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class LocationName {
    @SchemaID
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
}
