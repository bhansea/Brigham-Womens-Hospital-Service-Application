package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LocationNameDaoImplTest {
    static PdbController pdbController;
    static LocationNameDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"uuid", "longName", "shortName", "nodeType"};
        pdbController = new PdbController(Config.source);
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