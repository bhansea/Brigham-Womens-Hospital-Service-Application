package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Move {
    @SchemaID
    private Long uuid;
    private Long nodeID;
    private String longName;
    private LocalDate date;
}
