package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
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
        LocationName location = new LocationName(100L, "testName", "testName", LocationName.NodeType.HALL);
        LocationName location2 = new LocationName(101L, "testName", "testName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(),location.getShortName(),location.getNodeType()};
        Object[] values2 = new Object[]{location2.getUuid(), location2.getLongName(),location2.getShortName(),location2.getNodeType()};
        try{
            pdbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
            pdbController.insertQuery(TableType.LOCATIONNAMES, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(LocationName.Field.SHORT_NAME, "testName");
        var map = new HashMap<java.lang.Long, LocationName>();
        try (var rs = pdbController.searchQuery(TableType.LOCATIONNAMES, "shortName", "testName")) {
            while (rs.next()) {
                LocationName req = new LocationName(
                        (java.lang.Long)rs.getObject("uuid"),
                        (java.lang.String)rs.getObject("longName"),
                        (java.lang.String)rs.getObject("shortName"),
                        edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf((String)rs.getObject("nodeType")));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(location.getUuid()), results.get(location.getUuid()));
        assertEquals(map.get(location2.getUuid()), results.get(location2.getUuid()));
        try{
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location2.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[]{100L, "LongtestName0", "testName0", LocationName.NodeType.HALL};
        var values1 = new Object[]{101L, "LongtestName1", "testName1", LocationName.NodeType.HALL};
        var values2 = new Object[]{102L, "LongtestName2", "testName2", LocationName.NodeType.HALL};

        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, LocationName>();
        for (Object[] values : valueSet) {
            var location = new LocationName((Long) values[0], (String) values[1], (String) values[2], (LocationName.NodeType) values[3]);
            refMap.put(location.getUuid(), location);
            try {
                pdbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Long, LocationName> resultMap = dao.getAll();
        for (var uuid : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", uuid);
            } catch (PdbController.DatabaseException e) {
                assert false: "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
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
        var dao = new LocationNameDaoImpl(pdbController);
        Long uuid = 100L;
        LocationName ln = new LocationName(uuid, "testName", "testName", LocationName.NodeType.HALL);
        dao.save(ln);

        LocationName updatedLn = new LocationName(uuid, "testName", "testName", LocationName.NodeType.DEPT);
        LocationName.Field[] fields = {LocationName.Field.NODE_TYPE};
        dao.update(updatedLn, fields);

        Optional<LocationName> results = dao.get(ln.getUuid());
        LocationName daoresult = results.get();
        assertEquals(updatedLn, daoresult);
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", ln.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
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