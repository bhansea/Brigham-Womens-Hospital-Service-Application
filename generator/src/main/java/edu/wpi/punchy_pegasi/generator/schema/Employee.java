package edu.wpi.punchy_pegasi.generator.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Employee {
    private Long employeeID;
    private String firstName;
    private String lastName;

    @RequiredArgsConstructor
    public enum Field {
        EMPLOYEE_ID("employeeID"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName");
        @Getter
        private final String colName;
        public Object getValue(Employee ref){
            return ref.getFromField(this);
        }
    }
    public Object getFromField(Employee.Field field) {
        return switch (field) {
            case EMPLOYEE_ID -> getEmployeeID();
            case FIRST_NAME -> getFirstName();
            case LAST_NAME -> getLastName();
        };
    }
}
