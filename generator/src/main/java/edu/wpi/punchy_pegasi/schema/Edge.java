package edu.wpi.punchy_pegasi.schema;

import lombok.Data;

@Data
public class Edge {
    private Long uuid;
    private Long startNode;
    private Long endNode;

    public Edge(Long uuid, Long startNode, Long endNode) {
        this.uuid = uuid;
        this.startNode = startNode;
        this.endNode = endNode;
    }
}
