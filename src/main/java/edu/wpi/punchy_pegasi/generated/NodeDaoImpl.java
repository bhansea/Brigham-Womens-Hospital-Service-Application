package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class NodeDaoImpl implements IDao<Node, String> {

    static String[] fields = {"nodeID", "xcoord", "ycoord", "floor", "building"};
    private final PdbController dbController;

    public NodeDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public NodeDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Node> get(String key) {
        try (var rs = dbController.searchQuery(TableType.NODES, "nodeID", key)) {
            rs.next();
            Node req = new Node(
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.Integer)rs.getObject("xcoord"),
                    (java.lang.Integer)rs.getObject("ycoord"),
                    (java.lang.String)rs.getObject("floor"),
                    (java.lang.String)rs.getObject("building"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Node> getAll() {
        var map = new HashMap<String, Node>();
        try (var rs = dbController.searchQuery(TableType.NODES)) {
            while (rs.next()) {
                Node req = new Node(
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.Integer)rs.getObject("xcoord"),
                    (java.lang.Integer)rs.getObject("ycoord"),
                    (java.lang.String)rs.getObject("floor"),
                    (java.lang.String)rs.getObject("building"));
                if (req != null)
                    map.put(String.valueOf(req.getNodeID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Node node) {
        Object[] values = {node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            dbController.insertQuery(TableType.NODES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Node node, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(Node node) {
        try {
            dbController.deleteQuery(TableType.NODES, "nodeID", String.valueOf(node.getNodeID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}