package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class FlowerDeliveryRequestEntryDaoImplTest {
    static PdbController pdbController;

    static FlowerDeliveryRequestEntryDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"serviceID", "patientName", "roomNumber", "staffAssignment", "additionalNotes", "status", "flowerSize", "flowerAmount", "flowerType"};
        pdbController = new PdbController(Config.source);
        dao = new FlowerDeliveryRequestEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.FLOWERREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        FlowerDeliveryRequestEntry flowers = new FlowerDeliveryRequestEntry(UUID.randomUUID(), "testPatient", locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        Object[] values = new Object[]{flowers.getServiceID(), flowers.getPatientName(), flowers.getLocationName(), flowers.getStaffAssignment(), flowers.getAdditionalNotes(), flowers.getStatus(), flowers.getFlowerSize(), flowers.getFlowerAmount(), flowers.getFlowerType()};
        try {
            pdbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<FlowerDeliveryRequestEntry> results = dao.get(flowers.getServiceID());
        FlowerDeliveryRequestEntry daoresult = results.get();
        assertEquals(daoresult, flowers);
        try {
            pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", flowers.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var flowers = new FlowerDeliveryRequestEntry(UUID.randomUUID(), "testPatient", locName0, staff0, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        var flowers2 = new FlowerDeliveryRequestEntry(UUID.randomUUID(), "testPatient", locName1, staff1, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        var values = new Object[]{flowers.getServiceID(), flowers.getPatientName(), flowers.getLocationName(), flowers.getStaffAssignment(), flowers.getAdditionalNotes(), flowers.getStatus(), flowers.getFlowerSize(), flowers.getFlowerAmount(), flowers.getFlowerType()};
        var values2 = new Object[]{flowers2.getServiceID(), flowers2.getPatientName(), flowers2.getLocationName(), flowers2.getStaffAssignment(), flowers2.getAdditionalNotes(), flowers2.getStatus(), flowers2.getFlowerSize(), flowers2.getFlowerAmount(), flowers2.getFlowerType()};
        try {
            pdbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
            pdbController.insertQuery(TableType.FLOWERREQUESTS, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(FlowerDeliveryRequestEntry.Field.PATIENT_NAME, "testPatient");
        var map = new HashMap<java.util.UUID, FlowerDeliveryRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.FLOWERREQUESTS, "patientName", "testPatient")) {
            while (rs.next()) {
                FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                        (java.util.UUID) rs.getObject("serviceID"),
                        (java.lang.String) rs.getObject("patientName"),
                        (java.lang.Long) rs.getObject("roomNumber"),
                        (java.lang.Long) rs.getObject("staffAssignment"),
                        (java.lang.String) rs.getObject("additionalNotes"),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String) rs.getObject("status")),
                        (java.lang.String) rs.getObject("flowerSize"),
                        (java.lang.String) rs.getObject("flowerAmount"),
                        (java.lang.String) rs.getObject("flowerType"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(flowers.getServiceID()), results.get(flowers.getServiceID()));
        assertEquals(map.get(flowers2.getServiceID()), results.get(flowers2.getServiceID()));
        try {
            pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", flowers.getServiceID());
            pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", flowers2.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var locName2 = ThreadLocalRandom.current().nextLong();
        var staff2 = ThreadLocalRandom.current().nextLong();
        var values0 = new Object[]{
                UUID.randomUUID(),
                "testPatient0",
                locName0,
                staff0,
                "testNotes0",
                RequestEntry.Status.PROCESSING,
                "testSmall0",
                "test0",
                "testTulip0"
        };
        var values1 = new Object[]{
                UUID.randomUUID(),
                "testPatient1",
                locName1,
                staff1,
                "testNotes1",
                RequestEntry.Status.PROCESSING,
                "testSmall1",
                "test1",
                "testTulip1"
        };
        var values2 = new Object[]{
                UUID.randomUUID(),
                "testPatient2",
                locName2,
                staff2,
                "testNotes2",
                RequestEntry.Status.PROCESSING,
                "testSmall2",
                "test2",
                "testTulip2"
        };

        var valueSet = new Object[][]{values0, values1, values2};
        var refMap = new HashMap<UUID, FlowerDeliveryRequestEntry>();

        for (var values : valueSet) {
            try {
                pdbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
            var uuid = (UUID) values[0];
            var patientName = (String) values[1];
            var roomNumber = (Long) values[2];
            var staffAssignment = (Long) values[3];
            var additionalNotes = (String) values[4];
            var status = (RequestEntry.Status) values[5];
            var flowerSize = (String) values[6];
            var flowerAmount = (String) values[7];
            var flowerType = (String) values[8];
            var entry = new FlowerDeliveryRequestEntry(uuid, patientName, roomNumber, staffAssignment, additionalNotes, status, flowerSize, flowerAmount, flowerType);
            refMap.put(uuid, entry);
        }
        Map<UUID, FlowerDeliveryRequestEntry> resultMap = dao.getAll();
        for (var uuid : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", uuid);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        UUID uuid = UUID.randomUUID();
        FlowerDeliveryRequestEntry fdre = new FlowerDeliveryRequestEntry(uuid, "testPatient", locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        dao.save(fdre);
        Optional<FlowerDeliveryRequestEntry> results = dao.get(uuid);
        FlowerDeliveryRequestEntry daoresult = results.get();
        assertEquals(fdre, daoresult);
        try {
            pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        var updatedLocName = ThreadLocalRandom.current().nextLong();
        var updatedStaff = ThreadLocalRandom.current().nextLong();
        UUID uuid = UUID.randomUUID();
        FlowerDeliveryRequestEntry fdre = new FlowerDeliveryRequestEntry(uuid, "testPatient", locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        dao.save(fdre);

        FlowerDeliveryRequestEntry updatedFdre = new FlowerDeliveryRequestEntry(uuid, "testPatient", updatedLocName, updatedStaff, "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test2", "testTulip");
        FlowerDeliveryRequestEntry.Field[] fields = {FlowerDeliveryRequestEntry.Field.FLOWER_AMOUNT};
        dao.update(updatedFdre, fields);

        Optional<FlowerDeliveryRequestEntry> results = dao.get(uuid);
        FlowerDeliveryRequestEntry daoresult = results.get();
        assertEquals(updatedFdre, daoresult);
        try {
            pdbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        FlowerDeliveryRequestEntry flowerEntry =
                new FlowerDeliveryRequestEntry(
                        UUID.randomUUID(),
                        "testPatient",
                        locName,
                        staff,
                        "testNotes",
                        RequestEntry.Status.PROCESSING,
                        "testSmall",
                        "test1",
                        "testTulip"
                );
        var values = new Object[]{
                flowerEntry.getServiceID(),
                flowerEntry.getPatientName(),
                flowerEntry.getLocationName(),
                flowerEntry.getStaffAssignment(),
                flowerEntry.getAdditionalNotes(),
                flowerEntry.getStatus(),
                flowerEntry.getFlowerSize(),
                flowerEntry.getFlowerAmount(),
                flowerEntry.getFlowerType()
        };
        try {
            pdbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Unable to insert flower request";
            log.error("Unable to insert flower request: ", e);
        }

        try {
            ResultSet result = pdbController.searchQuery(TableType.FLOWERREQUESTS, "serviceID", flowerEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Unable to find flower request";
            throw new RuntimeException(e);
        }

        dao.delete(flowerEntry);

        try {
            ResultSet result = pdbController.searchQuery(TableType.FLOWERREQUESTS, "serviceID", flowerEntry.getServiceID());
        } catch (PdbController.DatabaseException thrown) {
            assert true : "Flower request was deleted";
            assertTrue(thrown.getMessage().contentEquals("SQL error"));
        }


    }
}