package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class MoveDaoImplTest {
    static PdbController pdbController;
    static MoveDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"uuid", "nodeID", "longName", "date"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new MoveDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.MOVES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Move> results = dao.get(move.getUuid());
        Move daoresult = results.get();
        assertEquals(daoresult,move);
        try{
            pdbController.deleteQuery(TableType.MOVES,"uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Move move2 = new Move(101L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        Object[] values2 = new Object[]{move2.getUuid(), move2.getNodeID(), move2.getLongName(), move2.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, fields, values);
            pdbController.insertQuery(TableType.MOVES, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(Move.Field.LONG_NAME, "testLong");
        var map = new HashMap<java.lang.Long, Move>();
        try (var rs = pdbController.searchQuery(TableType.MOVES, "longName", "testLong")) {
            while (rs.next()) {
                Move req = new Move(
                        (java.lang.Long)rs.getObject("uuid"),
                        (java.lang.Long)rs.getObject("nodeID"),
                        (java.lang.String)rs.getObject("longName"),
                        (java.lang.String)rs.getObject("date"));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(move.getUuid()), results.get(move.getUuid()));
        assertEquals(map.get(move2.getUuid()), results.get(move2.getUuid()));
        try{
            pdbController.deleteQuery(TableType.MOVES,"uuid", move.getUuid());
            pdbController.deleteQuery(TableType.MOVES,"uuid", move2.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[]{100L, 1005L, "testLong", "testDate"};
        var values1 = new Object[]{101L, 1006L, "testLong1", "testDate1"};
        var values2 = new Object[]{102L, 1007L, "testLong2", "testDate2"};
        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, Move>();
        for (Object[] values : valueSet) {
            var move = new Move((Long) values[0], (Long) values[1], (String) values[2], (String) values[3]);
            refMap.put(move.getUuid(), move);
            try {
                pdbController.insertQuery(TableType.MOVES, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Long, Move> resultMap = dao.getAll();
        for (var uuid : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.MOVES, "uuid", uuid);
            } catch (PdbController.DatabaseException e) {
                assert false: "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void save() {
        var dao = new MoveDaoImpl(pdbController);
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        dao.save(move);

        Move updatedMove = new Move(100L, 1005L, "updatedTestLong", "updatedTestDate");
        Move.Field[] fields = {Move.Field.LONG_NAME, Move.Field.DATE};
        dao.update(updatedMove, fields);

        Optional<Move> results = dao.get(move.getUuid());
        Move daoresult = results.get();
        assertEquals(updatedMove, daoresult);
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert test data";
        }

        try{
            var result = pdbController.searchQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to delete test data";
        }

        dao.delete(move);

        try{
            var result = pdbController.searchQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert true: "Test data deleted successfully";
        }
    }
}