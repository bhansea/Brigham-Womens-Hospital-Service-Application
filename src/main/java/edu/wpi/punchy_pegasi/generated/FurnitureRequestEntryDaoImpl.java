package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FurnitureRequestEntry;
import java.util.Arrays;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FurnitureRequestEntryDaoImpl implements IDao<java.util.UUID, FurnitureRequestEntry, FurnitureRequestEntry.Field> {

    static String[] fields = {"serviceID", "roomNumber", "staffAssignment", "additionalNotes", "status", "selectFurniture"};
    private final PdbController dbController;

    public FurnitureRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public FurnitureRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<FurnitureRequestEntry> get(java.util.UUID key) {
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
    public Optional<FurnitureRequestEntry> get(FurnitureRequestEntry.Field column, Object value) {
        try (var rs = dbController.searchQuery(TableType.FURNITUREREQUESTS, column.name(), value)) {
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
    public Map<java.util.UUID, FurnitureRequestEntry> getAll() {
        var map = new HashMap<java.util.UUID, FurnitureRequestEntry>();
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
                    map.put(req.getServiceID(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FurnitureRequestEntry furnitureRequestEntry) {
        Object[] values = {furnitureRequestEntry.getServiceID(), furnitureRequestEntry.getRoomNumber(), furnitureRequestEntry.getStaffAssignment(), furnitureRequestEntry.getAdditionalNotes(), furnitureRequestEntry.getStatus(), furnitureRequestEntry.getSelectFurniture()};
        try {
            dbController.insertQuery(TableType.FURNITUREREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FurnitureRequestEntry furnitureRequestEntry, FurnitureRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequestEntry.getServiceID(), Arrays.stream(params).map(FurnitureRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(furnitureRequestEntry)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(FurnitureRequestEntry furnitureRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FURNITUREREQUESTS, "serviceID", furnitureRequestEntry.getServiceID());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}