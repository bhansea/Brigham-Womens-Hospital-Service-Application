package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class FurnitureRequestEntryCachedDaoImpl implements IDao<java.util.UUID, FurnitureRequestEntry, FurnitureRequestEntry.Field>, PropertyChangeListener {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "employeeID", "selectFurniture"};

    private final Map<java.util.UUID, FurnitureRequestEntry> cache = new HashMap<>();
    private final PdbController dbController;

    public FurnitureRequestEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public FurnitureRequestEntryCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(FurnitureRequestEntry furnitureRequestEntry) {
        if (!cache.containsKey(furnitureRequestEntry.getServiceID()))
            cache.put(furnitureRequestEntry.getServiceID(), furnitureRequestEntry);
    }

    public void update(FurnitureRequestEntry furnitureRequestEntry) {
        cache.put(furnitureRequestEntry.getServiceID(), furnitureRequestEntry);
    }

    public void remove(FurnitureRequestEntry furnitureRequestEntry) {
        cache.remove(furnitureRequestEntry.getServiceID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.FURNITUREREQUESTS)) {
            while (rs.next()) {
                FurnitureRequestEntry req = new FurnitureRequestEntry(
                    rs.getObject("serviceID", java.util.UUID.class),
                    rs.getObject("locationName", java.lang.Long.class),
                    rs.getObject("staffAssignment", java.lang.Long.class),
                    rs.getObject("additionalNotes", java.lang.String.class),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf(rs.getString("status")),
                    java.util.Arrays.asList((String[])rs.getArray("selectFurniture").getArray()),
                    rs.getObject("employeeID", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<FurnitureRequestEntry> get(java.util.UUID key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.util.UUID, FurnitureRequestEntry> get(FurnitureRequestEntry.Field column, Object value) {
        return get(new FurnitureRequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, FurnitureRequestEntry> get(FurnitureRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, FurnitureRequestEntry>();
        if (params.length != value.length) return map;
        cache.values().forEach(v -> {
            var include = true;
            for (int i = 0; i < params.length; i++)
                include &= Objects.equals(params[i].getValue(v), value[i]);
            if (include)
                map.put(v.getServiceID(), v);
        });
        return map;
    }

    @Override
    public Map<java.util.UUID, FurnitureRequestEntry> getAll() {
        return cache;
    }

    @Override
    public void save(FurnitureRequestEntry furnitureRequestEntry) {
        Object[] values = {furnitureRequestEntry.getServiceID(), furnitureRequestEntry.getLocationName(), furnitureRequestEntry.getStaffAssignment(), furnitureRequestEntry.getAdditionalNotes(), furnitureRequestEntry.getStatus(), furnitureRequestEntry.getEmployeeID(), furnitureRequestEntry.getSelectFurniture()};
        try {
            dbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
//            add(furnitureRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(FurnitureRequestEntry furnitureRequestEntry, FurnitureRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequestEntry.getServiceID(), Arrays.stream(params).map(FurnitureRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(furnitureRequestEntry)).toArray());
//            update(furnitureRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FurnitureRequestEntry furnitureRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequestEntry.getServiceID());
//            remove(furnitureRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.FURNITUREREQUESTS.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (FurnitureRequestEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}