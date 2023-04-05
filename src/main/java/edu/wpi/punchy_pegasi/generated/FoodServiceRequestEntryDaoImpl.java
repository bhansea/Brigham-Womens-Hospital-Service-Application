package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class FoodServiceRequestEntryDaoImpl implements IDao<java.util.UUID, FoodServiceRequestEntry, FoodServiceRequestEntryDaoImpl.Column> {

    static String[] fields = {"foodSelection", "tempType", "additionalItems", "dietaryRestrictions", "patientName", "serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController;

    public FoodServiceRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public FoodServiceRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<FoodServiceRequestEntry> get(java.util.UUID key) {
        try (var rs = dbController.searchQuery(TableType.FOODREQUESTS, "serviceID", key)) {
            rs.next();
            FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("foodSelection"),
                    (java.lang.String)rs.getObject("tempType"),
                    Arrays.asList((String[])rs.getArray("additionalItems").getArray()),
                    (java.lang.String)rs.getObject("dietaryRestrictions"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<FoodServiceRequestEntry> get(Column column, Object value) {
        try (var rs = dbController.searchQuery(TableType.FOODREQUESTS, column.name(), value)) {
            rs.next();
            FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("foodSelection"),
                    (java.lang.String)rs.getObject("tempType"),
                    Arrays.asList((String[])rs.getArray("additionalItems").getArray()),
                    (java.lang.String)rs.getObject("dietaryRestrictions"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.util.UUID, FoodServiceRequestEntry> getAll() {
        var map = new HashMap<java.util.UUID, FoodServiceRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.FOODREQUESTS)) {
            while (rs.next()) {
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("foodSelection"),
                    (java.lang.String)rs.getObject("tempType"),
                    Arrays.asList((String[])rs.getArray("additionalItems").getArray()),
                    (java.lang.String)rs.getObject("dietaryRestrictions"));
                if (req != null)
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FoodServiceRequestEntry foodServiceRequestEntry) {
        Object[] values = {foodServiceRequestEntry.getFoodSelection(), foodServiceRequestEntry.getTempType(), foodServiceRequestEntry.getAdditionalItems(), foodServiceRequestEntry.getDietaryRestrictions(), foodServiceRequestEntry.getPatientName(), foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry.getRoomNumber(), foodServiceRequestEntry.getStaffAssignment(), foodServiceRequestEntry.getAdditionalNotes(), foodServiceRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.FOODREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FoodServiceRequestEntry foodServiceRequestEntry, Column[] params) {
        Object[] values = {foodServiceRequestEntry.getFoodSelection(), foodServiceRequestEntry.getTempType(), foodServiceRequestEntry.getAdditionalItems(), foodServiceRequestEntry.getDietaryRestrictions(), foodServiceRequestEntry.getPatientName(), foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry.getRoomNumber(), foodServiceRequestEntry.getStaffAssignment(), foodServiceRequestEntry.getAdditionalNotes(), foodServiceRequestEntry.getStatus()};
        List<Object> pruned = new ArrayList<>();
        for(var column : params)
            pruned.add(values[Arrays.asList(Column.values()).indexOf(column)]);
        try {
            dbController.updateQuery(TableType.FOODREQUESTS, "serviceID", foodServiceRequestEntry.getServiceID(), (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), pruned.toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FoodServiceRequestEntry foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FOODREQUESTS, "serviceID", foodServiceRequestEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }

    @RequiredArgsConstructor
    public enum Column {
        FOOD_SELECTION("foodSelection"),
        TEMP_TYPE("tempType"),
        ADDITIONAL_ITEMS("additionalItems"),
        DIETARY_RESTRICTIONS("dietaryRestrictions"),
        PATIENT_NAME("patientName"),
        SERVICE_ID("serviceID"),
        ROOM_NUMBER("roomNumber"),
        STAFF_ASSIGNMENT("staffAssignment"),
        ADDITIONAL_NOTES("additionalNotes"),
        STATUS("status");
        @Getter
        private final String colName;
    }
}