package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OfficeServiceRequestEntryDaoImplTest {
    static PdbController pdbController;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "officeRequest", "employeeName"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        try {
            pdbController.initTableByType(TableType.OFFICEREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void get() {
        var dao = new OfficeServiceRequestEntryDaoImpl(pdbController);
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
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        var dao = new OfficeServiceRequestEntryDaoImpl(pdbController);
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