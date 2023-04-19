package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.*;
import edu.wpi.punchy_pegasi.schema.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AccountDaoImplTest {

    static PdbController pdbController;
    static AccountDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"username", "password", "employeeID", "accountType"};
        pdbController = new PdbController(Config.source, "test");
        dao = new AccountDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.ACCOUNTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        var statement = pdbController.exposeConnection().createStatement();
        statement.execute("drop schema test cascade;");
    }

    @Test
    void get() {
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        Object[] values = new Object[]{account.getUsername(), "testPassword", account.getEmployeeID(), account.getAccountType()};
        try {
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Account> results = dao.get(account.getUsername());
        Account daoresult = results.get();
        assertEquals(daoresult, account);
        try {
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account.getUsername());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        Account account2 = new Account("testUsername1", "testPassword", 100L, Account.AccountType.ADMIN);
        Object[] values = new Object[]{account.getUsername(), account.getPassword(), account.getEmployeeID(), account.getAccountType()};
        Object[] values2 = new Object[]{account2.getUsername(), account2.getPassword(), account2.getEmployeeID(), account2.getAccountType()};
        try {
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values);
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(Account.Field.EMPLOYEE_ID, 100L);
        var map = new HashMap<String, Account>();
        try (var rs = pdbController.searchQuery(TableType.ACCOUNTS, "employeeID", 100L)) {
            while (rs.next()) {
                Account req = new Account(
                        (java.lang.String) rs.getObject("username"),
                        (java.lang.String) rs.getObject("password"),
                        (java.lang.Long) rs.getObject("employeeID"),
                        edu.wpi.punchy_pegasi.schema.Account.AccountType.valueOf((String) rs.getObject("accountType")));
                if (req != null)
                    map.put(req.getUsername(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(account.getUsername()), results.get(account.getUsername()));
        assertEquals(map.get(account2.getUsername()), results.get(account2.getUsername()));
        try {
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account.getUsername());
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account2.getUsername());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet1() {
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        Account account2 = new Account("testUsername1", "testPassword", 200L, Account.AccountType.STAFF);
        Object[] values = new Object[]{account.getUsername(), account.getPassword(), account.getEmployeeID(), account.getAccountType()};
        Object[] values2 = new Object[]{account2.getUsername(), account2.getPassword(), account2.getEmployeeID(), account2.getAccountType()};
        try {
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values);
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Account.Field[] fields = {Account.Field.EMPLOYEE_ID, Account.Field.ACCOUNT_TYPE};
        Object[] searchValues = new Object[]{"100L", "STAFF"};
        String[] searchFields = new String[]{"employeeID", "accountType"};
        var results = dao.get(fields, searchValues);
        var map = new HashMap<String, Account>();
        try (var rs = pdbController.searchQuery(TableType.ACCOUNTS, searchFields, searchValues)) {
            while (rs.next()) {
                Account req = new Account(
                        (java.lang.String) rs.getObject("username"),
                        (java.lang.String) rs.getObject("password"),
                        (java.lang.Long) rs.getObject("employeeID"),
                        edu.wpi.punchy_pegasi.schema.Account.AccountType.valueOf((String) rs.getObject("accountType")));
                if (req != null)
                    map.put(req.getUsername(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(account.getUsername()), results.get(account.getUsername()));
        assertEquals(map.get(account2.getUsername()), results.get(account2.getUsername()));
        try {
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account.getUsername());
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account2.getUsername());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[]{"username0", "password0", 100L, Account.AccountType.ADMIN};
        var values1 = new Object[]{"username1", "password1", 101L, Account.AccountType.ADMIN};
        var values2 = new Object[]{"username2", "password2", 102L, Account.AccountType.ADMIN};

        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<String, Account>();
        for (Object[] values : valueSet) {
            var account = new Account((String) values[0], (String) values[1], (Long) values[2], (Account.AccountType) values[3]);
            refMap.put(account.getUsername(), account);
            try {
                pdbController.insertQuery(TableType.ACCOUNTS, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<String, Account> resultMap = dao.getAll();
        for (var username : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.ACCOUNTS, "username", username);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var dao = new AccountDaoImpl(pdbController);
        Long uuid = 100L;
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        dao.save(account);
        Optional<Account> results = dao.get(account.getUsername());
        Account daoresult = results.get();
        assertEquals(account, daoresult);
        try {
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account.getUsername());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        var dao = new AccountDaoImpl(pdbController);
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        dao.save(account);

        Account updatedAccount = new Account("testUsername", "testPassword", 100L, Account.AccountType.STAFF);
        Account.Field[] fields = {Account.Field.ACCOUNT_TYPE};
        dao.update(updatedAccount, fields);

        Optional<Account> results = dao.get(account.getUsername());
        Account daoresult = results.get();
        assertEquals(updatedAccount, daoresult);
        try {
            pdbController.deleteQuery(TableType.ACCOUNTS, "username", account.getUsername());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        Account account = new Account("testUsername", "testPassword", 100L, Account.AccountType.ADMIN);
        Object[] values = new Object[]{account.getUsername(), account.getPassword(), account.getEmployeeID(), account.getAccountType()};
        try {
            pdbController.insertQuery(TableType.ACCOUNTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert into database";
        }
        try {
            pdbController.searchQuery(TableType.ACCOUNTS, "username", account.getUsername());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to search database";
        }

        dao.delete(account);

        try {
            pdbController.searchQuery(TableType.ACCOUNTS, "username", account.getUsername());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted from database";
        }
    }
}