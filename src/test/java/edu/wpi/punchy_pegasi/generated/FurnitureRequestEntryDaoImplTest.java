package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FurnitureRequestEntryDaoImplTest {
    static PdbController pdbController;
    static FurnitureRequestEntryDaoImpl dao;

    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "selectFurniture"};
        pdbController = new PdbController(Config.source);
        dao = new FurnitureRequestEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.FURNITUREREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        List<String> requestItems = new ArrayList<>();
        requestItems.add("testItems");
        FurnitureRequestEntry furniture = new FurnitureRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, requestItems);
        Object[] values = new Object[]{furniture.getServiceID(), furniture.getLocationName(), furniture.getStaffAssignment(), furniture.getAdditionalNotes(), furniture.getStatus(), furniture.getSelectFurniture()};
        try {
            pdbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<FurnitureRequestEntry> results = dao.get(furniture.getServiceID());
        FurnitureRequestEntry daoresult = results.get();
        assertEquals(daoresult, furniture);
        try {
            pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", furniture.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        List<String> requestItems = new ArrayList<>();
        requestItems.add("testItems");
        FurnitureRequestEntry furniture = new FurnitureRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, requestItems);
        FurnitureRequestEntry furniture2 = new FurnitureRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, requestItems);
        Object[] values = new Object[]{furniture.getServiceID(), furniture.getLocationName(), furniture.getStaffAssignment(), furniture.getAdditionalNotes(), furniture.getStatus(), furniture.getSelectFurniture()};
        Object[] values2 = new Object[]{furniture2.getServiceID(), furniture2.getLocationName(), furniture2.getStaffAssignment(), furniture2.getAdditionalNotes(), furniture2.getStatus(), furniture2.getSelectFurniture()};
        try {
            pdbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
            pdbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(FurnitureRequestEntry.Field.ADDITIONAL_NOTES, "testNotes");
        var map = new HashMap<java.util.UUID, FurnitureRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.FURNITUREREQUESTS, "additionalNotes", "testNotes")) {
            while (rs.next()) {
                FurnitureRequestEntry req = new FurnitureRequestEntry(
                        (java.util.UUID) rs.getObject("serviceID"),
                        (java.lang.String) rs.getObject("roomNumber"),
                        (java.lang.String) rs.getObject("staffAssignment"),
                        (java.lang.String) rs.getObject("additionalNotes"),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String) rs.getObject("status")),
                        Arrays.asList((String[]) rs.getArray("selectFurniture").getArray()));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(furniture.getServiceID()), results.get(furniture.getServiceID()));
        assertEquals(map.get(furniture2.getServiceID()), results.get(furniture2.getServiceID()));
        try {
            pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", furniture.getServiceID());
            pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", furniture2.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[]{
                UUID.randomUUID(),
                "0",
                "staff0",
                "additionalNotes0",
                RequestEntry.Status.PROCESSING,
                List.of("item1", "item2")
        };
        var values1 = new Object[]{
                UUID.randomUUID(),
                "1",
                "staff1",
                "additionalNotes1",
                RequestEntry.Status.PROCESSING,
                List.of("item1", "item2")
        };
        var values2 = new Object[]{
                UUID.randomUUID(),
                "2",
                "staff2",
                "additionalNotes2",
                RequestEntry.Status.PROCESSING,
                List.of("item1", "item2")
        };

        var valuesSet = new Object[][]{
                values0,
                values1,
                values2
        };
        var refMap = new HashMap<UUID, FurnitureRequestEntry>();

        for (Object[] objects : valuesSet) {
            try {
                pdbController.insertQuery(TableType.FURNITUREREQUESTS, fields, objects);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to insert into database";
            }
            var furnRequests = new FurnitureRequestEntry(
                    (UUID) objects[0],
                    (String) objects[1],
                    (String) objects[2],
                    (String) objects[3],
                    (RequestEntry.Status) objects[4],
                    (List<String>) objects[5]
            );
            refMap.put(furnRequests.getServiceID(), furnRequests);
        }

        Map<UUID, FurnitureRequestEntry> resultMap = dao.getAll();

        for (var uuid : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", uuid);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var dao = new FurnitureRequestEntryDaoImpl(pdbController);
        UUID uuid = UUID.randomUUID();
        List<String> requestItems = new ArrayList<>();
        requestItems.add("testItems");
        FurnitureRequestEntry fdre = new FurnitureRequestEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, requestItems);
        dao.save(fdre);
        Optional<FurnitureRequestEntry> results = dao.get(uuid);
        FurnitureRequestEntry daoresult = results.get();
        assertEquals(fdre, daoresult);
        try {
            pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        var dao = new FurnitureRequestEntryDaoImpl(pdbController);
        UUID uuid = UUID.randomUUID();
        List<String> requestItems = new ArrayList<>();
        requestItems.add("testItems");
        FurnitureRequestEntry fdre = new FurnitureRequestEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, requestItems);
        dao.save(fdre);

        FurnitureRequestEntry updatedFdre = new FurnitureRequestEntry(uuid, "testRoom", "testStaff", "updatedTestNotes", RequestEntry.Status.NONE, requestItems);
        FurnitureRequestEntry.Field[] fields = {FurnitureRequestEntry.Field.ADDITIONAL_NOTES, FurnitureRequestEntry.Field.STATUS};
        dao.update(updatedFdre, fields);

        Optional<FurnitureRequestEntry> results = dao.get(uuid);
        FurnitureRequestEntry daoresult = results.get();
        assertEquals(updatedFdre, daoresult);
        try {
            pdbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        FurnitureRequestEntry furnitureRequest = new FurnitureRequestEntry(
                UUID.randomUUID(),
                "roomNum",
                "staff",
                "additionalNotes",
                RequestEntry.Status.PROCESSING,
                List.of("item1", "item2")
        );

        var values = new Object[]{
                furnitureRequest.getServiceID(),
                furnitureRequest.getLocationName(),
                furnitureRequest.getStaffAssignment(),
                furnitureRequest.getAdditionalNotes(),
                furnitureRequest.getStatus(),
                furnitureRequest.getSelectFurniture()
        };

        try {
            pdbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert into database";
        }

        try {
            pdbController.searchQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to search database";
        }

        dao.delete(furnitureRequest);

        try {
            pdbController.searchQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted from database";
        }
    }
}