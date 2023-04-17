package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FacadeTest {
    static PdbController pdbController;
    static Facade facade;
    static String[] nodeFields;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        nodeFields = new String[]{"nodeID", "xcoord", "ycoord", "floor", "building"};
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

    }

    @Test
    void testGetEdge() {
    }

    @Test
    void testGetEdge1() {
    }

    @Test
    void getAllEdge() {
    }

    @Test
    void saveEdge() {
    }

    @Test
    void updateEdge() {
    }

    @Test
    void deleteEdge() {
    }

    @Test
    void getMove() {
    }

    @Test
    void testGetMove() {
    }

    @Test
    void testGetMove1() {
    }

    @Test
    void getAllMove() {
    }

    @Test
    void saveMove() {
    }

    @Test
    void updateMove() {
    }

    @Test
    void deleteMove() {
    }

    @Test
    void getLocationName() {
    }

    @Test
    void testGetLocationName() {
    }

    @Test
    void testGetLocationName1() {
    }

    @Test
    void getAllLocationName() {
    }

    @Test
    void saveLocationName() {
    }

    @Test
    void updateLocationName() {
    }

    @Test
    void deleteLocationName() {
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
    }

    @Test
    void testGetFoodServiceRequestEntry() {
    }

    @Test
    void testGetFoodServiceRequestEntry1() {
    }

    @Test
    void getAllFoodServiceRequestEntry() {
    }

    @Test
    void saveFoodServiceRequestEntry() {
    }

    @Test
    void updateFoodServiceRequestEntry() {
    }

    @Test
    void deleteFoodServiceRequestEntry() {
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