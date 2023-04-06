package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NodeDaoImplTest {
    static PdbController pdbController;
    static NodeDaoImpl dao;
    static String[] fields;

    @BeforeAll
    static void init(){
        fields = new String[]{"nodeID", "xcoord", "ycoord", "floor", "building"};
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        dao = new NodeDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.NODES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try{
            pdbController.insertQuery(TableType.NODES,fields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Node> results = dao.get(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(daoresult,node);
        try{
            pdbController.deleteQuery(TableType.NODES,"nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {

    }

    @Test
    void getAll() {
        var values0 = new Object[]{100L, 500, 500, "L1", "testBuilding0"};
        var values1 = new Object[]{101L, 501, 501, "L1", "testBuilding1"};
        var values2 = new Object[]{102L, 502, 502, "L1", "testBuilding2"};
        var valueSet = new Object[][]{values0, values1, values2};

        var refMap = new HashMap<Long, Node>();
        for (var values : valueSet) {
            try {
                pdbController.insertQuery(TableType.NODES, fields, values);
            } catch (PdbController.DatabaseException e) {
                throw new RuntimeException(e);
            }
            var node = new Node((Long) values[0], (Integer) values[1], (Integer) values[2], (String) values[3], (String) values[4]);
            refMap.put(node.getNodeID(), node);
        }

        Map<Long, Node> resultMap = dao.getAll();
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
    void save() {
        var dao = new NodeDaoImpl(pdbController);
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        dao.save(node);
        Optional<Node> results = dao.get(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(node, daoresult);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        var dao = new NodeDaoImpl(pdbController);
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        dao.save(node);

        Node updatedNode = new Node(100L, 1500, 1500, "L2", "updatedTestBuilding");
        Node.Field[] fields = {Node.Field.XCOORD, Node.Field.YCOORD, Node.Field.FLOOR, Node.Field.BUILDING};
        dao.update(updatedNode, fields);

        Optional<Node> results = dao.get(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(updatedNode, daoresult);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try{
            pdbController.insertQuery(TableType.NODES,fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert node";
        }

        try{
            pdbController.searchQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to find node";
        }

        dao.delete(node);

        try{
            pdbController.searchQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            assert true: "Node was deleted successfully";
        }
    }
}