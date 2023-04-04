package edu.wpi.punchy_pegasi.backend;

import lombok.Data;

@Data
public class Edge {
    private long uuid;
    private String startNode;
    private String endNode;

    public Edge(long uuid, String startNode, String endNode) {
        this.uuid = uuid;
        this.startNode = startNode;
        this.endNode = endNode;
    }
}
