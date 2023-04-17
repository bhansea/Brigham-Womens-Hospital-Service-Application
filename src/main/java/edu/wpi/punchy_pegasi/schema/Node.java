package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
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
        return nodeID.toString();
    }

    public int getFloorNum() {
        return switch (floor) {
            case "L1" -> 1;
            case "L2" -> 2;
            case "1" -> 3;
            case "2" -> 4;
            case "3" -> 5;
            default -> -1;
        };
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Node> {
        NODE_ID("nodeID"),
        XCOORD("xcoord"),
        YCOORD("ycoord"),
        FLOOR("floor"),
        BUILDING("building");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Node ref) {
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