package edu.wpi.punchy_pegasi.backend;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class Node {
    private long nodeID;
    private int xcoord;
    private int ycoord;
    private String floor;
    private String building;

    public Node(long nodeID, int xcoord, int ycoord, String floor, String building){
        this.nodeID = nodeID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
    }


}
