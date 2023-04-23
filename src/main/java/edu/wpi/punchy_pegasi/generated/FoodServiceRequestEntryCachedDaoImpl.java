package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
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
public class FoodServiceRequestEntryCachedDaoImpl implements IDao<java.util.UUID, FoodServiceRequestEntry, FoodServiceRequestEntry.Field>, PropertyChangeListener {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "employeeID", "foodSelection", "tempType", "additionalItems", "dietaryRestrictions", "patientName", "beverage"};

    private final ObservableMap<java.util.UUID, FoodServiceRequestEntry> cache = FXCollections.observableMap(new LinkedHashMap<>());
    private final ObservableList<FoodServiceRequestEntry> list = FXCollections.observableArrayList();
    private final PdbController dbController;

    public FoodServiceRequestEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        cache.addListener((MapChangeListener<java.util.UUID, FoodServiceRequestEntry>) c -> {
            if (c.wasRemoved()) {
                list.remove(c.getValueRemoved());
            } else if (c.wasAdded()) {
                list.add(c.getValueAdded());
            }
        });
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public void add(FoodServiceRequestEntry foodServiceRequestEntry) {
        if (!cache.containsKey(foodServiceRequestEntry.getServiceID()))
            cache.put(foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry);
    }

    public void update(FoodServiceRequestEntry foodServiceRequestEntry) {
        cache.put(foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry);
    }

    public void remove(FoodServiceRequestEntry foodServiceRequestEntry) {
        cache.remove(foodServiceRequestEntry.getServiceID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.FOODREQUESTS)) {
            while (rs.next()) {
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                        rs.getObject("serviceID", java.util.UUID.class),
                        rs.getObject("locationName", java.lang.Long.class),
                        rs.getObject("staffAssignment", java.lang.Long.class),
                        rs.getObject("additionalNotes", java.lang.String.class),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf(rs.getString("status")),
                        rs.getObject("foodSelection", java.lang.String.class),
                        rs.getObject("tempType", java.lang.String.class),
                        java.util.Arrays.asList((String[]) rs.getArray("additionalItems").getArray()),
                        rs.getObject("beverage", java.lang.String.class),
                        rs.getObject("dietaryRestrictions", java.lang.String.class),
                        rs.getObject("patientName", java.lang.String.class),
                        rs.getObject("employeeID", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<FoodServiceRequestEntry> get(java.util.UUID key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.util.UUID, FoodServiceRequestEntry> get(FoodServiceRequestEntry.Field column, Object value) {
        return get(new FoodServiceRequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, FoodServiceRequestEntry> get(FoodServiceRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, FoodServiceRequestEntry>();
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
    public ObservableMap<java.util.UUID, FoodServiceRequestEntry> getAll() {
        return cache;
    }

    @Override
    public ObservableList<FoodServiceRequestEntry> getAllAsList() {
        return list;
    }

    @Override
    public void save(FoodServiceRequestEntry foodServiceRequestEntry) {
        Object[] values = {foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry.getLocationName(), foodServiceRequestEntry.getStaffAssignment(), foodServiceRequestEntry.getAdditionalNotes(), foodServiceRequestEntry.getStatus(), foodServiceRequestEntry.getEmployeeID(), foodServiceRequestEntry.getFoodSelection(), foodServiceRequestEntry.getTempType(), foodServiceRequestEntry.getAdditionalItems(), foodServiceRequestEntry.getDietaryRestrictions(), foodServiceRequestEntry.getPatientName(), foodServiceRequestEntry.getBeverage()};
        try {
            dbController.insertQuery(TableType.FOODREQUESTS, fields, values);
//            add(foodServiceRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(FoodServiceRequestEntry foodServiceRequestEntry, FoodServiceRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.FOODREQUESTS, "serviceID", foodServiceRequestEntry.getServiceID(), Arrays.stream(params).map(FoodServiceRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(foodServiceRequestEntry)).toArray());
//            update(foodServiceRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FoodServiceRequestEntry foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", foodServiceRequestEntry.getServiceID());
//            remove(foodServiceRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.FOODREQUESTS.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (FoodServiceRequestEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> remove(data);
                case INSERT -> add(data);
            }
        }
    }
}