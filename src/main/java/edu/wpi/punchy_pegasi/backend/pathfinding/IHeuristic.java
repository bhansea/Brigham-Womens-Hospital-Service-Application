package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;

public interface IHeuristic<T extends INode> {
    double computeCost(T from, T to);
}

