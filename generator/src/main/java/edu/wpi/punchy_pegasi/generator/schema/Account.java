package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Account {
    private String username;
    private String password;
    @SchemaID
    private Long employeeID;
    private AccountType accountType;

        public enum AccountType {
            NONE,
            ADMIN,
            STAFF;
        }

}
