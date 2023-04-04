package edu.wpi.punchy_pegasi.schema;

import lombok.Data;

@Data
public class Move {
    private Long uuid;
    private Long nodeID;
    private String longName;
    private String date;

    public Move(Long uuid, Long nodeID, String longName, String date) {
        this.uuid = uuid;
        this.nodeID = nodeID;
        this.longName = longName;
        this.date = date;
    }


}
