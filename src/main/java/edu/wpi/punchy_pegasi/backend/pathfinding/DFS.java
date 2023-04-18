package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;

import java.util.*;

public class DFS<K, T extends INode> implements IPathFind<T> {
    private final Graph<K, T> graph;

    public DFS(Graph<K, T> graph) {
        this.graph = graph;
    }

    @Override
    public List<T> findPath(T from, T to) throws IllegalStateException {
        Stack<RouteNode<T>> stack = new Stack<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        var start = new RouteNode<>(from);
        stack.push(start);
        allNodes.put(from, start);

        while (!stack.isEmpty()) {
            var current = stack.pop();
            for (var adjNode : graph.getConnections(current.getCurrent())) {
                if (allNodes.containsKey(adjNode))
                    continue;
                var nextNode = new RouteNode<>(adjNode);
                nextNode.setPrevious(current.getCurrent());
                allNodes.put(adjNode, nextNode);
                if (nextNode.getCurrent().equals(to))
                    return genRoute(allNodes, nextNode);
                stack.push(nextNode);
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