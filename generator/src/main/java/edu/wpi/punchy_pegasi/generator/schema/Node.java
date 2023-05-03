package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class Node implements INode {
    @SchemaID
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
            case "L1" -> 2;
            case "L2" -> 1;
            case "1" -> 3;
            case "2" -> 4;
            case "3" -> 5;
            default -> -1;
        };
    }
}
