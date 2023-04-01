package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.lang.Long;


public class NodeDaoImpl implements IDao<Node, Long> {
    private HashMap <Long, Node> nodes;

    public NodeDaoImpl() {
        nodes = new HashMap <Long, Node>();
    }

    @Override
    public Optional<Node> get(Long key) {
        return Optional.ofNullable(nodes.get(key));
    }

    @Override
    public List<Node> getAll() {
        return null;
    }

    @Override
    public void save(Node node) {

    }

    @Override
    public void update(Node node, String[] params) {

    }

    @Override
    public void delete(Node node) {

    }
}
