package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Move;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MoveCachedDaoImpl implements IDao<java.lang.Long, Move, Move.Field>, PropertyChangeListener {

    static String[] fields = {"uuid", "nodeID", "longName", "date"};

    private final Map<java.lang.Long, Move> cache = new HashMap<>();
    private final PdbController dbController;

    public MoveCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public MoveCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(Move move) {
        if (!cache.containsKey(move.getUuid()))
            cache.put(move.getUuid(), move);
    }

    public void update(Move move) {
        cache.put(move.getUuid(), move);
    }

    public void remove(Move move) {
        cache.remove(move.getUuid());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.MOVES)) {
            while (rs.next()) {
                Move req = new Move(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("nodeID", java.lang.Long.class),
                    rs.getObject("longName", java.lang.String.class),
                    rs.getObject("date", java.time.LocalDate.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<Move> get(java.lang.Long key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.lang.Long, Move> get(Move.Field column, Object value) {
        return get(new Move.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.Long, Move> get(Move.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.Long, Move>();
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
    public Map<java.lang.Long, Move> getAll() {
        return cache;
    }

    @Override
    public void save(Move move) {
        Object[] values = {move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            dbController.insertQuery(TableType.MOVES, fields, values);
//            add(move);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(Move move, Move.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.MOVES, "uuid", move.getUuid(), Arrays.stream(params).map(Move.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(move)).toArray());
//            update(move);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Move move) {
        try {
            dbController.deleteQuery(TableType.MOVES, "uuid", move.getUuid());
//            remove(move);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.MOVES.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (Move) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}