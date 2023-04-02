package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.App;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LocationNameDaoImpl implements IDao<LocationName, String> {
    private final HashMap<String, LocationName> locationNames;
    private final PdbController dbController = App.getSingleton().getPdb();

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
    public void update(LocationName lName, Object[] params) {
        var key = lName.getLongName();  // TODO: is long name the primary key?
        LocationName newLocName = locationNames.get(key);
        if (params.length != 3) {
            //TODO: throw error
        } else {
            newLocName.setLongName(params[0].toString());
            newLocName.setShortName(params[1].toString());
            newLocName.setNodeType((LocationName.NodeType) params[2]);
            locationNames.put(key, newLocName);
        }
    }

    @Override
    public void delete(LocationName locationName) {
        var key = locationName.getLongName();  // TODO: is long name the primary key?
        locationNames.remove(key);
    }


}
