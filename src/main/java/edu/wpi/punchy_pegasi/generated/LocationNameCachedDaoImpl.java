package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class LocationNameCachedDaoImpl implements IDao<java.lang.Long, LocationName, LocationName.Field>, PropertyChangeListener {

    static String[] fields = {"uuid", "longName", "shortName", "nodeType"};

    private final Map<java.lang.Long, LocationName> cache = new HashMap<>();
    private final PdbController dbController;

    public LocationNameCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public LocationNameCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(LocationName locationName) {
        if (!cache.containsKey(locationName.getUuid()))
            cache.put(locationName.getUuid(), locationName);
    }

    public void update(LocationName locationName) {
        cache.put(locationName.getUuid(), locationName);
    }

    public void remove(LocationName locationName) {
        cache.remove(locationName.getUuid());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.LOCATIONNAMES)) {
            while (rs.next()) {
                LocationName req = new LocationName(
                    rs.getObject("uuid", java.lang.Long.class),
                    rs.getObject("longName", java.lang.String.class),
                    rs.getObject("shortName", java.lang.String.class),
                    edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf(rs.getString("nodeType")));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<LocationName> get(java.lang.Long key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.lang.Long, LocationName> get(LocationName.Field column, Object value) {
        return get(new LocationName.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.Long, LocationName> get(LocationName.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.Long, LocationName>();
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
    public Map<java.lang.Long, LocationName> getAll() {
        return cache;
    }

    @Override
    public void save(LocationName locationName) {
        Object[] values = {locationName.getUuid(), locationName.getLongName(), locationName.getShortName(), locationName.getNodeType()};
        try {
            dbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
//            add(locationName);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(LocationName locationName, LocationName.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.LOCATIONNAMES, "uuid", locationName.getUuid(), Arrays.stream(params).map(LocationName.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(locationName)).toArray());
//            update(locationName);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(LocationName locationName) {
        try {
            dbController.deleteQuery(TableType.LOCATIONNAMES, "uuid", locationName.getUuid());
//            remove(locationName);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.LOCATIONNAMES.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (LocationName) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}