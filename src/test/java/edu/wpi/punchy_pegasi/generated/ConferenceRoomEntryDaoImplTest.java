package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.DefaultTest;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.ConferenceRoomEntry;
import edu.wpi.punchy_pegasi.schema.RequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@Slf4j
class ConferenceRoomEntryDaoImplTest {
    static PdbController pdbController;
    static String[] fields;

    static ConferenceRoomEntryDaoImpl dao;


    @BeforeAll
    static void init() throws SQLException, ClassNotFoundException {
        fields = new String[]{"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "beginningTime", "endTime"};
        pdbController = new PdbController(Config.source);
        dao = new ConferenceRoomEntryDaoImpl(pdbController);
        try {
            pdbController.initTableByType(TableType.CONFERENCEREQUESTS);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
        ConferenceRoomEntry room = new ConferenceRoomEntry(UUID.randomUUID(), "testLocation", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd", LocalDate.now());
        Object[] values = new Object[]{room.getServiceID(), room.getLocationName(), room.getStaffAssignment(), room.getAdditionalNotes(), room.getStatus(), room.getBeginningTime(), room.getEndTime()};
        try{
            pdbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
        Optional<ConferenceRoomEntry> results = dao.get(room.getServiceID());
        ConferenceRoomEntry daoresult = results.get();
        assertEquals(daoresult, room);
        try{
            pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", room.getServiceID());
        } catch(PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGet() {
        var room = new ConferenceRoomEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd", LocalDate.now());
        var room2 = new ConferenceRoomEntry(UUID.randomUUID(), "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd", LocalDate.now());
        var values = new Object[]{room.getServiceID(), room.getLocationName(), room.getStaffAssignment(), room.getAdditionalNotes(), room.getStatus(), room.getBeginningTime(), room.getEndTime()};
        var values2 = new Object[]{room2.getServiceID(), room2.getLocationName(), room2.getStaffAssignment(), room2.getAdditionalNotes(), room2.getStatus(), room2.getBeginningTime(), room2.getEndTime()};
        try{
            pdbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
            pdbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values2);
        } catch (PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
        var results = dao.get(ConferenceRoomEntry.Field.LOCATION_NAME, "testRoom");
        var map = new HashMap<java.util.UUID, ConferenceRoomEntry>();
        try (var rs = pdbController.searchQuery(TableType.CONFERENCEREQUESTS, ConferenceRoomEntry.Field.LOCATION_NAME.getColName(), "testRoom")) {
            while (rs.next()) {
                ConferenceRoomEntry req = new ConferenceRoomEntry(
                        (java.util.UUID)rs.getObject("serviceID"),
                        (java.lang.String)rs.getObject("roomNumber"),
                        (java.lang.String)rs.getObject("staffAssignment"),
                        (java.lang.String)rs.getObject("additionalNotes"),
                        edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                        (java.lang.String)rs.getObject("beginningTime"),
                        (java.lang.String)rs.getObject("endTime"),
                        LocalDate.now());
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        assertEquals(map.get(room.getServiceID()), results.get(room.getServiceID()));
        assertEquals(map.get(room2.getServiceID()), results.get(room2.getServiceID()));
        try{
            pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", room.getServiceID());
            pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", room2.getServiceID());
        } catch(PdbController.DatabaseException e){
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAll() {
        var values0 = new Object[] {
                UUID.randomUUID(),
                "testRoomNum0",
                "testStaff0",
                "testNotes0",
                RequestEntry.Status.PROCESSING,
                "beginTime0",
                "endTime0",
                LocalDate.now()
        };
        var values1 = new Object[] {
                UUID.randomUUID(),
                "testRoomNum1",
                "testStaff1",
                "testNotes1",
                RequestEntry.Status.PROCESSING,
                "beginTime1",
                "endTime1",
                LocalDate.now()
        };
        var values2 = new Object[] {
                UUID.randomUUID(),
                "testRoomNum2",
                "testStaff2",
                "testNotes2",
                RequestEntry.Status.PROCESSING,
                "beginTime2",
                "endTime2",
                LocalDate.now()
        };

        var valuesSet = new Object[][] {values0, values1, values2};
        var refMap = new HashMap<UUID, ConferenceRoomEntry>();

        for(var values : valuesSet) {
            try {
                pdbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
            } catch (PdbController.DatabaseException e) {
                assert false : e.getMessage();
            }
            var uuid = (UUID) values[0];
            var locName = (String) values[1];
            var staff = (String) values[2];
            var notes = (String) values[3];
            var status = (RequestEntry.Status) values[4];
            var beginTime = (String) values[5];
            var endTime = (String) values[6];
            var date = (LocalDate) values[7];
            refMap.put(uuid, new ConferenceRoomEntry(uuid, locName, staff, notes, status, beginTime, endTime, date));
        }

        Map<UUID, ConferenceRoomEntry> resultMap = dao.getAll();
        for (var entry : resultMap.entrySet()) {
            try {
                pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", entry.getKey());
            } catch (PdbController.DatabaseException e) {
                assert false : "Failed to delete from database";
            }
        }
        assertEquals(refMap, resultMap);

    }

    @Test
    void save() {
        var dao = new ConferenceRoomEntryDaoImpl(pdbController);
        UUID uuid = UUID.randomUUID();
        ConferenceRoomEntry conference = new ConferenceRoomEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd");
        dao.save(conference);
        Optional<ConferenceRoomEntry> results = dao.get(uuid);
        ConferenceRoomEntry daoresult = results.get();
        assertEquals(conference, daoresult);
        try {
            pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void update() {
        UUID uuid = UUID.randomUUID();
        ConferenceRoomEntry conference = new ConferenceRoomEntry(uuid, "testRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd");
        dao.save(conference);

        ConferenceRoomEntry updatedConference = new ConferenceRoomEntry(uuid, "updatedTestRoom", "testStaff", "testNotes", RequestEntry.Status.PROCESSING, "testBeginning", "testEnd");
        ConferenceRoomEntry.Field[] fields = {ConferenceRoomEntry.Field.ROOM_NUMBER};
        dao.update(updatedConference, fields);

        Optional<ConferenceRoomEntry> results = dao.get(uuid);
        ConferenceRoomEntry daoresult = results.get();
        assertEquals(updatedConference, daoresult);
        try {
            pdbController.deleteQuery(TableType.CONFERENCEREQUESTS, "serviceID", uuid);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void delete() {
        ConferenceRoomEntry conferenceRoom =
                new ConferenceRoomEntry(
                        UUID.randomUUID(),
                        "testRoomNum",
                        "testStaff",
                        "testNotes",
                        RequestEntry.Status.PROCESSING,
                        "beginTime",
                        "endTime");

        var values = new Object[] {
                conferenceRoom.getServiceID(),
                conferenceRoom.getLocationName(),
                conferenceRoom.getStaffAssignment(),
                conferenceRoom.getAdditionalNotes(),
                conferenceRoom.getStatus(),
                conferenceRoom.getBeginningTime(),
                conferenceRoom.getEndTime()
        };

        try {
            pdbController.insertQuery(TableType.CONFERENCEREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to insert into database";
            log.error("Failed to insert into database", e);
        }

        try {
            ResultSet rs = pdbController.searchQuery(TableType.CONFERENCEREQUESTS, "serviceID", conferenceRoom.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert false: "Failed to find the entry in the database";
            log.error("Failed to find the entry in the database", e);
        }

        dao.delete(conferenceRoom);

        try {
            ResultSet rs = pdbController.searchQuery(TableType.CONFERENCEREQUESTS, "serviceID", conferenceRoom.getServiceID());
        } catch (PdbController.DatabaseException e) {
            assert true: "Successfully deleted the entry from the database";
            assertTrue(e.getMessage().contains("SQL error"));
        }
    }
}