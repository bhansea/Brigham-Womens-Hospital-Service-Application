package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.Node;

public class CartesianHeuristic implements IHeuristic<Node> {
    @Override
    public double computeCost(Node from, Node to) {
        return Math.sqrt(Math.pow(from.getXcoord() - to.getXcoord(), 2d) + Math.pow(from.getYcoord() - to.getYcoord(), 2d));
    }
}
