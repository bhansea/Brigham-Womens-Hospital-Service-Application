package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Employee;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class EmployeeDaoImplTest {

    static PdbController pdbController;
    static EmployeeDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"employeeID", "firstName", "lastName"};
        pdbController = new PdbController(Config.source, "test");
        dao = new EmployeeDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.EMPLOYEES);
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
        Employee employee = new Employee(100L, "testName", "testName");
        Object[] values = new Object[]{employee.getEmployeeID(), employee.getFirstName(), employee.getLastName()};
        try {
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Employee> results = dao.get(employee.getEmployeeID());
        Employee daoresult = results.get();
        assertEquals(daoresult, employee);
        try {
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        Employee employee = new Employee(100L, "testFirstName", "testLastName");
        Employee employee2 = new Employee(101L, "testFirstName", "testLastName");
        Object[] values = new Object[]{employee.getEmployeeID(), employee.getFirstName(), employee.getLastName()};
        Object[] values2 = new Object[]{employee2.getEmployeeID(), employee2.getFirstName(), employee2.getLastName()};
        try {
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values);
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(Employee.Field.FIRST_NAME, "testName");
        var map = new HashMap<Long, Employee>();
        try (var rs = pdbController.searchQuery(TableType.EMPLOYEES, "shortName", "testName")) {
            while (rs.next()) {
                Employee req = new Employee(
                        (java.lang.Long) rs.getObject("employeeID"),
                        (java.lang.String) rs.getObject("firstName"),
                        (java.lang.String) rs.getObject("lastName"));
                if (req != null)
                    map.put(req.getEmployeeID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(employee.getEmployeeID()), results.get(employee.getEmployeeID()));
        assertEquals(map.get(employee2.getEmployeeID()), results.get(employee2.getEmployeeID()));
        try {
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee2.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet1() {
        Employee employee = new Employee(100L, "testFirstName1", "testLastName1");
        Employee employee2 = new Employee(101L, "testFirstName2", "testLastName2");
        Object[] values = new Object[]{employee.getEmployeeID(), employee.getFirstName(), employee.getLastName()};
        Object[] values2 = new Object[]{employee2.getEmployeeID(), employee2.getFirstName(), employee2.getLastName()};
        try {
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values);
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Employee.Field[] fields = {Employee.Field.FIRST_NAME, Employee.Field.LAST_NAME};
        Object[] searchValues = new Object[]{"testFirstName1", "testFirstName2"};
        String[] searchFields = new String[]{"staffAssignment", "employeeName"};
        var results = dao.get(fields, searchValues);
        var map = new HashMap<Long, Employee>();
        try (var rs = pdbController.searchQuery(TableType.EMPLOYEES, searchFields, searchValues)) {
            while (rs.next()) {
                Employee req = new Employee(
                        (java.lang.Long) rs.getObject("employeeID"),
                        (java.lang.String) rs.getObject("firstName"),
                        (java.lang.String) rs.getObject("lastName"));
                if (req != null)
                    map.put(req.getEmployeeID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(employee.getEmployeeID()), results.get(employee.getEmployeeID()));
        assertEquals(map.get(employee2.getEmployeeID()), results.get(employee2.getEmployeeID()));
        try {
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee2.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[]{100L, "LongtestName0", "testName0"};
        var values1 = new Object[]{101L, "LongtestName1", "testName1"};
        var values2 = new Object[]{102L, "LongtestName2", "testName2"};

        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, Employee>();
        for (Object[] values : valueSet) {
            var employee = new Employee((Long) values[0], (String) values[1], (String) values[2]);
            refMap.put(employee.getEmployeeID(), employee);
            try {
                pdbController.insertQuery(TableType.EMPLOYEES, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Long, Employee> resultMap = dao.getAll();
        for (var employeeID : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employeeID);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var dao = new EmployeeDaoImpl(pdbController);
        Employee employee = new Employee(100L, "testFirstName", "testLastName");
        dao.save(employee);
        Optional<Employee> results = dao.get(employee.getEmployeeID());
        Employee daoresult = results.get();
        assertEquals(employee, daoresult);
        try {
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        var dao = new EmployeeDaoImpl(pdbController);
        Employee employee = new Employee(100L, "testFirstName", "testLastName");
        dao.save(employee);

        Employee updatedEmployee = new Employee(100L, "testFirstName", "updatedTestLastName");
        Employee.Field[] fields = {Employee.Field.LAST_NAME};
        dao.update(updatedEmployee, fields);

        Optional<Employee> results = dao.get(employee.getEmployeeID());
        Employee daoresult = results.get();
        assertEquals(updatedEmployee, daoresult);
        try {
            pdbController.deleteQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        Employee employee = new Employee(100L, "testLName", "testSName");
        Object[] values = new Object[]{employee.getEmployeeID(), employee.getFirstName(), employee.getLastName()};
        try {
            pdbController.insertQuery(TableType.EMPLOYEES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert into database";
        }
        try {
            pdbController.searchQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to search database";
        }

        dao.delete(employee);

        try {
            pdbController.searchQuery(TableType.EMPLOYEES, "employeeID", employee.getEmployeeID());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted from database";
        }
    }
}