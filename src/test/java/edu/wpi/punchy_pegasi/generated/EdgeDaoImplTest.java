package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class EdgeDaoImplTest {

    static PdbController pdbController;
    static EdgeDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"uuid", "startNode", "endNode"};
        pdbController = new PdbController(Config.source);
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
        try {
            pdbController.insertQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Edge> results = dao.get(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(daoresult, edge);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        var edge = new Edge(1231234L, 123123L, 123123L);
        var edge2 = new Edge(12312345L, 123123L, 123123L);
        var values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        var values2 = new Object[]{edge2.getUuid(), edge2.getStartNode(), edge2.getEndNode()};
        try {
            pdbController.insertQuery(TableType.EDGES, fields, values);
            pdbController.insertQuery(TableType.EDGES, fields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = dao.get(Edge.Field.START_NODE, 123123L);
        var map = new HashMap<Long, Edge>();
        try (var rs = pdbController.searchQuery(TableType.EDGES, Edge.Field.START_NODE.getColName(), 123123L)) {
            while (rs.next()) {
                Edge req = new Edge(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.Long) rs.getObject("startNode"),
                        (java.lang.Long) rs.getObject("endNode"));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(edge.getUuid()), results.get(edge.getUuid()));
        assertEquals(map.get(edge2.getUuid()), results.get(edge2.getUuid()));
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge2.getUuid());
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
            assert false : "Failed to insert edge";
            throw new RuntimeException(e);
        }

        try {
            pdbController.searchQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to find edge";
            throw new RuntimeException(e);
        }

        dao.delete(edge);

        try {
            pdbController.searchQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted edge";
        }

    }
}