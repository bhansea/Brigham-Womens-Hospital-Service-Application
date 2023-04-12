package edu.wpi.punchy_pegasi.schema;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Employee {

    private Long employeeID;
    private String firstName;
    private String lastName;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case EMPLOYEE_ID -> getEmployeeID();
            case FIRST_NAME -> getFirstName();
            case LAST_NAME -> getLastName();
        };
    }

    @lombok.RequiredArgsConstructor
    public enum Field {
        EMPLOYEE_ID("employeeID"),
        FIRST_NAME("firstName"),
        LAST_NAME("lastName");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Employee ref) {
            return ref.getFromField(this);
        }
    }

}