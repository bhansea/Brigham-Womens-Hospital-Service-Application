package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.backend.pathfinding.INode;
import lombok.Data;

@Data
public class Node implements INode {
    private long nodeID;
    private int xcoord;
    private int ycoord;
    private String floor;
    private String building;

    public Node(long nodeID, int xcoord, int ycoord, String floor, String building) {
        this.nodeID = nodeID;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.floor = floor;
        this.building = building;
    }

    @Override
    public String getId() {
        return null;
    }
}
