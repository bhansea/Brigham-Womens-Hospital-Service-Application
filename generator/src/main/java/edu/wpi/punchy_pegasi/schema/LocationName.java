package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LocationName {
    @SchemaID
    @Getter
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
        SERV
    }
}
