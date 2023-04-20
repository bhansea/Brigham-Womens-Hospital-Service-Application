package edu.wpi.punchy_pegasi.backend.pathfinding;

import edu.wpi.punchy_pegasi.schema.INode;

import java.util.List;

public interface IPathFind<K, T extends INode> {

    List<T> findPath(Graph<K, T> graph, T from, T to) throws IllegalStateException;
}
