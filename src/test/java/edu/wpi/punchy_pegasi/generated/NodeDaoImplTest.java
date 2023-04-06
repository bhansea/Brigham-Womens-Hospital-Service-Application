package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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