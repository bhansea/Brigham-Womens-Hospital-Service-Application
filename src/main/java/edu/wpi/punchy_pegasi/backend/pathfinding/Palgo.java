package edu.wpi.punchy_pegasi.backend.pathfinding;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Palgo<T extends INode> {
    private final Graph<T> graph;
    private final IHeuristic<T> nextNodeScorer;
    private final IHeuristic<T> targetScorer;

    public Palgo(Graph<T> graph, IHeuristic<T> nextNodeScorer, IHeuristic<T> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }


    public List<T> BFS(T from, T to) throws IllegalStateException {
        Queue<RouteNode<T>> queue = new LinkedList<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        var start = new RouteNode<>(from);
        queue.add(start);
        allNodes.put(from, start);
        while (!queue.isEmpty()) {
            var current = queue.poll();
            for (var adjNode : graph.getConnections(current.getCurrent())) {
                if(allNodes.containsKey(adjNode))
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

    public List<T> AStar(T from, T to) throws IllegalStateException {
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

    private List<T> genRoute(Map<T, RouteNode<T>> nodes, RouteNode<T> next) {
        List<T> route = new ArrayList<T>();
        var current = next;
        do {
            route.add(0, current.getCurrent());
            current = nodes.get(current.getPrevious());
        } while (current != null);
        return route;
    }
}
