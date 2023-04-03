package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class FoodServiceRequestEntryDaoImpl implements IDao<FoodServiceRequestEntry, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"foodSelection", "tempType", "additionalItems", "dietaryRestrictions", "serviceID", "patientName", "roomNumber", "additionalNotes"};

    @Override
    public Optional<FoodServiceRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.FOODREQUESTS, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, FoodServiceRequestEntry> getAll() {
        return null;
    }

    @Override
    public void save(FoodServiceRequestEntry foodServiceRequestEntry) {
        Object[] values = {foodServiceRequestEntry.getFoodSelection(), foodServiceRequestEntry.getTempType(), foodServiceRequestEntry.getAdditionalItems(), foodServiceRequestEntry.getDietaryRestrictions(), foodServiceRequestEntry.getServiceID(), foodServiceRequestEntry.getPatientName(), foodServiceRequestEntry.getRoomNumber(), foodServiceRequestEntry.getAdditionalNotes()};
        try {
            dbController.insertQuery(PdbController.TableType.FOODREQUESTS, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(FoodServiceRequestEntry foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(FoodServiceRequestEntry foodServiceRequestEntry) {

    }
}