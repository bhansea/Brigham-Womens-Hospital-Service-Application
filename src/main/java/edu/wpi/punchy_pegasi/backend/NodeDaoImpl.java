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
    public void update(Long key, Object[] params) {
        Node node = nodes.get(key);
        if (params.length != 5) {
            //TODO: throw error
        } else {
            node.setNodeID(((Long)params[0]));
            node.setXcoord(((Integer)params[1]).intValue());
            node.setYcoord(((Integer)params[2]).intValue());
            node.setFloor(params[3].toString());
            node.setBuilding((params[4]).toString());
            nodes.put(key, node);
        }
    }

    @Override
    public void delete(Long key) {
        nodes.remove(key);
    }
}
