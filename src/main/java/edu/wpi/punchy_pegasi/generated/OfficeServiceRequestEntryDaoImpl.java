package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import java.util.Arrays;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class OfficeServiceRequestEntryDaoImpl implements IDao<java.util.UUID, OfficeServiceRequestEntry, OfficeServiceRequestEntry.Field> {

    static String[] fields = {"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "officeRequest", "employeeName"};
    private final PdbController dbController;

    public OfficeServiceRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public OfficeServiceRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<OfficeServiceRequestEntry> get(java.util.UUID key) {
        try (var rs = dbController.searchQuery(TableType.OFFICEREQUESTS, "serviceID", key)) {
            rs.next();
            OfficeServiceRequestEntry req = new OfficeServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("officeRequest"),
                    (java.lang.String)rs.getObject("employeeName"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.util.UUID, OfficeServiceRequestEntry> get(OfficeServiceRequestEntry.Field column, Object value) {
        return get(new OfficeServiceRequestEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, OfficeServiceRequestEntry> get(OfficeServiceRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, OfficeServiceRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.OFFICEREQUESTS, Arrays.stream(params).map(OfficeServiceRequestEntry.Field::getColName).toList().toArray(new String[params.length]), value)) {
            while (rs.next()) {
                OfficeServiceRequestEntry req = new OfficeServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("officeRequest"),
                    (java.lang.String)rs.getObject("employeeName"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public Map<java.util.UUID, OfficeServiceRequestEntry> getAll() {
        var map = new HashMap<java.util.UUID, OfficeServiceRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.OFFICEREQUESTS)) {
            while (rs.next()) {
                OfficeServiceRequestEntry req = new OfficeServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("officeRequest"),
                    (java.lang.String)rs.getObject("employeeName"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(OfficeServiceRequestEntry officeServiceRequestEntry) {
        Object[] values = {officeServiceRequestEntry.getServiceID(), officeServiceRequestEntry.getRoomNumber(), officeServiceRequestEntry.getStaffAssignment(), officeServiceRequestEntry.getAdditionalNotes(), officeServiceRequestEntry.getStatus(), officeServiceRequestEntry.getOfficeRequest(), officeServiceRequestEntry.getEmployeeName()};
        try {
            dbController.insertQuery(TableType.OFFICEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(OfficeServiceRequestEntry officeServiceRequestEntry, OfficeServiceRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.OFFICEREQUESTS, "serviceID", officeServiceRequestEntry.getServiceID(), Arrays.stream(params).map(OfficeServiceRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(officeServiceRequestEntry)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(OfficeServiceRequestEntry officeServiceRequestEntry) {
        try {
            dbController.deleteQuery(TableType.OFFICEREQUESTS, "serviceID", officeServiceRequestEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}