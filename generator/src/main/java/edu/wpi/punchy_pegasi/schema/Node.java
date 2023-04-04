package edu.wpi.punchy_pegasi.schema;

import lombok.Data;

@Data
public class Node implements INode {
    private Long nodeID;
    private Integer xcoord;
    private Integer ycoord;
    private String floor;
    private String building;

    public Node(Long nodeID, Integer xcoord, Integer ycoord, String floor, String building) {
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
