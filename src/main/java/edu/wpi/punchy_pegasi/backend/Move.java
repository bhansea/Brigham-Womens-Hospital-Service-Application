package edu.wpi.punchy_pegasi.backend;

import lombok.Data;

@Data
public class Move {
    private long uuid;
    private long nodeID;
    private String longName;
    private String date;

    Move(long uuid, long nodeID, String longName, String date) {
        this.uuid = uuid;
        this.nodeID = nodeID;
        this.longName = longName;
        this.date = date;
    }


}
