package edu.wpi.punchy_pegasi.generator.schema;

import com.jsoniter.annotation.JsonProperty;
import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.jsoniter.annotation.JsonCreator;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @SchemaID
    private Long employeeID;
    private String firstName;
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
