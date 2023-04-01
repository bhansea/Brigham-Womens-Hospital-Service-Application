package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class EdgeDaoImpl implements IDao<Edge, String> {
    private HashMap<String, Edge> edges;

    public EdgeDaoImpl() {
        edges = new HashMap<String, Edge>();
    }

    @Override
    public Optional<Edge> get(String s) {
        return Optional.ofNullable(edges.get(s));
    }

    @Override
    public List<Edge> getAll() {
        return null;
    }

    @Override
    public void save(Edge edge) {

    }

    @Override
    public void update(Edge edge, String[] params) {

    }

    @Override
    public void delete(Edge edge) {

    }
}
