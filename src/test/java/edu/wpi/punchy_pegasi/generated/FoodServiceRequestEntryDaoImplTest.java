package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodServiceRequestEntryDaoImplTest {
    static PdbController pdbController;
    static FoodServiceRequestEntryDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "foodSelection", "tempType", "additionalItems", "beverage", "dietaryRestrictions", "patientName"};
        pdbController = new PdbController(Config.source);
        dao = new FoodServiceRequestEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.FOODREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        FoodServiceRequestEntry food = new FoodServiceRequestEntry(UUID.randomUUID(), locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        Object[] values = new Object[]{food.getServiceID(), food.getLocationName(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getBeverage(),food.getDietaryRestrictions(), food.getPatientName()};
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<FoodServiceRequestEntry> results = dao.get(food.getServiceID());
        FoodServiceRequestEntry daoresult = results.get();
        assertEquals(daoresult, food);
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var food = new FoodServiceRequestEntry(UUID.randomUUID(), locName0, staff0, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        var food2 = new FoodServiceRequestEntry(UUID.randomUUID(), locName1, staff1, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        var values = new Object[]{food.getServiceID(), food.getLocationName(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getBeverage(), food.getDietaryRestrictions(), food.getPatientName()};
        var values2 = new Object[]{food2.getServiceID(), food2.getLocationName(), food2.getStaffAssignment(), food2.getAdditionalNotes(), food2.getStatus(), food2.getFoodSelection(), food2.getTempType(), food2.getAdditionalItems(), food2.getBeverage(), food2.getDietaryRestrictions(), food2.getPatientName()};
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, fields, values);
            pdbController.insertQuery(TableType.FOODREQUESTS, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(FoodServiceRequestEntry.Field.STAFF_ASSIGNMENT, staff0);
        var map = new HashMap<java.util.UUID, FoodServiceRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.FOODREQUESTS, "staffAssignment", staff0)) {
            while (rs.next()) {
                String myCol = rs.getString("additionalItems");
                List<String> myColumnList = Arrays.asList(myCol.split(","));
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                        (java.util.UUID) rs.getObject("serviceID"),
                        (java.lang.Long) rs.getObject("locationName"),
                        (java.lang.Long) rs.getObject("staffAssignment"),
                        rs.getObject("additionalNotes", String.class),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String) rs.getObject("status")),
                        (java.lang.String) rs.getObject("foodSelection"),
                        (java.lang.String) rs.getObject("tempType"),
                        myColumnList,
                        (java.lang.String) rs.getObject("beverage"),
                        (java.lang.String) rs.getObject("dietaryRestrictions"),
                        (java.lang.String) rs.getObject("patientName"));
                map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            assert false : e.getMessage();
        }
        assertEquals(map.get(food.getServiceID()), results.get(food.getServiceID()));
        assertEquals(map.get(food2.getServiceID()), results.get(food2.getServiceID()));
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food.getServiceID());
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food2.getServiceID());
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
        var additionalItem = "testItems";
        var values0 = new Object[]{UUID.randomUUID(), locName0, staff0, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var values1 = new Object[]{UUID.randomUUID(), locName1, staff1, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var values2 = new Object[]{UUID.randomUUID(), locName2, staff2, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var valuesSet = new Object[][]{values0, values1, values2};
        var refMap = new HashMap<java.util.UUID, FoodServiceRequestEntry>();

        for (var values : valuesSet) {
            try {
                pdbController.insertQuery(TableType.FOODREQUESTS, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
            FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID) values[0],
                    (java.lang.Long) values[1],
                    (java.lang.Long) values[2],
                    (java.lang.String) values[3],
                    (RequestEntry.Status) values[4],
                    (java.lang.String) values[5],
                    (java.lang.String) values[6],
                    (List<String>) values[7],
                    (java.lang.String) values[8],
                    (java.lang.String) values[9],
                    (java.lang.String) values[10]);
            refMap.put(req.getServiceID(), req);
        }

        Map<UUID, FoodServiceRequestEntry> resultMap = dao.getAll();

        for (var key : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", key);
            } catch (PdbController.DatabaseException e) {
                assert false : e.getMessage();
            }
        }

        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var dao = new FoodServiceRequestEntryDaoImpl(pdbController);
        UUID uuid = UUID.randomUUID();
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        FoodServiceRequestEntry fsre = new FoodServiceRequestEntry(uuid, locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        dao.save(fsre);

        Optional<FoodServiceRequestEntry> results = dao.get(uuid);
        FoodServiceRequestEntry daoresult = results.get();
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        assertEquals(fsre, daoresult);
    }

    @Test
    void update() {
        UUID uuid = UUID.randomUUID();
        FoodServiceRequestEntry foodRequest = new FoodServiceRequestEntry(
                uuid,
                100L,
                100L,
                "testNode0",
                RequestEntry.Status.PROCESSING,
                "testFood0",
                "100",
                List.of("item1", "item2"),
                "Juice",
                "restrictions0",
                "patientName0"
        );
        FoodServiceRequestEntry updateFoodRequest = new FoodServiceRequestEntry(
                uuid,
                100L,
                100L,
                "testNode0",
                RequestEntry.Status.PROCESSING,
                "testFood0",
                "100",
                List.of("item1", "item2"),
                "Apple Juice",
                "restrictions0",
                "patientName0"
        );
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, fields, new Object[]{
                    foodRequest.getServiceID(),
                    foodRequest.getLocationName(),
                    foodRequest.getStaffAssignment(),
                    foodRequest.getAdditionalNotes(),
                    foodRequest.getStatus(),
                    foodRequest.getFoodSelection(),
                    foodRequest.getTempType(),
                    foodRequest.getAdditionalItems(),
                    foodRequest.getBeverage(),
                    foodRequest.getDietaryRestrictions(),
                    foodRequest.getPatientName()
            });
        } catch (PdbController.DatabaseException e) {
            assert false : e.getMessage();
        }
        FoodServiceRequestEntry.Field[] updateFields = {
                FoodServiceRequestEntry.Field.BEVERAGE
        };
        dao.update(updateFoodRequest, updateFields);
        Optional<FoodServiceRequestEntry> fsrq = dao.get(uuid);
        FoodServiceRequestEntry daoresult = fsrq.get();
        assertEquals(daoresult, updateFoodRequest);
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        FoodServiceRequestEntry foodRequest = new FoodServiceRequestEntry(
                UUID.randomUUID(),
                ThreadLocalRandom.current().nextLong(),
                ThreadLocalRandom.current().nextLong(),
                "testNode",
                RequestEntry.Status.PROCESSING,
                "testFood",
                "100",
                List.of("item1", "item2"),
                "juice",
                "restrictions",
                "patientName"
        );


        var values = new Object[]{
                foodRequest.getServiceID(),
                foodRequest.getLocationName(),
                foodRequest.getStaffAssignment(),
                foodRequest.getAdditionalNotes(),
                foodRequest.getStatus(),
                foodRequest.getFoodSelection(),
                foodRequest.getTempType(),
                foodRequest.getAdditionalItems(),
                foodRequest.getBeverage(),
                foodRequest.getDietaryRestrictions(),
                foodRequest.getPatientName()
        };
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert test data";
        }

        try {
            pdbController.searchQuery(TableType.FOODREQUESTS, "serviceID", foodRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to find test data";
        }

        dao.delete(foodRequest);

        try {
            pdbController.searchQuery(TableType.FOODREQUESTS, "serviceID", foodRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted test data";
        }
    }
}

