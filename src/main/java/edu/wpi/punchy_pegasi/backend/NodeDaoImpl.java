package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class NodeDaoImpl implements IDao<Node, Long> {
    private HashMap<Long, Node> nodes;
    private PdbController dbController = PdbController.getSingleton();
    public NodeDaoImpl() {
        nodes = new HashMap<Long, Node>();
    }

    @Override
    public Optional<Node> get(Long key) {
        return Optional.ofNullable(nodes.get(key));
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
            newNode.setNodeID(((Long)params[0]));
            newNode.setXcoord(((Integer)params[1]).intValue());
            newNode.setYcoord(((Integer)params[2]).intValue());
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
