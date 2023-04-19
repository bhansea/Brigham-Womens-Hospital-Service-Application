package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;

import java.util.*;
@RequiredArgsConstructor
public class AStar<K, T extends INode> implements IPathFind<K, T> {
    private final IHeuristic<T> nextNodeScorer;
    private final IHeuristic<T> targetScorer;
    @Override
    public List<T> findPath(Graph<K, T> graph, T from, T to) throws IllegalStateException {
        //Priority queue is required for O(Log(N))
        Queue<RouteNode<T>> queue = new PriorityQueue<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
        queue.add(start);
        allNodes.put(from, start);

        while (!queue.isEmpty()) {
            var current = queue.poll();
            if (current.getCurrent().equals(to))
                return genRoute(allNodes, current);
            for (var adjNode : graph.getConnections(current.getCurrent())) {
                var nextNode = allNodes.getOrDefault(adjNode, new RouteNode<>(adjNode));
                allNodes.put(adjNode, nextNode);

                double newScore = current.getRouteScore() + nextNodeScorer.computeCost(current.getCurrent(), adjNode);
                if (newScore < nextNode.getRouteScore()) {
                    nextNode.setPrevious(current.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetScorer.computeCost(adjNode, to));
                    queue.add(nextNode);
                }
            }
        }
        throw new IllegalStateException("No route found");
    }

    public List<T> genRoute(Map<T, RouteNode<T>> nodes, RouteNode<T> next) {
        List<T> route = new ArrayList<T>();
        var current = next;
        do {
            route.add(0, current.getCurrent());
            current = nodes.get(current.getPrevious());
        } while (current != null);
        return route;
    }
}