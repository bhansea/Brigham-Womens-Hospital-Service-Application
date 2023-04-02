package edu.wpi.punchy_pegasi.backend;
import edu.wpi.punchy_pegasi.frontend.FlowerDeliveryRequestEntry;
import edu.wpi.punchy_pegasi.frontend.App;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Optional;

public class FlowerDeliveryServiceDaoImpl implements IDao<FlowerDeliveryRequestEntry, String> {

    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<FlowerDeliveryRequestEntry> get(String s) {
//        ResultSet result = dbController.searchQuery();
        return Optional.empty();
    }

    @Override
    public Map<String, FlowerDeliveryRequestEntry> getAll() {
        return null;
    }

    @Override
    public void save(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {

    }

    @Override
    public void update(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, Object[] params) {

    }

    @Override
    public void delete(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {

    }
}
