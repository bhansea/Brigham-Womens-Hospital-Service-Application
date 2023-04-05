package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ConferenceRoomEntryDaoImpl implements IDao<ConferenceRoomEntry, String> {

    static String[] fields = {"beginningTime", "endTime", "serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<ConferenceRoomEntry> get(String key) {
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS, "serviceID", key)) {
            rs.next();
            ConferenceRoomEntry req = new ConferenceRoomEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("beginningTime"),
                    (java.lang.String)rs.getObject("endTime"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, ConferenceRoomEntry> getAll() {
        var map = new HashMap<String, ConferenceRoomEntry>();
        try (var rs = dbController.searchQuery(TableType.CONFERENCEREQUESTS)) {
            while (rs.next()) {
                ConferenceRoomEntry req = new ConferenceRoomEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("beginningTime"),
                    (java.lang.String)rs.getObject("endTime"));
                if (req != null)
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(ConferenceRoomEntry conferenceRoomEntry) {
        Object[] values = {conferenceRoomEntry.getBeginningTime(), conferenceRoomEntry.getEndTime(), conferenceRoomEntry.getServiceID(), conferenceRoomEntry.getRoomNumber(), conferenceRoomEntry.getStaffAssignment(), conferenceRoomEntry.getAdditionalNotes(), conferenceRoomEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(ConferenceRoomEntry conferenceRoomEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(ConferenceRoomEntry conferenceRoomEntry) {
        try {
            dbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", String.valueOf(conferenceRoomEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}