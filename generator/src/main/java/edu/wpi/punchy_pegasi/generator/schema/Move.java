package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;

import java.time.LocalDate;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class Move {
    @SchemaID
    private Long uuid;
    private Long nodeID;
    private Long locationID;
    private LocalDate date;
}
