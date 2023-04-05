package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.schema.OfficeServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class OfficeServiceRequestEntryDaoImpl implements IDao<OfficeServiceRequestEntry, String> {

    static String[] fields = {"officeRequest", "employeeName", "serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<OfficeServiceRequestEntry> get(String key) {
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
    public Map<String, OfficeServiceRequestEntry> getAll() {
        var map = new HashMap<String, OfficeServiceRequestEntry>();
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
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(OfficeServiceRequestEntry officeServiceRequestEntry) {
        Object[] values = {officeServiceRequestEntry.getOfficeRequest(), officeServiceRequestEntry.getEmployeeName(), officeServiceRequestEntry.getServiceID(), officeServiceRequestEntry.getRoomNumber(), officeServiceRequestEntry.getStaffAssignment(), officeServiceRequestEntry.getAdditionalNotes(), officeServiceRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.OFFICEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(OfficeServiceRequestEntry officeServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(OfficeServiceRequestEntry officeServiceRequestEntry) {
        try {
            dbController.deleteQuery(TableType.OFFICEREQUESTS, "serviceID", String.valueOf(officeServiceRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}