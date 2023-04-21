package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class Dijkstra<K, T extends INode> implements IPathFind<K, T> {

    private final IHeuristic<T> heuristic;

    @Override
    public List<T> findPath(Graph<K, T> graph, T from, T to) throws IllegalStateException {
        Map<T, Double> costMap = new HashMap<>();
        Map<T, T> parentMap = new HashMap<>();
        PriorityQueue<T> queue = new PriorityQueue<>(Comparator.comparingDouble(costMap::get));

        costMap.put(from, 0.0);
        queue.add(from);

        while (!queue.isEmpty()) {
            T current = queue.poll();
            if (current.equals(to)) {
                return getRoute(parentMap, to);
            }
            for (T neighbor : graph.getConnections(current)) {
                double newCost = costMap.get(current) + heuristic.computeCost(current, neighbor);
                if (!costMap.containsKey(neighbor) || newCost < costMap.get(neighbor)) {
                    costMap.put(neighbor, newCost);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        throw new IllegalStateException("Unable to find path");
    }

    private List<T> getRoute(Map<T, T> parentMap, T to) {
        List<T> path = new ArrayList<>();
        T current = to;
        while (parentMap.containsKey(current)) {
            path.add(current);
            current = parentMap.get(current);
        }
        path.add(current);
        Collections.reverse(path);
        return path;
    }
}
