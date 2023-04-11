package edu.wpi.punchy_pegasi.generator.schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class Account {
        private String username;
        private String password;
        private Long employeeID;
        private AccountType accountType;

        public enum AccountType {
            NONE,
            ADMIN,
            STAFF;
        }

        @RequiredArgsConstructor
        public enum Field {
            USERNAME("username"),
            PASSWORD("password");
            @Getter
            private final String colName;
            public Object getValue(edu.wpi.punchy_pegasi.generator.schema.Account ref){
                return ref.getFromField(this);
            }
        }
        public Object getFromField(edu.wpi.punchy_pegasi.generator.schema.Account.Field field) {
            return switch (field) {
                case USERNAME -> getUsername();
                case PASSWORD -> getPassword();
            };
        }


}
