package edu.wpi.punchy_pegasi.backend.generated;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.backend.TestDB;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.GenericRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<GenericRequestEntry, String> {

    static String[] fields = {"serviceID", "patientName", "roomNumber", "additionalNotes"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<GenericRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.GENERIC, "serviceID", key)) {
            rs.next();
            GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("additionalNotes"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, GenericRequestEntry> getAll() {
        var map = new HashMap<String, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(PdbController.TableType.GENERIC)) {
            while (rs.next()) {
                GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("additionalNotes"));
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
        Object[] values = {genericRequestEntry.getServiceID(), genericRequestEntry.getPatientName(), genericRequestEntry.getRoomNumber(), genericRequestEntry.getAdditionalNotes()};
        try {
            dbController.insertQuery(PdbController.TableType.GENERIC, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(GenericRequestEntry foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(GenericRequestEntry foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.GENERIC, "serviceID", String.valueOf(foodServiceRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}