package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EdgeDaoImplTest {

    static PdbController pdbController;
    static EdgeDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"uuid", "startNode", "endNode"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new EdgeDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.EDGES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        var dao = new EdgeDaoImpl(pdbController);
        Edge edge = new Edge(100L, 1005L, 1006L);
        Object[] values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try{
            pdbController.insertQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
        Optional<Edge> results = dao.get(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(daoresult,edge);
        try {
            pdbController.deleteQuery(TableType.EDGES,"uuid", edge.getUuid());
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
        var dao = new EdgeDaoImpl(pdbController);
        Edge edge = new Edge(100L, 1005L, 1006L);
        dao.save(edge);
        Optional<Edge> results = dao.get(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(edge, daoresult);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
        var dao = new EdgeDaoImpl(pdbController);
        long uuid = 1111;
        long startNode = 2222;
        long endNode = 3333;
        Edge edge = new Edge(uuid, startNode, endNode);
        var values = new Long[]{
                edge.getUuid(),
                edge.getStartNode(),
                edge.getEndNode()
        };
        try {
            pdbController.insertQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert edge";
            throw new RuntimeException(e);
        }

        try {
            pdbController.searchQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to find edge";
            throw new RuntimeException(e);
        }

        dao.delete(edge);

        try {
            pdbController.searchQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert true: "Successfully deleted edge";
        }

    }
}