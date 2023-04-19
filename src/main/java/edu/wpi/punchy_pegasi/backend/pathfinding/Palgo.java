package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Palgo<K, T extends INode> {
    private final Graph<K, T> graph;
    private final IHeuristic<T> nextNodeScorer;
    private final IHeuristic<T> targetScorer;

    public Palgo(Graph<K, T> graph, IHeuristic<T> nextNodeScorer, IHeuristic<T> targetScorer) {
        this(graph, nextNodeScorer, targetScorer, null);
    }

    public Palgo(Graph<K, T> graph, IHeuristic<T> nextNodeScorer, IHeuristic<T> targetScorer, IPathFind<K, T> pathfinder) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }

    public List<T> findPath(T from, T to) throws IllegalStateException {
        return null;
    }
}
