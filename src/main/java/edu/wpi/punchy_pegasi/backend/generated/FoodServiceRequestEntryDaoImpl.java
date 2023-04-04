package edu.wpi.punchy_pegasi.backend.generated;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import java.util.Arrays;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FoodServiceRequestEntryDaoImpl implements IDao<FoodServiceRequestEntry, String> {

    static String[] fields = {"foodSelection", "tempType", "additionalItems", "dietaryRestrictions", "serviceID", "patientName", "roomNumber", "staffAssignment", "additionalNotes", "status"};

    // TODO: change this to App.getSingleton().getPdb() when not testing
    private final PdbController dbController = TestDB.getSingleton().getPdb();

    @Override
    public Optional<FoodServiceRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.FOODREQUESTS, "serviceID", key)) {
            rs.next();
            FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.frontend.RequestEntry.Status.valueOf((String)rs.getObject("status")),
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
    public Map<String, FoodServiceRequestEntry> getAll() {
        var map = new HashMap<String, FoodServiceRequestEntry>();
        try (var rs = dbController.searchQuery(PdbController.TableType.FOODREQUESTS)) {
            while (rs.next()) {
                FoodServiceRequestEntry req = new FoodServiceRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.frontend.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("foodSelection"),
                    (java.lang.String)rs.getObject("tempType"),
                    Arrays.asList((String[])rs.getArray("additionalItems").getArray()),
                    (java.lang.String)rs.getObject("dietaryRestrictions"));
                if (req != null)
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FoodServiceRequestEntry foodServiceRequestEntry) {
        Object[] values = {foodServiceRequestEntry.getFoodSelection(), foodServiceRequestEntry.getTempType(), foodServiceRequestEntry.getAdditionalItems(), foodServiceRequestEntry.getDietaryRestrictions(), foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry.getPatientName(), foodServiceRequestEntry.getRoomNumber(), foodServiceRequestEntry.getStaffAssignment(), foodServiceRequestEntry.getAdditionalNotes(), foodServiceRequestEntry.getStatus()};
        try {
            dbController.insertQuery(PdbController.TableType.FOODREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FoodServiceRequestEntry foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
        // TODO: use updateQuery() in PdbController
        
    }

    @Override
    public void delete(FoodServiceRequestEntry foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.FOODREQUESTS, "serviceID", String.valueOf(foodServiceRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}