package edu.wpi.punchy_pegasi.schema;

import com.jsoniter.annotation.JsonProperty;
import edu.wpi.punchy_pegasi.backend.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.jsoniter.annotation.JsonCreator;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @SchemaID
    @com.jsoniter.annotation.JsonProperty("employeeid")
    private Long employeeID;
    @com.jsoniter.annotation.JsonProperty("firstname")
    private String firstName;
    @com.jsoniter.annotation.JsonProperty("lastname")
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }
@lombok.RequiredArgsConstructor
public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Employee>{
        EMPLOYEE_ID("employeeID"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName");
        @lombok.Getter
        private final String colName;
        public Object getValue(edu.wpi.punchy_pegasi.schema.Employee ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Field field) {
        return switch (field) {
            case EMPLOYEE_ID -> getEmployeeID();
            case FIRST_NAME -> getFirstName();
            case LAST_NAME -> getLastName();
        };
    }

}