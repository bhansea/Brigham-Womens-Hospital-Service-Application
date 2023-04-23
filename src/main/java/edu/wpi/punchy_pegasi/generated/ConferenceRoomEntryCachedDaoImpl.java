package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
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
public class ConferenceRoomEntryCachedDaoImpl implements IDao<java.util.UUID, ConferenceRoomEntry, ConferenceRoomEntry.Field>, PropertyChangeListener {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "employeeID", "beginningTime", "endTime", "date", "amountOfParticipants"};

    private final ObservableMap<java.util.UUID, ConferenceRoomEntry> cache = FXCollections.observableMap(new LinkedHashMap<>());
    private final ObservableList<ConferenceRoomEntry> list = FXCollections.observableArrayList();
    private final PdbController dbController;

    public ConferenceRoomEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        cache.addListener((MapChangeListener<java.util.UUID, ConferenceRoomEntry>) c -> {
            if (c.wasRemoved())
                list.remove(c.getValueRemoved());
            if (c.wasAdded())
                list.add(c.getValueAdded());
        });
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public void add(ConferenceRoomEntry conferenceRoomEntry) {
        if (!cache.containsKey(conferenceRoomEntry.getServiceID()))
            cache.put(conferenceRoomEntry.getServiceID(), conferenceRoomEntry);
    }

    public void update(ConferenceRoomEntry conferenceRoomEntry) {
        cache.put(conferenceRoomEntry.getServiceID(), conferenceRoomEntry);
    }

    public void remove(ConferenceRoomEntry conferenceRoomEntry) {
        cache.remove(conferenceRoomEntry.getServiceID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS)) {
            while (rs.next()) {
                ConferenceRoomEntry req = new ConferenceRoomEntry(
                        rs.getObject("serviceID", java.util.UUID.class),
                        rs.getObject("locationName", java.lang.Long.class),
                        rs.getObject("staffAssignment", java.lang.Long.class),
                        rs.getObject("additionalNotes", java.lang.String.class),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf(rs.getString("status")),
                        rs.getObject("beginningTime", java.lang.String.class),
                        rs.getObject("endTime", java.lang.String.class),
                        rs.getObject("date", java.time.LocalDate.class),
                        rs.getObject("amountOfParticipants", java.lang.String.class),
                        rs.getObject("employeeID", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<ConferenceRoomEntry> get(java.util.UUID key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.util.UUID, ConferenceRoomEntry> get(ConferenceRoomEntry.Field column, Object value) {
        return get(new ConferenceRoomEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, ConferenceRoomEntry> get(ConferenceRoomEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, ConferenceRoomEntry>();
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
    public ObservableMap<java.util.UUID, ConferenceRoomEntry> getAll() {
        return cache;
    }

    @Override
    public ObservableList<ConferenceRoomEntry> getAllAsList() {
        return list;
    }

    @Override
    public void save(ConferenceRoomEntry conferenceRoomEntry) {
        Object[] values = {conferenceRoomEntry.getServiceID(), conferenceRoomEntry.getLocationName(), conferenceRoomEntry.getStaffAssignment(), conferenceRoomEntry.getAdditionalNotes(), conferenceRoomEntry.getStatus(), conferenceRoomEntry.getEmployeeID(), conferenceRoomEntry.getBeginningTime(), conferenceRoomEntry.getEndTime(), conferenceRoomEntry.getDate(), conferenceRoomEntry.getAmountOfParticipants()};
        try {
            dbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
//            add(conferenceRoomEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(ConferenceRoomEntry conferenceRoomEntry, ConferenceRoomEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.CONFERENCEREQUESTS, "serviceID", conferenceRoomEntry.getServiceID(), Arrays.stream(params).map(ConferenceRoomEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(conferenceRoomEntry)).toArray());
//            update(conferenceRoomEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(ConferenceRoomEntry conferenceRoomEntry) {
        try {
            dbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", conferenceRoomEntry.getServiceID());
//            remove(conferenceRoomEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.CONFERENCEREQUESTS.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (ConferenceRoomEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> remove(data);
                case INSERT -> add(data);
            }
        }
    }
}