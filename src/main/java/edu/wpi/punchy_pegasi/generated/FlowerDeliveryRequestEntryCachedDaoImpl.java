package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class FlowerDeliveryRequestEntryCachedDaoImpl implements IDao<java.util.UUID, FlowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field>, PropertyChangeListener {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "employeeID", "flowerSize", "flowerType", "flowerAmount", "patientName"};

    private final Map<java.util.UUID, FlowerDeliveryRequestEntry> cache = new HashMap<>();
    private final PdbController dbController;

    public FlowerDeliveryRequestEntryCachedDaoImpl(PdbController dbController) {
        this.dbController = dbController;
        initCache();
        this.dbController.addPropertyChangeListener(this);
    }

    public FlowerDeliveryRequestEntryCachedDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    public void add(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        if (!cache.containsKey(flowerDeliveryRequestEntry.getServiceID()))
            cache.put(flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry);
    }

    public void update(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        cache.put(flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry);
    }

    public void remove(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        cache.remove(flowerDeliveryRequestEntry.getServiceID());
    }

    private void initCache() {
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS)) {
            while (rs.next()) {
                FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    rs.getObject("serviceID", java.util.UUID.class),
                    rs.getObject("patientName", java.lang.String.class),
                    rs.getObject("locationName", java.lang.Long.class),
                    rs.getObject("staffAssignment", java.lang.Long.class),
                    rs.getObject("additionalNotes", java.lang.String.class),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf(rs.getString("status")),
                    rs.getObject("flowerSize", java.lang.String.class),
                    rs.getObject("flowerAmount", java.lang.String.class),
                    rs.getObject("flowerType", java.lang.String.class),
                    rs.getObject("employeeID", java.lang.Long.class));
                add(req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
    }

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(java.util.UUID key) {
        return Optional.ofNullable(cache.get(key));
    }

    @Override
    public Map<java.util.UUID, FlowerDeliveryRequestEntry> get(FlowerDeliveryRequestEntry.Field column, Object value) {
        return get(new FlowerDeliveryRequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, FlowerDeliveryRequestEntry> get(FlowerDeliveryRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, FlowerDeliveryRequestEntry>();
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
    public Map<java.util.UUID, FlowerDeliveryRequestEntry> getAll() {
        return cache;
    }

    @Override
    public void save(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        Object[] values = {flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry.getLocationName(), flowerDeliveryRequestEntry.getStaffAssignment(), flowerDeliveryRequestEntry.getAdditionalNotes(), flowerDeliveryRequestEntry.getStatus(), flowerDeliveryRequestEntry.getEmployeeID(), flowerDeliveryRequestEntry.getFlowerSize(), flowerDeliveryRequestEntry.getFlowerType(), flowerDeliveryRequestEntry.getFlowerAmount(), flowerDeliveryRequestEntry.getPatientName()};
        try {
            dbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
//            add(flowerDeliveryRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void update(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.FLOWERREQUESTS, "serviceID", flowerDeliveryRequestEntry.getServiceID(), Arrays.stream(params).map(FlowerDeliveryRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(flowerDeliveryRequestEntry)).toArray());
//            update(flowerDeliveryRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", flowerDeliveryRequestEntry.getServiceID());
//            remove(flowerDeliveryRequestEntry);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), TableType.FLOWERREQUESTS.name() + "_update")) {
            var update = (PdbController.DatabaseChangeEvent) evt.getNewValue();
            var data = (FlowerDeliveryRequestEntry) update.data();
            switch (update.action()) {
                case UPDATE -> update(data);
                case DELETE -> delete(data);
                case INSERT -> add(data);
            }
        }
    }
}