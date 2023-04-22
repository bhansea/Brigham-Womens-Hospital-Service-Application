package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
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
public class RequestEntryCachedDaoImpl implements IDao<java.util.UUID, RequestEntry, RequestEntry.Field>, PropertyChangeListener {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "employeeID"};

    private final ObservableMap<java.util.UUID, RequestEntry> cache = FXCollections.observableMap(new LinkedHashMap<>());
    private final ObservableList<RequestEntry> list = FXCollections.observableArrayList();
    private final PdbController dbController;

    public RequestEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        cache.addListener((MapChangeListener<java.util.UUID, RequestEntry>) c -> {
            if (c.wasRemoved()) {
                list.remove(c.getValueRemoved());
            } else if (c.wasAdded()) {
                list.add(c.getValueAdded());
            }
        });
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public RequestEntryCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(RequestEntry requestEntry) {
        if (!cache.containsKey(requestEntry.getServiceID()))
            cache.put(requestEntry.getServiceID(), requestEntry);
    }

    public void update(RequestEntry requestEntry) {
        cache.put(requestEntry.getServiceID(), requestEntry);
    }

    public void remove(RequestEntry requestEntry) {
        cache.remove(requestEntry.getServiceID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.REQUESTS)) {
            while (rs.next()) {
                RequestEntry req = new RequestEntry(
                    rs.getObject("serviceID", java.util.UUID.class),
                    rs.getObject("locationName", java.lang.Long.class),
                    rs.getObject("staffAssignment", java.lang.Long.class),
                    rs.getObject("additionalNotes", java.lang.String.class),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf(rs.getString("status")),
                    rs.getObject("employeeID", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<RequestEntry> get(java.util.UUID key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.util.UUID, RequestEntry> get(RequestEntry.Field column, Object value) {
        return get(new RequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, RequestEntry> get(RequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, RequestEntry>();
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
    public ObservableMap<java.util.UUID, RequestEntry> getAll() {
        return cache;
    }

    @Override
    public ObservableList<RequestEntry> getAllAsList() {
        return list;
    }

    @Override
    public void save(RequestEntry requestEntry) {
        Object[] values = {requestEntry.getServiceID(), requestEntry.getLocationName(), requestEntry.getStaffAssignment(), requestEntry.getAdditionalNotes(), requestEntry.getStatus(), requestEntry.getEmployeeID()};
        try {
            dbController.insertQuery(TableType.REQUESTS, fields, values);
//            add(requestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(RequestEntry requestEntry, RequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.REQUESTS, "serviceID", requestEntry.getServiceID(), Arrays.stream(params).map(RequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(requestEntry)).toArray());
//            update(requestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(RequestEntry requestEntry) {
        try {
            dbController.deleteQuery(TableType.REQUESTS, "serviceID", requestEntry.getServiceID());
//            remove(requestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.REQUESTS.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (RequestEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> remove(data);
                case INSERT -> add(data);
            }
        }
    }
}