package edu.wpi.punchy_pegasi.schema;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Move {
    @SchemaID
    private Long uuid;
    private Long nodeID;
    private String longName;
    private String date;
}
