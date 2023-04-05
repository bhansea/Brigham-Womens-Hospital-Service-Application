package edu.wpi.punchy_pegasi.schema;

import lombok.Data;

import java.util.UUID;

@Data
public class LocationName {
//    private UUID uuid;
    private String longName;
    private String shortName;
    private NodeType nodeType;

    public LocationName(String longName, String shortName, NodeType nodeType) {
//        this.uuid = uuid;
        this.longName = longName;
        this.shortName = shortName;
        this.nodeType = nodeType;
    }


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
