package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;

import java.util.*;

public class BFS<K, T extends INode> implements IPathFind<T> {
    private final Graph<K, T> graph;

    public BFS(Graph<K, T> graph) {
        this.graph = graph;
    }

    @Override
    public List<T> findPath(T from, T to) throws IllegalStateException {
        Queue<RouteNode<T>> queue = new LinkedList<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        var start = new RouteNode<>(from);
        queue.add(start);
        allNodes.put(from, start);
        while (!queue.isEmpty()) {
            var current = queue.poll();
            for (var adjNode : graph.getConnections(current.getCurrent())) {
                if (allNodes.containsKey(adjNode))
                    continue;
                var nextNode = new RouteNode<>(adjNode);
                nextNode.setPrevious(current.getCurrent());
                allNodes.put(adjNode, nextNode);
                if (nextNode.getCurrent().equals(to))
                    return genRoute(allNodes, nextNode);
                queue.add(nextNode);
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