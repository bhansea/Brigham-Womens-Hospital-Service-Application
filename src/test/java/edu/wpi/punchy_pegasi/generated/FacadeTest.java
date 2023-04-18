package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FacadeTest {
    static PdbController pdbController;
    static Facade facade;
    static String[] nodeFields;
    static String[] edgeFields;
    static String[] moveFields;
    static String[] locationNameFields;
    static String[] requestFields;
    static String[] foodServiceFields;
    static String[] flowerDeliveryFields;
    static String[] conferenceRoomFields;
    static String[] furnitureRequestFields;
    static String[] officeServiceFields;
    static String[] employeeFields;
    static String[] accountFields;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        nodeFields = new String[]{"nodeID", "xcoord", "ycoord", "floor", "building"};
        edgeFields = new String[]{"uuid", "startNode", "endNode"};
        moveFields = new String[]{"uuid", "nodeID", "longName", "date"};
        locationNameFields = new String[]{"uuid", "longName", "shortName", "nodeType"};
        foodServiceFields = new String[]{"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "foodSelection", "tempType", "additionalItems", "beverage", "dietaryRestrictions", "patientName"};
        flowerDeliveryFields = new String[]{"serviceID", "patientName", "locationName", "staffAssignment", "additionalNotes", "status", "flowerSize", "flowerAmount", "flowerType"};
        conferenceRoomFields = new String[]{"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "beginningTime", "endTime", "date"};
        furnitureRequestFields = new String[]{"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "selectFurniture"};
        officeServiceFields = new String[]{"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "officeRequest", "employeeName"};
        pdbController = new PdbController(Config.source, "test");
        facade = new Facade(pdbController);
        for (var tt : TableType.values()) {
            try {
                pdbController.initTableByType(tt);
            } catch (PdbController.DatabaseException e) {
                log.error("Could not init table " + tt.name());
            }
        }

    }

    @AfterAll
    static void tearDown() throws SQLException {
        var statement = pdbController.exposeConnection().createStatement();
        statement.execute("drop schema test cascade;");
    }

    @Test
    void getNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            pdbController.insertQuery(TableType.NODES, nodeFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Node> results = facade.getNode(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(daoresult, node);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Node node2 = new Node(101L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        Object[] values2 = new Object[]{node2.getNodeID(), node2.getXcoord(), node2.getYcoord(), node2.getFloor(), node2.getBuilding()};
        try {
            pdbController.insertQuery(TableType.NODES, nodeFields, values);
            pdbController.insertQuery(TableType.NODES, nodeFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = facade.getNode(Node.Field.BUILDING, "testBuilding");
        var map = new HashMap<Long, Node>();
        try (var rs = pdbController.searchQuery(TableType.NODES, "building", "testBuilding")) {
            while (rs.next()) {
                Node req = new Node(
                        (java.lang.Long) rs.getObject("nodeID"),
                        (java.lang.Integer) rs.getObject("xcoord"),
                        (java.lang.Integer) rs.getObject("ycoord"),
                        (java.lang.String) rs.getObject("floor"),
                        (java.lang.String) rs.getObject("building"));
                if (req != null)
                    map.put(req.getNodeID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(node.getNodeID()), results.get(node.getNodeID()));
        assertEquals(map.get(node2.getNodeID()), results.get(node2.getNodeID()));
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
            pdbController.deleteQuery(TableType.NODES, "nodeID", node2.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetNode1() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Node node2 = new Node(101L, 400, 500, "L1", "testBuilding");
        Node node3 = new Node(102L, 500, 500, "L1", "testBuilding1");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        Object[] values2 = new Object[]{node2.getNodeID(), node2.getXcoord(), node2.getYcoord(), node2.getFloor(), node2.getBuilding()};
        Object[] values3 = new Object[]{node3.getNodeID(), node3.getXcoord(), node3.getYcoord(), node3.getFloor(), node3.getBuilding()};
        try {
            pdbController.insertQuery(TableType.NODES, nodeFields, values);
            pdbController.insertQuery(TableType.NODES, nodeFields, values2);
            pdbController.insertQuery(TableType.NODES, nodeFields, values3);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Node.Field[] fields = {Node.Field.BUILDING, Node.Field.XCOORD};
        Object[] searchValues = new Object[]{"testBuilding", 500};
        String[] searchFields = new String[]{"building", "xcoord"};
        var results = facade.getNode(fields, searchValues);
        var map = new HashMap<Long, Node>();
        try (var rs = pdbController.searchQuery(TableType.NODES, searchFields, searchValues)) {
            while (rs.next()) {
                Node req = new Node(
                        (java.lang.Long) rs.getObject("nodeID"),
                        (java.lang.Integer) rs.getObject("xcoord"),
                        (java.lang.Integer) rs.getObject("ycoord"),
                        (java.lang.String) rs.getObject("floor"),
                        (java.lang.String) rs.getObject("building"));
                if (req != null)
                    map.put(req.getNodeID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(node.getNodeID()), results.get(node.getNodeID()));
        assertEquals(map.get(node2.getNodeID()), results.get(node2.getNodeID()));
        assertEquals(map.get(node3.getNodeID()), results.get(node3.getNodeID()));
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
            pdbController.deleteQuery(TableType.NODES, "nodeID", node2.getNodeID());
            pdbController.deleteQuery(TableType.NODES, "nodeID", node3.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllNode() {
        var values0 = new Object[]{100L, 500, 500, "L1", "testBuilding0"};
        var values1 = new Object[]{101L, 501, 501, "L1", "testBuilding1"};
        var values2 = new Object[]{102L, 502, 502, "L1", "testBuilding2"};
        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, Node>();
        for (var values : valueSet) {
            try {
                pdbController.insertQuery(TableType.NODES, nodeFields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
            var node = new Node((Long) values[0], (Integer) values[1], (Integer) values[2], (String) values[3], (String) values[4]);
            refMap.put(node.getNodeID(), node);
        }

        Map<Long, Node> resultMap = facade.getAllNode();
        for (var key : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.NODES, "nodeID", key);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void saveNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        facade.saveNode(node);
        Optional<Node> results = facade.getNode(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(node, daoresult);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        facade.saveNode(node);

        Node updatedNode = new Node(100L, 1500, 1500, "L2", "updatedTestBuilding");
        Node.Field[] fields = {Node.Field.XCOORD, Node.Field.YCOORD, Node.Field.FLOOR, Node.Field.BUILDING};
        facade.updateNode(updatedNode, fields);

        Optional<Node> results = facade.getNode(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(updatedNode, daoresult);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            pdbController.insertQuery(TableType.NODES, nodeFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert node";
        }

        try {
            pdbController.searchQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to find node";
        }

        facade.deleteNode(node);

        try {
            pdbController.searchQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            assert true : "Node was deleted successfully";
        }
    }

    @Test
    void getEdge() {
        Edge edge = new Edge(123123L, 123123L, 123123L);
        Object[] values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try {
            pdbController.insertQuery(TableType.EDGES, edgeFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Edge> results = facade.getEdge(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(daoresult, edge);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetEdge() {
        var edge = new Edge(1231234L, 123123L, 123123L);
        var edge2 = new Edge(12312345L, 123123L, 123123L);
        var values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        var values2 = new Object[]{edge2.getUuid(), edge2.getStartNode(), edge2.getEndNode()};
        try {
            pdbController.insertQuery(TableType.EDGES, edgeFields, values);
            pdbController.insertQuery(TableType.EDGES, edgeFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = facade.getEdge(Edge.Field.START_NODE, 123123L);
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
    void testGetEdge1() {
        var edge = new Edge(1231234L, 1L, 2L);
        var edge2 = new Edge(12312345L, 2L, 2L);
        var edge3 = new Edge(123123456L, 2L, 0L);
        var values = new Object[]{edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        var values2 = new Object[]{edge2.getUuid(), edge2.getStartNode(), edge2.getEndNode()};
        var values3 = new Object[]{edge3.getUuid(), edge3.getStartNode(), edge3.getEndNode()};
        try {
            pdbController.insertQuery(TableType.EDGES, edgeFields, values);
            pdbController.insertQuery(TableType.EDGES, edgeFields, values2);
            pdbController.insertQuery(TableType.EDGES, edgeFields, values3);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Edge.Field[] fields = {Edge.Field.START_NODE, Edge.Field.END_NODE};
        Object[] searchValues = new Object[]{2L, 2L};
        String[] searchFields = new String[]{"startNode", "endNode"};
        var results = facade.getEdge(fields, searchValues);
        var map = new HashMap<Long, Edge>();
        try (var rs = pdbController.searchQuery(TableType.EDGES, searchFields, searchValues)) {
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
        assertEquals(map.get(edge3.getUuid()), results.get(edge3.getUuid()));
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge2.getUuid());
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge3.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllEdge() {
        var value0 = new Long[]{100L, 1005L, 1006L};
        var value1 = new Long[]{101L, 1005L, 1007L};
        var value2 = new Long[]{102L, 1005L, 1008L};
        var valueSet = new Object[][]{value0, value1, value2};
        var refMap = new HashMap<Long, Edge>();
        for (Object[] values : valueSet) {
            try {
                pdbController.insertQuery(TableType.EDGES, edgeFields, values);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to insert edge";
            }
            Edge edge = new Edge((Long) values[0], (Long) values[1], (Long) values[2]);
            refMap.put(edge.getUuid(), edge);
        }

        Map<Long, Edge> resultMap = facade.getAllEdge();
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
    void saveEdge() {
        Edge edge = new Edge(100L, 1005L, 1006L);
        facade.saveEdge(edge);
        Optional<Edge> results = facade.getEdge(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(edge, daoresult);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateEdge() {
        Edge edge = new Edge(100L, 1005L, 1006L);
        facade.saveEdge(edge);

        Edge updatedEdge = new Edge(100L, 2005L, 2006L);
        Edge.Field[] fields = {Edge.Field.START_NODE, Edge.Field.END_NODE};
        facade.updateEdge(updatedEdge, fields);

        Optional<Edge> results = facade.getEdge(edge.getUuid());
        Edge daoresult = results.get();
        assertEquals(updatedEdge, daoresult);
        try {
            pdbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteEdge() {
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
            pdbController.insertQuery(TableType.EDGES, edgeFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert edge";
            throw new RuntimeException(e);
        }

        try {
            pdbController.searchQuery(TableType.EDGES, edgeFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to find edge";
            throw new RuntimeException(e);
        }

        facade.deleteEdge(edge);

        try {
            pdbController.searchQuery(TableType.EDGES, edgeFields, values);
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted edge";
        }
    }

    @Test
    void getMove() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, moveFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Move> results = facade.getMove(move.getUuid());
        Move daoresult = results.get();
        assertEquals(daoresult, move);
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetMove() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Move move2 = new Move(101L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        Object[] values2 = new Object[]{move2.getUuid(), move2.getNodeID(), move2.getLongName(), move2.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, moveFields, values);
            pdbController.insertQuery(TableType.MOVES, moveFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = facade.getMove(Move.Field.LONG_NAME, "testLong");
        var map = new HashMap<java.lang.Long, Move>();
        try (var rs = pdbController.searchQuery(TableType.MOVES, "longName", "testLong")) {
            while (rs.next()) {
                Move req = new Move(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.Long) rs.getObject("nodeID"),
                        (java.lang.String) rs.getObject("longName"),
                        (java.lang.String) rs.getObject("date"));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(move.getUuid()), results.get(move.getUuid()));
        assertEquals(map.get(move2.getUuid()), results.get(move2.getUuid()));
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
            pdbController.deleteQuery(TableType.MOVES, "uuid", move2.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetMove1() {
        Move move = new Move(100L, 1005L, "testLong", "testDate0");
        Move move2 = new Move(101L, 1000L, "testLongName", "testDate1");
        Move move3 = new Move(102L, 1005L, "testLongName", "testDate2");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        Object[] values2 = new Object[]{move2.getUuid(), move2.getNodeID(), move2.getLongName(), move2.getDate()};
        Object[] values3 = new Object[]{move3.getUuid(), move3.getNodeID(), move3.getLongName(), move3.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, moveFields, values);
            pdbController.insertQuery(TableType.MOVES, moveFields, values2);
            pdbController.insertQuery(TableType.MOVES, moveFields, values3);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Move.Field[] fields = {Move.Field.LONG_NAME, Move.Field.NODE_ID};
        Object[] searchValues = new Object[]{"testLongName", 1005L};
        String[] searchFields = new String[]{"longName", "nodeID"};
        var results = facade.getMove(fields, searchValues);
        var map = new HashMap<java.lang.Long, Move>();
        try (var rs = pdbController.searchQuery(TableType.MOVES, searchFields, searchValues)) {
            while (rs.next()) {
                Move req = new Move(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.Long) rs.getObject("nodeID"),
                        (java.lang.String) rs.getObject("longName"),
                        (java.lang.String) rs.getObject("date"));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(move.getUuid()), results.get(move.getUuid()));
        assertEquals(map.get(move2.getUuid()), results.get(move2.getUuid()));
        assertEquals(map.get(move3.getUuid()), results.get(move3.getUuid()));
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
            pdbController.deleteQuery(TableType.MOVES, "uuid", move2.getUuid());
            pdbController.deleteQuery(TableType.MOVES, "uuid", move3.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllMove() {
        var values0 = new Object[]{100L, 1005L, "testLong", "testDate"};
        var values1 = new Object[]{101L, 1006L, "testLong1", "testDate1"};
        var values2 = new Object[]{102L, 1007L, "testLong2", "testDate2"};
        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, Move>();
        for (Object[] values : valueSet) {
            var move = new Move((Long) values[0], (Long) values[1], (String) values[2], (String) values[3]);
            refMap.put(move.getUuid(), move);
            try {
                pdbController.insertQuery(TableType.MOVES, moveFields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Long, Move> resultMap = facade.getAllMove();
        for (var uuid : refMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.MOVES, "uuid", uuid);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void saveMove() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        facade.saveMove(move);
        Move updatedMove = new Move(100L, 1005L, "updatedTestLong", "updatedTestDate");
        Move.Field[] fields = {Move.Field.LONG_NAME, Move.Field.DATE};
        facade.updateMove(updatedMove, fields);
        Optional<Move> results = facade.getMove(move.getUuid());
        Move daoresult = results.get();
        assertEquals(updatedMove, daoresult);
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateMove() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        facade.saveMove(move);
        Move updatedMove = new Move(100L, 1500L, "updatedTestLong", "testDate");
        Move.Field[] fields = {Move.Field.UUID, Move.Field.NODE_ID, Move.Field.LONG_NAME, Move.Field.DATE};
        facade.updateMove(updatedMove, fields);

        Optional<Move> results = facade.getMove(move.getUuid());
        Move daoresult = results.get();
        assertEquals(updatedMove, daoresult);
        try {
            pdbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteMove() {
        Move move = new Move(100L, 1005L, "testLong", "testDate");
        Object[] values = new Object[]{move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            pdbController.insertQuery(TableType.MOVES, moveFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert test data";
        }

        try {
            var result = pdbController.searchQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to delete test data";
        }

        facade.deleteMove(move);

        try {
            var result = pdbController.searchQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert true : "Test data deleted successfully";
        }
    }

    @Test
    void getLocationName() {
        LocationName location = new LocationName(100L, "testName", "testName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(), location.getShortName(), location.getNodeType()};
        try {
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<LocationName> results = facade.getLocationName(location.getUuid());
        LocationName daoresult = results.get();
        assertEquals(daoresult, location);
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetLocationName() {
        LocationName location = new LocationName(100L, "testName", "testName", LocationName.NodeType.HALL);
        LocationName location2 = new LocationName(101L, "testName", "testName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(), location.getShortName(), location.getNodeType()};
        Object[] values2 = new Object[]{location2.getUuid(), location2.getLongName(), location2.getShortName(), location2.getNodeType()};
        try {
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values);
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = facade.getLocationName(LocationName.Field.SHORT_NAME, "testName");
        var map = new HashMap<java.lang.Long, LocationName>();
        try (var rs = pdbController.searchQuery(TableType.LOCATIONNAMES, "shortName", "testName")) {
            while (rs.next()) {
                LocationName req = new LocationName(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.String) rs.getObject("longName"),
                        (java.lang.String) rs.getObject("shortName"),
                        edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf((String) rs.getObject("nodeType")));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(location.getUuid()), results.get(location.getUuid()));
        assertEquals(map.get(location2.getUuid()), results.get(location2.getUuid()));
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location2.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetLocationName1() {
        LocationName location = new LocationName(100L, "testName", "testName", LocationName.NodeType.HALL);
        LocationName location2 = new LocationName(101L, "testName", "testName", LocationName.NodeType.HALL);
        LocationName location3 = new LocationName(102L, "testName1", "testName1", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(), location.getShortName(), location.getNodeType()};
        Object[] values2 = new Object[]{location2.getUuid(), location2.getLongName(), location2.getShortName(), location2.getNodeType()};
        Object[] values3 = new Object[]{location3.getUuid(), location3.getLongName(), location3.getShortName(), location3.getNodeType()};
        try {
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values);
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values2);
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values3);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        LocationName.Field[] fields = {LocationName.Field.LONG_NAME, LocationName.Field.SHORT_NAME};
        Object[] searchValues = new Object[]{"testName1", "testName1"};
        String[] searchFields = new String[]{"longName", "shortName"};
        var results = facade.getLocationName(fields, searchValues);
        var map = new HashMap<java.lang.Long, LocationName>();
        try (var rs = pdbController.searchQuery(TableType.LOCATIONNAMES, searchFields, searchValues)) {
            while (rs.next()) {
                LocationName req = new LocationName(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.String) rs.getObject("longName"),
                        (java.lang.String) rs.getObject("shortName"),
                        edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf((String) rs.getObject("nodeType")));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(location.getUuid()), results.get(location.getUuid()));
        assertEquals(map.get(location2.getUuid()), results.get(location2.getUuid()));
        assertEquals(map.get(location3.getUuid()), results.get(location3.getUuid()));
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location2.getUuid());
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", location3.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllLocationName() {
        var values0 = new Object[]{100L, "LongtestName0", "testName0", LocationName.NodeType.HALL};
        var values1 = new Object[]{101L, "LongtestName1", "testName1", LocationName.NodeType.HALL};
        var values2 = new Object[]{102L, "LongtestName2", "testName2", LocationName.NodeType.HALL};

        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, LocationName>();
        for (Object[] values : valueSet) {
            var location = new LocationName((Long) values[0], (String) values[1], (String) values[2], (LocationName.NodeType) values[3]);
            refMap.put(location.getUuid(), location);
            try {
                pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
        }

        Map<Long, LocationName> resultMap = facade.getAllLocationName();
        for (var uuid : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", uuid);
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);
    }

    @Test
    void saveLocationName() {
        Long uuid = 100L;
        LocationName ln = new LocationName(uuid, "testName", "testName", LocationName.NodeType.HALL);
        facade.saveLocationName(ln);
        Optional<LocationName> results = facade.getLocationName(ln.getUuid());
        LocationName daoresult = results.get();
        assertEquals(ln, daoresult);
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", ln.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateLocationName() {
        Long uuid = 100L;
        LocationName ln = new LocationName(uuid, "testName", "testName", LocationName.NodeType.HALL);
        facade.saveLocationName(ln);
        Optional<LocationName> results = facade.getLocationName(ln.getUuid());
        LocationName daoresult = results.get();
        assertEquals(ln, daoresult);
        try {
            pdbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", ln.getUuid());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteLocationName() {
        LocationName location = new LocationName(100L, "testLName", "testSName", LocationName.NodeType.HALL);
        Object[] values = new Object[]{location.getUuid(), location.getLongName(), location.getShortName(), location.getNodeType()};
        try {
            pdbController.insertQuery(TableType.LOCATIONNAMES, locationNameFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert into database";
        }
        try {
            pdbController.searchQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to search database";
        }

        facade.deleteLocationName(location);

        try {
            pdbController.searchQuery(TableType.LOCATIONNAMES, "uuid", location.getUuid());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted from database";
        }
    }

    @Test
    void getRequestEntry() {

    }

    @Test
    void testGetRequestEntry() {
    }

    @Test
    void testGetRequestEntry1() {
    }

    @Test
    void getAllRequestEntry() {
    }

    @Test
    void saveRequestEntry() {
    }

    @Test
    void updateRequestEntry() {
    }

    @Test
    void deleteRequestEntry() {
    }

    @Test
    void getFoodServiceRequestEntry() {
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        FoodServiceRequestEntry food = new FoodServiceRequestEntry(UUID.randomUUID(), locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        Object[] values = new Object[]{food.getServiceID(), food.getLocationName(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getBeverage(),food.getDietaryRestrictions(), food.getPatientName()};
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<FoodServiceRequestEntry> results = facade.getFoodServiceRequestEntry(food.getServiceID());
        FoodServiceRequestEntry daoresult = results.get();
        assertEquals(daoresult, food);
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetFoodServiceRequestEntry() {
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var food = new FoodServiceRequestEntry(UUID.randomUUID(), locName0, staff0, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        var food2 = new FoodServiceRequestEntry(UUID.randomUUID(), locName1, staff1, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        var values = new Object[]{food.getServiceID(), food.getLocationName(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getBeverage(), food.getDietaryRestrictions(), food.getPatientName()};
        var values2 = new Object[]{food2.getServiceID(), food2.getLocationName(), food2.getStaffAssignment(), food2.getAdditionalNotes(), food2.getStatus(), food2.getFoodSelection(), food2.getTempType(), food2.getAdditionalItems(), food2.getBeverage(), food2.getDietaryRestrictions(), food2.getPatientName()};
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values);
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        var results = facade.getFoodServiceRequestEntry(FoodServiceRequestEntry.Field.STAFF_ASSIGNMENT, staff0);
        var map = new HashMap<java.util.UUID, FoodServiceRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.FOODREQUESTS, "staffAssignment", staff0)) {
            while (rs.next()) {
                String myCol = rs.getString("additionalItems");
                List<String> myColumnList = Arrays.asList(myCol.split(","));
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                        (java.util.UUID) rs.getObject("serviceID"),
                        (java.lang.Long) rs.getObject("locationName"),
                        (java.lang.Long) rs.getObject("staffAssignment"),
                        rs.getObject("additionalNotes", String.class),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String) rs.getObject("status")),
                        (java.lang.String) rs.getObject("foodSelection"),
                        (java.lang.String) rs.getObject("tempType"),
                        myColumnList,
                        (java.lang.String) rs.getObject("beverage"),
                        (java.lang.String) rs.getObject("dietaryRestrictions"),
                        (java.lang.String) rs.getObject("patientName"));
                map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            assert false : e.getMessage();
        }
        assertEquals(map.get(food.getServiceID()), results.get(food.getServiceID()));
        assertEquals(map.get(food2.getServiceID()), results.get(food2.getServiceID()));
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food.getServiceID());
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food2.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetFoodServiceRequestEntry1() {
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var food = new FoodServiceRequestEntry(UUID.randomUUID(), locName0, staff0, "testNotes1", RequestEntry.Status.PROCESSING, "testFood1", "testTemp1", additionalItems, "juice", "testRestrictions", "testPatient");
        var food2 = new FoodServiceRequestEntry(UUID.randomUUID(), locName1, staff1, "testNotes2", RequestEntry.Status.PROCESSING, "testFood2", "testTemp2", additionalItems, "juice", "testRestrictions", "testPatient");
        var values = new Object[]{food.getServiceID(), food.getLocationName(), food.getStaffAssignment(), food.getAdditionalNotes(), food.getStatus(), food.getFoodSelection(), food.getTempType(), food.getAdditionalItems(), food.getBeverage(), food.getDietaryRestrictions(), food.getPatientName()};
        var values2 = new Object[]{food2.getServiceID(), food2.getLocationName(), food2.getStaffAssignment(), food2.getAdditionalNotes(), food2.getStatus(), food2.getFoodSelection(), food2.getTempType(), food2.getAdditionalItems(), food2.getBeverage(), food2.getDietaryRestrictions(), food2.getPatientName()};
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values);
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values2);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        FoodServiceRequestEntry.Field[] fields = {FoodServiceRequestEntry.Field.ADDITIONAL_NOTES, FoodServiceRequestEntry.Field.FOOD_SELECTION};
        Object[] searchValues = new Object[]{"testNotes1", "testFood2"};
        String[] searchFields = new String[]{"additionalNotes", "foodSelection"};
        var results = facade.getFoodServiceRequestEntry(fields, searchValues);
        var map = new HashMap<java.util.UUID, FoodServiceRequestEntry>();
        try (var rs = pdbController.searchQuery(TableType.FOODREQUESTS, searchFields, searchValues)) {
            while (rs.next()) {
                String myCol = rs.getString("additionalItems");
                List<String> myColumnList = Arrays.asList(myCol.split(","));
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                        (java.util.UUID) rs.getObject("serviceID"),
                        (java.lang.Long) rs.getObject("locationName"),
                        (java.lang.Long) rs.getObject("staffAssignment"),
                        rs.getObject("additionalNotes", String.class),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String) rs.getObject("status")),
                        (java.lang.String) rs.getObject("foodSelection"),
                        (java.lang.String) rs.getObject("tempType"),
                        myColumnList,
                        (java.lang.String) rs.getObject("beverage"),
                        (java.lang.String) rs.getObject("dietaryRestrictions"),
                        (java.lang.String) rs.getObject("patientName"));
                map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            assert false : e.getMessage();
        }
        assertEquals(map.get(food.getServiceID()), results.get(food.getServiceID()));
        assertEquals(map.get(food2.getServiceID()), results.get(food2.getServiceID()));
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food.getServiceID());
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", food2.getServiceID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllFoodServiceRequestEntry() {
        var locName0 = ThreadLocalRandom.current().nextLong();
        var staff0 = ThreadLocalRandom.current().nextLong();
        var locName1 = ThreadLocalRandom.current().nextLong();
        var staff1 = ThreadLocalRandom.current().nextLong();
        var locName2 = ThreadLocalRandom.current().nextLong();
        var staff2 = ThreadLocalRandom.current().nextLong();
        var additionalItem = "testItems";
        var values0 = new Object[]{UUID.randomUUID(), locName0, staff0, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var values1 = new Object[]{UUID.randomUUID(), locName1, staff1, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var values2 = new Object[]{UUID.randomUUID(), locName2, staff2, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", List.of(additionalItem), "juice", "testRestrictions", "testPatient"};
        var valuesSet = new Object[][]{values0, values1, values2};
        var refMap = new HashMap<java.util.UUID, FoodServiceRequestEntry>();

        for (var values : valuesSet) {
            try {
                pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
            FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID) values[0],
                    (java.lang.Long) values[1],
                    (java.lang.Long) values[2],
                    (java.lang.String) values[3],
                    (RequestEntry.Status) values[4],
                    (java.lang.String) values[5],
                    (java.lang.String) values[6],
                    (List<String>) values[7],
                    (java.lang.String) values[8],
                    (java.lang.String) values[9],
                    (java.lang.String) values[10]);
            refMap.put(req.getServiceID(), req);
        }

        Map<UUID, FoodServiceRequestEntry> resultMap = facade.getAllFoodServiceRequestEntry();

        for (var key : resultMap.keySet()) {
            try {
                pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", key);
            } catch (PdbController.DatabaseException e) {
                assert false : e.getMessage();
            }
        }

        assertEquals(refMap, resultMap);
    }

    @Test
    void saveFoodServiceRequestEntry() {
        UUID uuid = UUID.randomUUID();
        List<String> additionalItems = new ArrayList<>();
        additionalItems.add("testItems");
        var locName = ThreadLocalRandom.current().nextLong();
        var staff = ThreadLocalRandom.current().nextLong();
        FoodServiceRequestEntry fsre = new FoodServiceRequestEntry(uuid, locName, staff, "testNotes", RequestEntry.Status.PROCESSING, "testFood", "testTemp", additionalItems, "juice", "testRestrictions", "testPatient");
        facade.saveFoodServiceRequestEntry(fsre);

        Optional<FoodServiceRequestEntry> results = facade.getFoodServiceRequestEntry(uuid);
        FoodServiceRequestEntry daoresult = results.get();
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        assertEquals(fsre, daoresult);
    }

    @Test
    void updateFoodServiceRequestEntry() {
        UUID uuid = UUID.randomUUID();
        FoodServiceRequestEntry foodRequest = new FoodServiceRequestEntry(
                uuid,
                100L,
                100L,
                "testNode0",
                RequestEntry.Status.PROCESSING,
                "testFood0",
                "100",
                List.of("item1", "item2"),
                "Juice",
                "restrictions0",
                "patientName0"
        );
        FoodServiceRequestEntry updateFoodRequest = new FoodServiceRequestEntry(
                uuid,
                100L,
                100L,
                "testNode0",
                RequestEntry.Status.PROCESSING,
                "testFood0",
                "100",
                List.of("item1", "item2"),
                "Apple Juice",
                "restrictions0",
                "patientName0"
        );
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, new Object[]{
                    foodRequest.getServiceID(),
                    foodRequest.getLocationName(),
                    foodRequest.getStaffAssignment(),
                    foodRequest.getAdditionalNotes(),
                    foodRequest.getStatus(),
                    foodRequest.getFoodSelection(),
                    foodRequest.getTempType(),
                    foodRequest.getAdditionalItems(),
                    foodRequest.getBeverage(),
                    foodRequest.getDietaryRestrictions(),
                    foodRequest.getPatientName()
            });
        } catch (PdbController.DatabaseException e) {
            assert false : e.getMessage();
        }
        FoodServiceRequestEntry.Field[] updateFields = {
                FoodServiceRequestEntry.Field.BEVERAGE
        };
        facade.updateFoodServiceRequestEntry(updateFoodRequest, updateFields);
        Optional<FoodServiceRequestEntry> fsrq = facade.getFoodServiceRequestEntry(uuid);
        FoodServiceRequestEntry daoresult = fsrq.get();
        assertEquals(daoresult, updateFoodRequest);
        try {
            pdbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteFoodServiceRequestEntry() {
        FoodServiceRequestEntry foodRequest = new FoodServiceRequestEntry(
                UUID.randomUUID(),
                ThreadLocalRandom.current().nextLong(),
                ThreadLocalRandom.current().nextLong(),
                "testNode",
                RequestEntry.Status.PROCESSING,
                "testFood",
                "100",
                List.of("item1", "item2"),
                "juice",
                "restrictions",
                "patientName"
        );


        var values = new Object[]{
                foodRequest.getServiceID(),
                foodRequest.getLocationName(),
                foodRequest.getStaffAssignment(),
                foodRequest.getAdditionalNotes(),
                foodRequest.getStatus(),
                foodRequest.getFoodSelection(),
                foodRequest.getTempType(),
                foodRequest.getAdditionalItems(),
                foodRequest.getBeverage(),
                foodRequest.getDietaryRestrictions(),
                foodRequest.getPatientName()
        };
        try {
            pdbController.insertQuery(TableType.FOODREQUESTS, foodServiceFields, values);
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to insert test data";
        }

        try {
            pdbController.searchQuery(TableType.FOODREQUESTS, "serviceID", foodRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false : "Failed to find test data";
        }

        facade.deleteFoodServiceRequestEntry(foodRequest);

        try {
            pdbController.searchQuery(TableType.FOODREQUESTS, "serviceID", foodRequest.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert true : "Successfully deleted test data";
        }
    }

    @Test
    void getFlowerDeliveryRequestEntry() {

    }

    @Test
    void testGetFlowerDeliveryRequestEntry() {
    }

    @Test
    void testGetFlowerDeliveryRequestEntry1() {
    }

    @Test
    void getAllFlowerDeliveryRequestEntry() {
    }

    @Test
    void saveFlowerDeliveryRequestEntry() {
    }

    @Test
    void updateFlowerDeliveryRequestEntry() {
    }

    @Test
    void deleteFlowerDeliveryRequestEntry() {
    }

    @Test
    void getConferenceRoomEntry() {
    }

    @Test
    void testGetConferenceRoomEntry() {
    }

    @Test
    void testGetConferenceRoomEntry1() {
    }

    @Test
    void getAllConferenceRoomEntry() {
    }

    @Test
    void saveConferenceRoomEntry() {
    }

    @Test
    void updateConferenceRoomEntry() {
    }

    @Test
    void deleteConferenceRoomEntry() {
    }

    @Test
    void getFurnitureRequestEntry() {
    }

    @Test
    void testGetFurnitureRequestEntry() {
    }

    @Test
    void testGetFurnitureRequestEntry1() {
    }

    @Test
    void getAllFurnitureRequestEntry() {
    }

    @Test
    void saveFurnitureRequestEntry() {
    }

    @Test
    void updateFurnitureRequestEntry() {
    }

    @Test
    void deleteFurnitureRequestEntry() {
    }

    @Test
    void getOfficeServiceRequestEntry() {
    }

    @Test
    void testGetOfficeServiceRequestEntry() {
    }

    @Test
    void testGetOfficeServiceRequestEntry1() {
    }

    @Test
    void getAllOfficeServiceRequestEntry() {
    }

    @Test
    void saveOfficeServiceRequestEntry() {
    }

    @Test
    void updateOfficeServiceRequestEntry() {
    }

    @Test
    void deleteOfficeServiceRequestEntry() {
    }

    @Test
    void getEmployee() {
    }

    @Test
    void testGetEmployee() {
    }

    @Test
    void testGetEmployee1() {
    }

    @Test
    void getAllEmployee() {
    }

    @Test
    void saveEmployee() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void testGetAccount() {
    }

    @Test
    void testGetAccount1() {
    }

    @Test
    void getAllAccount() {
    }

    @Test
    void saveAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }
}