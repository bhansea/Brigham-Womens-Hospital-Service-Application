package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.GenericRequestEntry;
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
public class GenericRequestEntryDaoImpl implements IDao<java.util.UUID, GenericRequestEntry, GenericRequestEntry.Field> {

    static String[] fields = {"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController;

    public GenericRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public GenericRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<GenericRequestEntry> get(java.util.UUID key) {
        try (var rs = dbController.searchQuery(TableType.GENERIC, "serviceID", key)) {
            rs.next();
            GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
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
    public Optional<GenericRequestEntry> get(GenericRequestEntry.Field column, Object value) {
        try (var rs = dbController.searchQuery(TableType.GENERIC, column.name(), value)) {
            rs.next();
            GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
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
    public Map<java.util.UUID, GenericRequestEntry> getAll() {
        var map = new HashMap<java.util.UUID, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.GENERIC)) {
            while (rs.next()) {
                GenericRequestEntry req = new GenericRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(GenericRequestEntry genericRequestEntry) {
        Object[] values = {genericRequestEntry.getServiceID(), genericRequestEntry.getRoomNumber(), genericRequestEntry.getStaffAssignment(), genericRequestEntry.getAdditionalNotes(), genericRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.GENERIC, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(GenericRequestEntry genericRequestEntry, GenericRequestEntry.Field[] params) {
        try {
            dbController.updateQuery(TableType.GENERIC, "serviceID", genericRequestEntry.getServiceID(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), Arrays.stream(params).map(p->p.getValue(genericRequestEntry)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(GenericRequestEntry genericRequestEntry) {
        try {
            dbController.deleteQuery(TableType.GENERIC, "serviceID", genericRequestEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}