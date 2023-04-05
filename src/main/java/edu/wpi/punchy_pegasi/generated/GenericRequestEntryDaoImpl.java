package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.GenericRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<GenericRequestEntry, String> {

    static String[] fields = {"serviceID", "patientName", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController;

    public GenericRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public GenericRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<GenericRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(TableType.GENERIC, "serviceID", key)) {
            rs.next();
            GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, GenericRequestEntry> getAll() {
        var map = new HashMap<String, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.GENERIC)) {
            while (rs.next()) {
                GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")));
                if (req != null)
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(GenericRequestEntry genericRequestEntry) {
        Object[] values = {genericRequestEntry.getServiceID(), genericRequestEntry.getPatientName(), genericRequestEntry.getRoomNumber(), genericRequestEntry.getStaffAssignment(), genericRequestEntry.getAdditionalNotes(), genericRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.GENERIC, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(GenericRequestEntry genericRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(GenericRequestEntry genericRequestEntry) {
        try {
            dbController.deleteQuery(TableType.GENERIC, "serviceID", String.valueOf(genericRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}