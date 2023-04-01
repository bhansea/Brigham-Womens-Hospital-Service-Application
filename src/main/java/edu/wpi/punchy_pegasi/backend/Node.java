package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.backend.pathfinding.INode;
import lombok.Data;

@Data
public class Node implements INode {
    private int xcoord;
    private int ycoord;

    @Override
    public String getId() {
        return null;
    }
}
