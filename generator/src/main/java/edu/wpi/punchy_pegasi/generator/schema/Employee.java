package edu.wpi.punchy_pegasi.generator.schema;

import com.jsoniter.annotation.JsonProperty;
import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.*;
import com.jsoniter.annotation.JsonCreator;

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
@NoArgsConstructor
@lombok.Builder(toBuilder=true)
public class Employee {
    @SchemaID
    private Long employeeID;
    private String firstName;
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
