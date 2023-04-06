package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.RequiredArgsConstructor;

import lombok.Data;

@Data
@AllArgsConstructor
public class Node implements INode {
        private Long nodeID;
    private Integer xcoord;
    private Integer ycoord;
    private String floor;
    private String building;

    @Override
    public String getId() {
        return null;
    }
    @RequiredArgsConstructor
    public enum Field {
        NODE_ID("nodeID"),
        XCOORD("xcoord"),
        YCOORD("ycoord"),
        FLOOR("floor"),
        BUILDING("building");
        @Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.Node ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case NODE_ID -> getNodeID();
            case XCOORD -> getXcoord();
            case YCOORD -> getYcoord();
            case FLOOR -> getFloor();
            case BUILDING -> getBuilding();
        };
    }

}