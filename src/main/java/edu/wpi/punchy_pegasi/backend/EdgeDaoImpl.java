package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EdgeDaoImpl implements IDao<Edge, Long> {
    private HashMap<Long, Edge> edges;
    private PdbController dbController = PdbController.getSingleton();

    public EdgeDaoImpl() {
        edges = new HashMap<Long, Edge>();
    }

    @Override
    public Optional<Edge> get(Long key) {
        return Optional.ofNullable(edges.get(key));
    }

    @Override
    public Map<Long, Edge> getAll() {
        return this.edges;
    }

    @Override
    public void save(Edge edge) {
        this.edges.put(edge.getUuid(), edge);
    }

    @Override
    public void update(Edge edge, Object[] params) {
        var key = edge.getUuid();
        Edge newEdge = edges.get(key);
        if (params.length != 2) {
            //TODO: throw error
        } else {
            newEdge.setStartNode(params[0].toString());
            newEdge.setEndNode(params[1].toString());
            edges.put(key, newEdge);
        }
    }

    @Override
    public void delete(Edge edge) {
        var key = edge.getUuid();
        edges.remove(key);
    }
}
