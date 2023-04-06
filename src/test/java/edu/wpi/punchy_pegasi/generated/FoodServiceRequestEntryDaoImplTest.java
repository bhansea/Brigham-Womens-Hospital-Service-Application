package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FoodServiceRequestEntryDaoImplTest {
    static PdbController pdbController;
    static FoodServiceRequestEntryDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() {
        fields = new String[]{"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "foodSelection", "tempType", "additionalItems", "dietaryRestrictions", "patientName"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new FoodServiceRequestEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.FOODREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        FoodServiceRequestEntry food = new FoodServiceRequestEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "testRestrictions", "testPatient");
        Object[] values = new Object[]{food.getServiceID(), food.getRoomNumber(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getDietaryRestrictions(), food.getPatientName()};
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
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
        var dao = new FoodServiceRequestEntryDaoImpl(pdbController);
        UUID uuid = UUID.randomUUID();
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        FoodServiceRequestEntry fsre = new FoodServiceRequestEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "testRestrictions", "testPatient");
        dao.save(fsre);
        Optional<FoodServiceRequestEntry> results = dao.get(uuid);
        FoodServiceRequestEntry daoresult = results.get();
        assertEquals(fsre, daoresult);
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        FoodServiceRequestEntry foodRequest = new FoodServiceRequestEntry(
                UUID.randomUUID(),
                "123",
                "testStaff",
                "testNode",
                RequestEntry.Status.PROCESSING,
                "testFood",
                "100",
                List.of("item1", "item2"),
                "restrictions",
                "patientName"
        );


        var values = new Object[]{
                foodRequest.getServiceID(),
                foodRequest.getRoomNumber(),
                foodRequest.getStaffAssignment(),
                foodRequest.getAdditionalNotes(),
                foodRequest.getStatus(),
                foodRequest.getFoodSelection(),
                foodRequest.getTempType(),
                foodRequest.getAdditionalItems(),
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

