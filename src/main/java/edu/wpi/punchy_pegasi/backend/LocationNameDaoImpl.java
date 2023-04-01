package edu.wpi.punchy_pegasi.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocationNameDaoImpl implements IDao<LocationName, String> {
    private HashMap<String, LocationName> locationNames;
    private PdbController dbController = PdbController.getSingleton();
    public LocationNameDaoImpl() {
        this.locationNames = new HashMap<>();
    }

    @Override
    public Optional<LocationName> get(String s) {
        return Optional.ofNullable(locationNames.get(s));
    }

    @Override
    public Map<String, LocationName> getAll() {
        return locationNames;
    }

    @Override
    public void save(LocationName locationName) {
        locationNames.put(locationName.getLongName(), locationName);
    }

    @Override
    public void update(String key, Object[] params) {
        LocationName lName = locationNames.get(key);
        if (params.length != 3) {
            //TODO: throw error
        } else {
            lName.setLongName(params[0].toString());
            lName.setShortName(params[1].toString());
            lName.setNodeType((LocationName.NodeType) params[2]);
            locationNames.put(key, lName);
        }
    }

    @Override
    public void delete(String key) {
        locationNames.remove(key);
    }


}
