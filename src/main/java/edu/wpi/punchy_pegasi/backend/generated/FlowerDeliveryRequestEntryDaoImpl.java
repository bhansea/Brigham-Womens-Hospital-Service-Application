package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import edu.wpi.punchy_pegasi.frontend.FlowerDeliveryRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FlowerDeliveryRequestEntryDaoImpl implements IDao<FlowerDeliveryRequestEntry, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"flowerSize", "flowerType", "flowerAmount", "serviceID", "patientName", "roomNumber", "additionalNotes"};

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.FLOWERREQUESTS, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, FlowerDeliveryRequestEntry> getAll() {
        return null;
    }

    @Override
    public void save(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
        Object[] values = {flowerDeliveryRequestEntry.getFlowerSize(), flowerDeliveryRequestEntry.getFlowerType(), flowerDeliveryRequestEntry.getFlowerAmount(), flowerDeliveryRequestEntry.getServiceID(), flowerDeliveryRequestEntry.getPatientName(), flowerDeliveryRequestEntry.getRoomNumber(), flowerDeliveryRequestEntry.getAdditionalNotes()};
        try {
            dbController.insertQuery(PdbController.TableType.FLOWERREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FlowerDeliveryRequestEntry foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(FlowerDeliveryRequestEntry foodServiceRequestEntry) {

    }
}