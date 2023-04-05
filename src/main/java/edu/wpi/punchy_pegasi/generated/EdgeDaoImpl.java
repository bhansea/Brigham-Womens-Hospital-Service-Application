package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Edge;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class EdgeDaoImpl implements IDao<java.lang.Long, Edge, EdgeDaoImpl.Column> {

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
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.String)rs.getObject("startNode"),
                    (java.lang.String)rs.getObject("endNode"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Edge> get(Column column, Object value) {
        try (var rs = dbController.searchQuery(TableType.EDGES, column.name(), value)) {
            rs.next();
            Edge req = new Edge(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.String)rs.getObject("startNode"),
                    (java.lang.String)rs.getObject("endNode"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.lang.Long, Edge> getAll() {
        var map = new HashMap<java.lang.Long, Edge>();
        try (var rs = dbController.searchQuery(TableType.EDGES)) {
            while (rs.next()) {
                Edge req = new Edge(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.String)rs.getObject("startNode"),
                    (java.lang.String)rs.getObject("endNode"));
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
    public void update(Edge edge, Column[] params) {
        Object[] values = {edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        List<Object> pruned = new ArrayList<>();
        for(var column : params)
            pruned.add(values[Arrays.asList(Column.values()).indexOf(column)]);
        try {
            dbController.updateQuery(TableType.EDGES, "uuid", edge.getUuid(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), pruned.toArray());
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

    @RequiredArgsConstructor
    public enum Column {
        UUID("uuid"),
        START_NODE("startNode"),
        END_NODE("endNode");
        @Getter
        private final String colName;
    }
}