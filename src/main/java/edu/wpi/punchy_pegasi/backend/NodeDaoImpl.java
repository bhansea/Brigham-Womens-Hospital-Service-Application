package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.App;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static edu.wpi.punchy_pegasi.backend.PdbController.TableType.NODES;

@Slf4j
public class NodeDaoImpl implements IDao<Node, Long> {
    private final PdbController dbController = App.getSingleton().getPdb();
    private final String[] fields = new String[]{"nodeID", "xcoord", "ycoord", "floor", "building"};

    public NodeDaoImpl() {
    }

    @Override
    public Optional<Node> get(Long key) {
        try {
            ResultSet nodes = dbController.searchQuery(NODES, "nodeID", key);
            Node node = new Node(nodes.getLong("nodeID"), nodes.getInt("xcoord"), nodes.getInt("ycoord"), nodes.getString("floor"), nodes.getString("building"));
            return Optional.ofNullable(node);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Map<Long, Node> getAll() {
        Map<Long, Node> nodesMap = new HashMap<>();
        try {
            ResultSet nodes = dbController.searchQuery(NODES);
            while (nodes.next()) {
                Node node = new Node(nodes.getLong("nodeID"), nodes.getInt("xcoord"), nodes.getInt("ycoord"), nodes.getString("floor"), nodes.getString("building"));
                nodesMap.put(nodes.getLong("nodeID"), node);
            }
            return nodesMap;
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error(e.getMessage());
            return nodesMap;
        }
    }

    @Override
    public void save(Node node) {
        try {
            Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
            dbController.insertQuery(NODES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void update(Node node, Object[] params) {
        try {
            dbController.updateQuery(NODES, fields, params);
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public void delete(Node node) {
        try {
            long key = node.getNodeID();
            dbController.deleteQuery(NODES, "nodeID", key);
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }
    }
}
