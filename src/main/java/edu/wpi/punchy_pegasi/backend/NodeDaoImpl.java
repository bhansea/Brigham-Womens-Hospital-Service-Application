package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.App;

import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static edu.wpi.punchy_pegasi.backend.PdbController.TableType.*;

@Slf4j
public class NodeDaoImpl implements IDao<Node, Long> {
    private final HashMap<Long, Node> nodes;
    private final PdbController dbController = App.getSingleton().getPdb();

    public NodeDaoImpl() {
        nodes = new HashMap<Long, Node>();
    }

    @Override
    public Optional<Node> get(Long key) {
        try {
            ResultSet nodes = dbController.searchQuery(NODES, "nodeID", key);
            Node node = new Node(nodes.getLong("nodeID"), nodes.getInt("xcoord"), nodes.getInt("ycoord"), nodes.getString("floor"), nodes.getString("building"));
            return Optional.ofNullable(node);
        } catch (PdbController.DatabaseException | SQLException e){
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Map<Long, Node> getAll() {
        
        return nodes;
    }

    @Override
    public void save(Node node) {
        this.nodes.put(node.getNodeID(), node);
    }

    @Override
    public void update(Node node, Object[] params) {
        var key = node.getNodeID();
        Node newNode = nodes.get(key);
        if (params.length != 5) {
            //TODO: throw error
        } else {
            newNode.setNodeID(((Long) params[0]));
            newNode.setXcoord(((Integer) params[1]).intValue());
            newNode.setYcoord(((Integer) params[2]).intValue());
            newNode.setFloor(params[3].toString());
            newNode.setBuilding((params[4]).toString());
            nodes.put(key, node);
        }
    }

    @Override
    public void delete(Node node) {
        var key = node.getNodeID();
        nodes.remove(key);
    }
}
