package edu.wpi.punchy_pegasi.backend.pathfinding;

import java.util.List;

public interface IPathFind<T> {

    List<T> findPath(T from, T to) throws IllegalStateException;
}
