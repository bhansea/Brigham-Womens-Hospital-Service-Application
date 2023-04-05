package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import java.util.Arrays;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class FlowerDeliveryRequestEntryDaoImpl implements IDao<java.util.UUID, FlowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field> {

    static String[] fields = {"flowerSize", "flowerType", "flowerAmount", "patientName", "serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController;

    public FlowerDeliveryRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public FlowerDeliveryRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(java.util.UUID key) {
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS, "serviceID", key)) {
            rs.next();
            FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("flowerSize"),
                    (java.lang.String)rs.getObject("flowerAmount"),
                    (java.lang.String)rs.getObject("flowerType"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(FlowerDeliveryRequestEntry.Field column, Object value) {
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS, column.name(), value)) {
            rs.next();
            FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("flowerSize"),
                    (java.lang.String)rs.getObject("flowerAmount"),
                    (java.lang.String)rs.getObject("flowerType"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.util.UUID, FlowerDeliveryRequestEntry> getAll() {
        var map = new HashMap<java.util.UUID, FlowerDeliveryRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS)) {
            while (rs.next()) {
                FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("flowerSize"),
                    (java.lang.String)rs.getObject("flowerAmount"),
                    (java.lang.String)rs.getObject("flowerType"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        Object[] values = {flowerDeliveryRequestEntry.getFlowerSize(), flowerDeliveryRequestEntry.getFlowerType(), flowerDeliveryRequestEntry.getFlowerAmount(), flowerDeliveryRequestEntry.getPatientName(), flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry.getRoomNumber(), flowerDeliveryRequestEntry.getStaffAssignment(), flowerDeliveryRequestEntry.getAdditionalNotes(), flowerDeliveryRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field[] params) {
        try {
            dbController.updateQuery(TableType.FLOWERREQUESTS, "serviceID", flowerDeliveryRequestEntry.getServiceID(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), Arrays.stream(params).map(p->p.getValue(flowerDeliveryRequestEntry)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", flowerDeliveryRequestEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}