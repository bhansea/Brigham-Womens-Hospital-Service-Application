package edu.wpi.punchy_pegasi.backend.generated;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.backend.TestDB;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.backend.Node;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class NodeDaoImpl implements IDao<Node, String> {

    static String[] fields = {"nodeID", "xcoord", "ycoord", "floor", "building"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<Node> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.NODES, "nodeID", key)) {
            rs.next();
            Node req = new Node(
                    (long)rs.getObject("nodeID"),
                    (int)rs.getObject("xcoord"),
                    (int)rs.getObject("ycoord"),
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
        try (var rs = dbController.searchQuery(PdbController.TableType.NODES)) {
            while (rs.next()) {
                Node req = new Node(
                    (long)rs.getObject("nodeID"),
                    (int)rs.getObject("xcoord"),
                    (int)rs.getObject("ycoord"),
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
            dbController.insertQuery(PdbController.TableType.NODES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Node foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(Node foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.NODES, "nodeID", String.valueOf(foodServiceRequestEntry.getNodeID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}