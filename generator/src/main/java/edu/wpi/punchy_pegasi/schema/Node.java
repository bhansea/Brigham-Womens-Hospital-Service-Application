package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Node implements INode {
    private Long nodeID;
    private Integer xcoord;
    private Integer ycoord;
    private String floor;
    private String building;

    @Override
    public String getId() {
        return null;
    }
}
