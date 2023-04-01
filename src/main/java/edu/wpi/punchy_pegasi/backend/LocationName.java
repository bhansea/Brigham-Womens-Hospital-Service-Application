package edu.wpi.punchy_pegasi.backend;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationName {
    private String longName;
    private String shortName;
    private NodeType nodeType;

    public LocationName(String longName, String shortName, NodeType nodeType){
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
