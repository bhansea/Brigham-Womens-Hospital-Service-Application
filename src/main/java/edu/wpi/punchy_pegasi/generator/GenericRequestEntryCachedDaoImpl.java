package edu.wpi.punchy_pegasi.generator;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.GenericRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class GenericRequestEntryCachedDaoImpl implements IDao<String/*idFieldType*/, GenericRequestEntry, GenericRequestEntry.Field>, PropertyChangeListener {

    static String[] fields = {/*fields*/};

    private final ObservableMap<String/*idFieldType*/, GenericRequestEntry> cache = FXCollections.observableMap(new LinkedHashMap<>());
    private final ObservableList<GenericRequestEntry> list = FXCollections.observableArrayList();
    private final PdbController dbController;

    public GenericRequestEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        cache.addListener((MapChangeListener<String/*idFieldType*/, GenericRequestEntry>) c -> {
            if (c.wasRemoved()) {
                list.remove(c.getValueRemoved());
            } else if (c.wasAdded()) {
                list.add(c.getValueAdded());
            }
        });
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public GenericRequestEntryCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(GenericRequestEntry genericRequestEntry) {
        if (!cache.containsKey("genericRequestEntry"/*getID*/))
            cache.put("genericRequestEntry"/*getID*/, genericRequestEntry);
    }

    public void update(GenericRequestEntry genericRequestEntry) {
        cache.put("genericRequestEntry"/*getID*/, genericRequestEntry);
    }

    public void remove(GenericRequestEntry genericRequestEntry) {
        cache.remove("genericRequestEntry"/*getID*/);
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.GENERIC)) {
            while (rs.next()) {
                GenericRequestEntry req/*fromResultSet*/ = null;
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<GenericRequestEntry> get/*FacadeClassName*/(String/*idFieldType*/ key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<String/*idFieldType*/, GenericRequestEntry> get/*FacadeClassName*/(GenericRequestEntry.Field column, Object value) {
        return get(new GenericRequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<String/*idFieldType*/, GenericRequestEntry> get/*FacadeClassName*/(GenericRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<String/*idFieldType*/, GenericRequestEntry>();
        if (params.length != value.length) return map;
        cache.values().forEach(v -> {
            var include = true;
            for (int i = 0; i < params.length; i++)
                include &= Objects.equals(params[i].getValue(v), value[i]);
            if (include)
                map.put("v"/*getID*/, v);
        });
        return map;
    }

    @Override
    public ObservableMap<String/*idFieldType*/, GenericRequestEntry> getAll/*FacadeClassName*/() {
        return cache;
    }

    @Override
    public ObservableList<GenericRequestEntry> getAllAsList/*FacadeClassName*/() {
        return list;
    }

    @Override
    public void save/*FacadeClassName*/(GenericRequestEntry genericRequestEntry) {
        Object[] values = {/*getFields*/};
        try {
            dbController.insertQuery(TableType.GENERIC, fields, values);
//            add(genericRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update/*FacadeClassName*/(GenericRequestEntry genericRequestEntry, GenericRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.GENERIC, ""/*idField*/, "genericRequestEntry"/*getID*/, Arrays.stream(params).map(GenericRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(genericRequestEntry)).toArray());
//            update(genericRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete/*FacadeClassName*/(GenericRequestEntry genericRequestEntry) {
        try {
            dbController.deleteQuery(TableType.GENERIC, ""/*idField*/, "genericRequestEntry"/*getID*/);
//            remove(genericRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.GENERIC.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (GenericRequestEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> remove(data);
                case INSERT -> add(data);
            }
        }
    }
}
