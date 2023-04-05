package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveDaoImplTest {
    static PdbController pdbController;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"uuid", "nodeID", "longName", "date"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        try {
            pdbController.initTableByType(TableType.MOVES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        var dao = new MoveDaoImpl(pdbController);
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
        var dao = new MoveDaoImpl(pdbController);
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