package edu.wpi.punchy_pegasi.schema;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @com.jsoniter.annotation.JsonProperty("username")
    private String username;
    @com.jsoniter.annotation.JsonProperty("password")
    private String password;
    @com.jsoniter.annotation.JsonProperty("employeeid")
    private Long employeeID;
    @com.jsoniter.annotation.JsonProperty("accounttype")
    private AccountType accountType;

    @RequiredArgsConstructor
    public enum AccountType {
        NONE(0),
        ADMIN(2),
        STAFF(1);
        @Getter
        private final int shieldLevel;
    }

    @lombok.RequiredArgsConstructor
    public enum Field implements IField<edu.wpi.punchy_pegasi.schema.Account> {
        USERNAME("username"),
        PASSWORD("password"),
        EMPLOYEE_ID("employeeID"),
        ACCOUNT_TYPE("accountType");
        @lombok.Getter
        private final String colName;

        public Object getValue(edu.wpi.punchy_pegasi.schema.Account ref) {
            return ref.getFromField(this);
        }
    }

    public Object getFromField(Field field) {
        return switch (field) {
            case USERNAME -> getUsername();
            case PASSWORD -> getPassword();
            case EMPLOYEE_ID -> getEmployeeID();
            case ACCOUNT_TYPE -> getAccountType();
        };
    }

}