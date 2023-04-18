package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Palgo<K, T extends INode> {
    private final Graph<K, T> graph;
    private final IHeuristic<T> nextNodeScorer;
    private final IHeuristic<T> targetScorer;
    private IPathFind<T> pathfinder;

    public Palgo(Graph<K, T> graph, IHeuristic<T> nextNodeScorer, IHeuristic<T> targetScorer, IPathFind<T> pathfinder) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
        this.pathfinder = pathfinder;
    }

    public void setPathfinder(IPathFind<T> pathfinder) {
        this.pathfinder = pathfinder;
    }

    public List<T> findPath(T from, T to) throws IllegalStateException {
        return pathfinder.findPath(from, to);
    }
}
