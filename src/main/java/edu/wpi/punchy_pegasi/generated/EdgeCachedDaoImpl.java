package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class EdgeCachedDaoImpl implements IDao<java.lang.Long, Edge, Edge.Field>, PropertyChangeListener {

    static String[] fields = {"uuid", "startNode", "endNode"};

    private final Map<java.lang.Long, Edge> cache = new HashMap<>();
    private final PdbController dbController;

    public EdgeCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public EdgeCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(Edge edge) {
        if (!cache.containsKey(edge.getUuid()))
            cache.put(edge.getUuid(), edge);
    }

    public void update(Edge edge) {
        cache.put(edge.getUuid(), edge);
    }

    public void remove(Edge edge) {
        cache.remove(edge.getUuid());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.EDGES)) {
            while (rs.next()) {
                Edge req = new Edge(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("startNode", java.lang.Long.class),
                    rs.getObject("endNode", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<Edge> get(java.lang.Long key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.lang.Long, Edge> get(Edge.Field column, Object value) {
        return get(new Edge.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.Long, Edge> get(Edge.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.Long, Edge>();
        if (params.length != value.length) return map;
        cache.values().forEach(v -> {
            var include = true;
            for (int i = 0; i < params.length; i++)
                include &= Objects.equals(params[i].getValue(v), value[i]);
            if (include)
                map.put(v.getUuid(), v);
        });
        return map;
    }

    @Override
    public Map<java.lang.Long, Edge> getAll() {
        return cache;
    }

    @Override
    public void save(Edge edge) {
        Object[] values = {edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try {
            dbController.insertQuery(TableType.EDGES, fields, values);
//            add(edge);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(Edge edge, Edge.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.EDGES, "uuid", edge.getUuid(), Arrays.stream(params).map(Edge.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(edge)).toArray());
//            update(edge);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Edge edge) {
        try {
            dbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
//            remove(edge);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.EDGES.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (Edge) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}