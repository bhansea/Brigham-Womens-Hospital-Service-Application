package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlowerDeliveryRequestEntryDaoImplTest {
    PdbController pdbController;

    @BeforeAll
    void init(){
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        try {
            pdbController.initTableByType(TableType.FLOWERREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        var dao = new FlowerDeliveryRequestEntryDaoImpl(pdbController);
        FlowerDeliveryRequestEntry flowers = new FlowerDeliveryRequestEntry(UUID.randomUUID(),"testPatient", "testRoomNum", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testSmall", "test1", "testTulip");
        dao.save(flowers);
        try {
            ResultSet result = pdbController.searchQuery(TableType.FLOWERREQUESTS, "serviceID",flowers.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
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
    }

    @Test
    void testGetAll() {
    }
}