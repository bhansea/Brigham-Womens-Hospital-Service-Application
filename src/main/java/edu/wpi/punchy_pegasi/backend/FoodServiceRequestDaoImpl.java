package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class FoodServiceRequestDaoImpl implements IDao<FoodServiceRequestEntry, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    @Override
    public Optional<FoodServiceRequestEntry> get(String s) {
        return Optional.empty();
    }

    @Override
    public Map<String, FoodServiceRequestEntry> getAll() {
        return null;
    }

    @Override
    public void save(FoodServiceRequestEntry foodServiceRequestEntry) {
        String[] fields = {"patientName", "roomNumber", "additionalNotes", "foodSelection", "tempType", "additionalItems", "dietaryRestrictions"};
        Object[] values = {foodServiceRequestEntry.getPatientName(),
                foodServiceRequestEntry.getRoomNumber(),
                foodServiceRequestEntry.getAdditionalNotes(),
                foodServiceRequestEntry.getFoodSelection(),
                foodServiceRequestEntry.getTempType(),
                foodServiceRequestEntry.getAdditionalItems(),
                foodServiceRequestEntry.getDietaryRestrictions()};
        try {
            dbController.insertQuery(PdbController.TableType.FOODREQUESTS,
                    fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving food service request", e);
        }

    }

    @Override
    public void update(FoodServiceRequestEntry foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(FoodServiceRequestEntry foodServiceRequestEntry) {

    }
}
