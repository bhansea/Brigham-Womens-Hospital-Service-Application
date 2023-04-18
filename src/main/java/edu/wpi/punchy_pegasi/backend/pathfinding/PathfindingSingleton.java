package edu.wpi.punchy_pegasi.backend.pathfinding;

import lombok.Getter;

public enum PathfindingSingleton {
    @Getter
    SINGLETON;
    @Getter
    private IPathFind algorithm;
    public void setPathfindingAlgo(IPathFind algorithm){
        this.algorithm = algorithm;
    }
}
