package edu.wpi.punchy_pegasi.generator.schema;

import edu.wpi.punchy_pegasi.generator.SchemaID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Account {
    @SchemaID
    private String username;
    private String password;
    private Long employeeID;
    private AccountType accountType;

        @RequiredArgsConstructor
        public enum AccountType {
            NONE(0),
            ADMIN(2),
            STAFF(1);
            @Getter
            private final int shieldLevel;
        }

}
