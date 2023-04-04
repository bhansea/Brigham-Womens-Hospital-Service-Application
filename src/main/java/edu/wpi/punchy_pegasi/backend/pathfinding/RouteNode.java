package edu.wpi.punchy_pegasi.backend.pathfinding;

import lombok.Getter;
import lombok.Setter;

public class RouteNode<T extends INode> implements Comparable<RouteNode> {
    @Getter
    private final T current;
    @Getter
    @Setter
    private T previous;
    @Getter
    @Setter
    private double routeScore;
    @Getter
    @Setter
    private double estimatedScore;

    RouteNode(T current) {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    RouteNode(T current, T previous, double routeScore, double estimatedScore) {
        this.current = current;
        this.previous = previous;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode o) {
        return Double.compare(estimatedScore, o.estimatedScore);
    }
}
