package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ConferenceRoomEntryDaoImpl implements IDao<java.util.UUID, ConferenceRoomEntry, ConferenceRoomEntry.Field> {

    static String[] fields = {"serviceID", "locationName", "staffAssignment", "additionalNotes", "status", "beginningTime", "endTime", "date"};
    private final PdbController dbController;

    public ConferenceRoomEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public ConferenceRoomEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<ConferenceRoomEntry> get(java.util.UUID key) {
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS, "serviceID", key)) {
            rs.next();
            ConferenceRoomEntry req = new ConferenceRoomEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.Long)rs.getObject("locationName"),
                    (java.lang.Long)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("beginningTime"),
                    (java.lang.String)rs.getObject("endTime"),
                    (java.time.LocalDate)rs.getObject("date"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.util.UUID, ConferenceRoomEntry> get(ConferenceRoomEntry.Field column, Object value) {
        return get(new ConferenceRoomEntry.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.util.UUID, ConferenceRoomEntry> get(ConferenceRoomEntry.Field[] params, Object[] value) {
        var map = new HashMap<java.util.UUID, ConferenceRoomEntry>();
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS, Arrays.stream(params).map(ConferenceRoomEntry.Field::getColName).toList().toArray(new String[params.length]), value)) {
            while (rs.next()) {
                ConferenceRoomEntry req = new ConferenceRoomEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.Long)rs.getObject("locationName"),
                    (java.lang.Long)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("beginningTime"),
                    (java.lang.String)rs.getObject("endTime"),
                    (java.time.LocalDate)rs.getObject("date"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public Map<java.util.UUID, ConferenceRoomEntry> getAll() {
        var map = new HashMap<java.util.UUID, ConferenceRoomEntry>();
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS)) {
            while (rs.next()) {
                ConferenceRoomEntry req = new ConferenceRoomEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.Long)rs.getObject("locationName"),
                    (java.lang.Long)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("beginningTime"),
                    (java.lang.String)rs.getObject("endTime"),
                    (java.time.LocalDate)rs.getObject("date"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(ConferenceRoomEntry conferenceRoomEntry) {
        Object[] values = {conferenceRoomEntry.getServiceID(), conferenceRoomEntry.getLocationName(), conferenceRoomEntry.getStaffAssignment(), conferenceRoomEntry.getAdditionalNotes(), conferenceRoomEntry.getStatus(), conferenceRoomEntry.getBeginningTime(), conferenceRoomEntry.getEndTime(), conferenceRoomEntry.getDate()};
        try {
            dbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
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
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(ConferenceRoomEntry conferenceRoomEntry) {
        try {
            dbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", conferenceRoomEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}