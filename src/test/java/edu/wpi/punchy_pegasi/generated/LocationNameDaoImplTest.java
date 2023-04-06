package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationNameDaoImplTest {
    static PdbController pdbController;
    static LocationNameDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"uuid", "longName", "shortName", "nodeType"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new LocationNameDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.LOCATIONNAMES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        LocationName location = new LocationName(100L, "testName", "testName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(),location.getShortName(),location.getNodeType()};
        try{
            pdbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<LocationName> results = dao.get(location.getUuid());
        LocationName daoresult = results.get();
        assertEquals(daoresult,location);
        try{
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
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
        var dao = new LocationNameDaoImpl(pdbController);
        Long uuid = 100L;
        LocationName ln = new LocationName(uuid, "testName", "testName", LocationName.NodeType.HALL);
        dao.save(ln);
        Optional<LocationName> results = dao.get(ln.getUuid());
        LocationName daoresult = results.get();
        assertEquals(ln, daoresult);
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", ln.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        LocationName location = new LocationName(100L, "testLName", "testSName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(),location.getShortName(),location.getNodeType()};
        try{
            pdbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert into database";
        }
        try{
            pdbController.searchQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to search database";
        }

        dao.delete(location);

        try{
            pdbController.searchQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert true: "Successfully deleted from database";
        }
    }
}