package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MoveDaoImpl implements IDao<java.lang.Long, Move, MoveDaoImpl.Column> {

    static String[] fields = {"uuid", "nodeID", "longName", "date"};
    private final PdbController dbController;

    public MoveDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public MoveDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Move> get(java.lang.Long key) {
        try (var rs = dbController.searchQuery(TableType.MOVES, "uuid", key)) {
            rs.next();
            Move req = new Move(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("date"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Move> get(Column column, Object value) {
        try (var rs = dbController.searchQuery(TableType.MOVES, column.name(), value)) {
            rs.next();
            Move req = new Move(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("date"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.lang.Long, Move> getAll() {
        var map = new HashMap<java.lang.Long, Move>();
        try (var rs = dbController.searchQuery(TableType.MOVES)) {
            while (rs.next()) {
                Move req = new Move(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.Long)rs.getObject("nodeID"),
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("date"));
                if (req != null)
                    map.put(req.getUuid(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Move move) {
        Object[] values = {move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            dbController.insertQuery(TableType.MOVES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Move move, Column[] params) {
        Object[] values = {move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        List<Object> pruned = new ArrayList<>();
        for(var column : params)
            pruned.add(values[Arrays.asList(Column.values()).indexOf(column)]);
        try {
            dbController.updateQuery(TableType.MOVES, "uuid", move.getUuid(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), pruned.toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Move move) {
        try {
            dbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @RequiredArgsConstructor
    public enum Column {
        UUID("uuid"),
        NODE_ID("nodeID"),
        LONG_NAME("longName"),
        DATE("date");
        @Getter
        private final String colName;
    }
}