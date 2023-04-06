package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
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
        Edge edge = new Edge(123123L, 123123L, 123123L);
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
        Edge edge = new Edge(1231234L, 123123L, 123123L);
        Object[] values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try{
            pdbController.insertQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
        Optional<Edge> results = dao.get(Edge.Field.START_NODE, 123123L);
        Edge daoresult = results.get();
        assertEquals(daoresult,edge);
        try {
            pdbController.deleteQuery(TableType.EDGES,"uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var value0 = new Long[]{100L, 1005L, 1006L};
        var value1 = new Long[]{101L, 1005L, 1007L};
        var value2 = new Long[]{102L, 1005L, 1008L};
        var valueSet = new Object[][]{value0, value1, value2};
        var refMap = new HashMap<Long, Edge>();
        for (Object[] values : valueSet) {
            try {
                pdbController.insertQuery(TableType.EDGES, fields, values);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to insert edge";
            }
            Edge edge = new Edge((Long) values[0], (Long) values[1], (Long) values[2]);
            refMap.put(edge.getUuid(), edge);
        }

        Map<Long, Edge> resultMap = dao.getAll();
        for (var uuid : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.EDGES, "uuid", uuid);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
        assertEquals(refMap, resultMap);
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
        Edge edge = new Edge(100L, 1005L, 1006L);
        dao.save(edge);

        Edge updatedEdge = new Edge(100L, 2005L, 2006L);
        Edge.Field[] fields = {Edge.Field.START_NODE, Edge.Field.END_NODE};
        dao.update(updatedEdge, fields);

        Optional<Edge> results = dao.get(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(updatedEdge, daoresult);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
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