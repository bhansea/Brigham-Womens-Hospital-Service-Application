package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;

import java.util.UUID;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class Edge {
    @SchemaID
    private UUID uuid;
    private Long startNode;
    private Long endNode;
}
