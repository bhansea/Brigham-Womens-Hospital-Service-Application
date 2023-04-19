package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class EdgeDaoImpl implements IDao<java.lang.Long, Edge, Edge.Field> {

    static String[] fields = {"uuid", "startNode", "endNode"};
    private final PdbController dbController;

    public EdgeDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public EdgeDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Edge> get(java.lang.Long key) {
        try (var rs = dbController.searchQuery(TableType.EDGES, "uuid", key)) {
            rs.next();
            Edge req = new Edge(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("startNode", java.lang.Long.class),
                    rs.getObject("endNode", java.lang.Long.class));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.lang.Long, Edge> get(Edge.Field column, Object value) {
        return get(new Edge.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.Long, Edge> get(Edge.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.Long, Edge>();
        try (var rs = dbController.searchQuery(TableType.EDGES, Arrays.stream(params).map(Edge.Field::getColName).toList().toArray(new String[params.length]), value)) {
            while (rs.next()) {
                Edge req = new Edge(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("startNode", java.lang.Long.class),
                    rs.getObject("endNode", java.lang.Long.class));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public Map<java.lang.Long, Edge> getAll() {
        var map = new HashMap<java.lang.Long, Edge>();
        try (var rs = dbController.searchQuery(TableType.EDGES)) {
            while (rs.next()) {
                Edge req = new Edge(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("startNode", java.lang.Long.class),
                    rs.getObject("endNode", java.lang.Long.class));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Edge edge) {
        Object[] values = {edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try {
            dbController.insertQuery(TableType.EDGES, fields, values);
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
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Edge edge) {
        try {
            dbController.deleteQuery(TableType.EDGES, "uuid", edge.getUuid());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}