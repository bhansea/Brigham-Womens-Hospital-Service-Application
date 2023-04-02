package edu.wpi.punchy_pegasi.backend.pathfinding;

public interface IHeuristic<T extends INode> {
    double computeCost(T from, T to);
}

