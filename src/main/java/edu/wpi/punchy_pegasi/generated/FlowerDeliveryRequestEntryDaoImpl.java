package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FlowerDeliveryRequestEntryDaoImpl implements IDao<FlowerDeliveryRequestEntry, String> {

    static String[] fields = {"flowerSize", "flowerType", "flowerAmount", "serviceID", "patientName", "roomNumber", "staffAssignment", "additionalNotes", "status"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS, "serviceID", key)) {
            rs.next();
            FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("flowerSize"),
                    (java.lang.String)rs.getObject("flowerAmount"),
                    (java.lang.String)rs.getObject("flowerType"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, FlowerDeliveryRequestEntry> getAll() {
        var map = new HashMap<String, FlowerDeliveryRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.FLOWERREQUESTS)) {
            while (rs.next()) {
                FlowerDeliveryRequestEntry req = new FlowerDeliveryRequestEntry(
                    (java.util.UUID)rs.getObject("serviceID"),
                    (java.lang.String)rs.getObject("patientName"),
                    (java.lang.String)rs.getObject("roomNumber"),
                    (java.lang.String)rs.getObject("staffAssignment"),
                    (java.lang.String)rs.getObject("additionalNotes"),
                    edu.wpi.punchy_pegasi.schema.RequestEntry.Status.valueOf((String)rs.getObject("status")),
                    (java.lang.String)rs.getObject("flowerSize"),
                    (java.lang.String)rs.getObject("flowerAmount"),
                    (java.lang.String)rs.getObject("flowerType"));
                if (req != null)
                    map.put(String.valueOf(req.getServiceID()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        Object[] values = {flowerDeliveryRequestEntry.getFlowerSize(), flowerDeliveryRequestEntry.getFlowerType(), flowerDeliveryRequestEntry.getFlowerAmount(), flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry.getPatientName(), flowerDeliveryRequestEntry.getRoomNumber(), flowerDeliveryRequestEntry.getStaffAssignment(), flowerDeliveryRequestEntry.getAdditionalNotes(), flowerDeliveryRequestEntry.getStatus()};
        try {
            dbController.insertQuery(TableType.FLOWERREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        try {
            dbController.deleteQuery(TableType.FLOWERREQUESTS, "serviceID", String.valueOf(flowerDeliveryRequestEntry.getServiceID()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}