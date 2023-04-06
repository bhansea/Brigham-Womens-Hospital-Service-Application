package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OfficeServiceRequestEntryDaoImplTest {
    static PdbController pdbController;
    static OfficeServiceRequestEntryDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "officeRequest", "employeeName"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new OfficeServiceRequestEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.OFFICEREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void get() {
        OfficeServiceRequestEntry office = new OfficeServiceRequestEntry(UUID.randomUUID(),"testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        Object[] values = new Object[]{office.getServiceID(), office.getRoomNumber(), office.getStaffAssignment(), office.getAdditionalNotes(), office.getStatus(),office.getOfficeRequest(), office.getEmployeeName()};
        try{
            pdbController.insertQuery(TableType.OFFICEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<OfficeServiceRequestEntry> results = dao.get(office.getServiceID());
        OfficeServiceRequestEntry daoresult = results.get();
        assertEquals(daoresult,office);
        try {
            pdbController.deleteQuery(TableType.OFFICEREQUESTS,"serviceID", office.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        var office0 = new OfficeServiceRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        var office1 = new OfficeServiceRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        Object[] values0 = new Object[]{office0.getServiceID(), office0.getRoomNumber(), office0.getStaffAssignment(), office0.getAdditionalNotes(), office0.getStatus(),office0.getOfficeRequest(), office0.getEmployeeName()};
        Object[] values1 = new Object[]{office1.getServiceID(), office1.getRoomNumber(), office1.getStaffAssignment(), office1.getAdditionalNotes(), office1.getStatus(),office1.getOfficeRequest(), office1.getEmployeeName()};
        try{
            pdbController.insertQuery(TableType.OFFICEREQUESTS, fields, values0);
            pdbController.insertQuery(TableType.OFFICEREQUESTS, fields, values1);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(OfficeServiceRequestEntry.Field.ROOM_NUMBER, "testRoom");
        var map = new HashMap<UUID, OfficeServiceRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.OFFICEREQUESTS, OfficeServiceRequestEntry.Field.ROOM_NUMBER.getColName(), "testRoom")) {
            while (rs.next()) {
                var req = new OfficeServiceRequestEntry(
                        (UUID)rs.getObject("serviceID"),
                        (String)rs.getObject("roomNumber"),
                        (String)rs.getObject("staffAssignment"),
                        (String)rs.getObject("additionalNotes"),
                        (RequestEntry.Status)rs.getObject("status"),
                        (String)rs.getObject("officeRequest"),
                        (String)rs.getObject("employeeName"));
                if (req != null) {
                    map.put(req.getServiceID(), req);
                }
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            assert false: e.getMessage();
        }

        assertEquals(map.get(office0.getServiceID()), results.get(office0.getServiceID()));
        assertEquals(map.get(office1.getServiceID()), results.get(office1.getServiceID()));
        try{
            pdbController.deleteQuery(TableType.OFFICEREQUESTS,"serviceID", office0.getServiceID());
            pdbController.deleteQuery(TableType.OFFICEREQUESTS,"serviceID", office1.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    void getAll() {
        var value0 = new Object[]{UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName"};
        var value1 = new Object[]{UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName"};
        var value2 = new Object[]{UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName"};
        var valueSet = new Object[][]{value0, value1, value2};

        var refMap = new HashMap<UUID, OfficeServiceRequestEntry>();
        for (var value : valueSet) {
            var office = new OfficeServiceRequestEntry((UUID) value[0], (String) value[1], (String) value[2], (String) value[3], (RequestEntry.Status) value[4], (String) value[5], (String) value[6]);
            refMap.put(office.getServiceID(), office);
            try {
                pdbController.insertQuery(TableType.OFFICEREQUESTS, fields, value);
            } catch (PdbController.DatabaseException e) {
                assert false: e.getMessage();
            }
        }
        Map<UUID, OfficeServiceRequestEntry> resultMap = dao.getAll();
        for (var key : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.OFFICEREQUESTS, "serviceID", key);
            } catch (PdbController.DatabaseException e) {
                assert false: e.getMessage();
            }
        }

        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        UUID uuid = UUID.randomUUID();
        OfficeServiceRequestEntry office = new OfficeServiceRequestEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        dao.save(office);
        Optional<OfficeServiceRequestEntry> results = dao.get(office.getServiceID());
        OfficeServiceRequestEntry daoresult = results.get();
        assertEquals(office, daoresult);
        try {
            pdbController.deleteQuery(TableType.OFFICEREQUESTS, "serviceID", office.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        UUID uuid = UUID.randomUUID();
        OfficeServiceRequestEntry office = new OfficeServiceRequestEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        dao.save(office);

        OfficeServiceRequestEntry updatedOffice = new OfficeServiceRequestEntry(uuid, "updatedTestRoom", "testStaff", "testNotes", RequestEntry.Status.DONE,"testOffices", "updatedTestName");
        OfficeServiceRequestEntry.Field[] fields = {OfficeServiceRequestEntry.Field.ROOM_NUMBER, OfficeServiceRequestEntry.Field.STATUS, OfficeServiceRequestEntry.Field.EMPLOYEE_NAME};
        dao.update(updatedOffice, fields);

        Optional<OfficeServiceRequestEntry> results = dao.get(office.getServiceID());
        OfficeServiceRequestEntry daoresult = results.get();
        assertEquals(updatedOffice, daoresult);
        try {
            pdbController.deleteQuery(TableType.OFFICEREQUESTS, "serviceID", office.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        OfficeServiceRequestEntry office = new OfficeServiceRequestEntry(UUID.randomUUID(),"110", "testStaff", "testNotes", RequestEntry.Status.PROCESSING,"testOffices", "testName");
        Object[] values = new Object[]{office.getServiceID(), office.getRoomNumber(), office.getStaffAssignment(), office.getAdditionalNotes(), office.getStatus(),office.getOfficeRequest(), office.getEmployeeName()};
        try{
            pdbController.insertQuery(TableType.OFFICEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert into database";
        }

        try{
            pdbController.searchQuery(TableType.OFFICEREQUESTS, "serviceID", office.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to search database";
        }

        dao.delete(office);

        try{
            pdbController.searchQuery(TableType.OFFICEREQUESTS, "serviceID", office.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert true: "Successfully deleted from database";
        }
    }
}