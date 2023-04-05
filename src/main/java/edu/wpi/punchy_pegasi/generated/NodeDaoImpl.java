package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Node;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class NodeDaoImpl implements IDao<java.lang.Long, Node, NodeDaoImpl.Column> {

    static String[] fields = {"nodeID", "xcoord", "ycoord", "floor", "building"};
    private final PdbController dbController;

    public NodeDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public NodeDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Node> get(java.lang.Long key) {
        try (var rs = dbController.searchQuery(TableType.NODES, "nodeID", key)) {
            rs.next();
            Node req = new Node(
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.Integer)rs.getObject("xcoord"),
                    (java.lang.Integer)rs.getObject("ycoord"),
                    (java.lang.String)rs.getObject("floor"),
                    (java.lang.String)rs.getObject("building"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Node> get(Column column, Object value) {
        try (var rs = dbController.searchQuery(TableType.NODES, column.name(), value)) {
            rs.next();
            Node req = new Node(
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.Integer)rs.getObject("xcoord"),
                    (java.lang.Integer)rs.getObject("ycoord"),
                    (java.lang.String)rs.getObject("floor"),
                    (java.lang.String)rs.getObject("building"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.lang.Long, Node> getAll() {
        var map = new HashMap<java.lang.Long, Node>();
        try (var rs = dbController.searchQuery(TableType.NODES)) {
            while (rs.next()) {
                Node req = new Node(
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.Integer)rs.getObject("xcoord"),
                    (java.lang.Integer)rs.getObject("ycoord"),
                    (java.lang.String)rs.getObject("floor"),
                    (java.lang.String)rs.getObject("building"));
                if (req != null)
                    map.put(req.getNodeID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Node node) {
        Object[] values = {node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            dbController.insertQuery(TableType.NODES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Node node, Column[] params) {
        Object[] values = {node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        List<Object> pruned = new ArrayList<>();
        for(var column : params)
            pruned.add(values[Arrays.asList(Column.values()).indexOf(column)]);
        try {
            dbController.updateQuery(TableType.NODES, "nodeID", node.getNodeID(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), pruned.toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Node node) {
        try {
            dbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @RequiredArgsConstructor
    public enum Column {
        NODE_ID("nodeID"),
        XCOORD("xcoord"),
        YCOORD("ycoord"),
        FLOOR("floor"),
        BUILDING("building");
        @Getter
        private final String colName;
    }
}