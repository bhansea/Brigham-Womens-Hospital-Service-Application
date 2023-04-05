package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FurnitureRequestEntryDaoImpl implements IDao<FurnitureRequestEntry, String> {

    static String[] fields = {"selectFurniture", "serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<FurnitureRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(TableType.FURNITUREREQUESTS, "serviceID", key)) {
            rs.next();
            FurnitureRequestEntry req = new FurnitureRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    Arrays.asList((String[])rs.getArray("selectFurniture").getArray()));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, FurnitureRequestEntry> getAll() {
        var map = new HashMap<String, FurnitureRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.FURNITUREREQUESTS)) {
            while (rs.next()) {
                FurnitureRequestEntry req = new FurnitureRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    Arrays.asList((String[])rs.getArray("selectFurniture").getArray()));
                if (req != null)
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FurnitureRequestEntry furnitureRequestEntry) {
        Object[] values = {furnitureRequestEntry.getSelectFurniture(), furnitureRequestEntry.getServiceID(), furnitureRequestEntry.getRoomNumber(), furnitureRequestEntry.getStaffAssignment(), furnitureRequestEntry.getAdditionalNotes(), furnitureRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FurnitureRequestEntry furnitureRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(FurnitureRequestEntry furnitureRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", String.valueOf(furnitureRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}