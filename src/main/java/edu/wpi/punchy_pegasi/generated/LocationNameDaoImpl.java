package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.TestDB;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LocationNameDaoImpl implements IDao<LocationName, String> {

    private static final String[] fields = {"longName", "shortName", "nodeType"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<LocationName> get(String key) {
        try (var rs = dbController.searchQuery(TableType.LOCATIONNAMES, "uuid", key)) {
            rs.next();
            LocationName req = new LocationName(
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("shortName"),
                    edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf((String)rs.getObject("nodeType")));
            return Optional.of(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, LocationName> getAll() {
        var map = new HashMap<String, LocationName>();
        try (var rs = dbController.searchQuery(TableType.LOCATIONNAMES)) {
            while (rs.next()) {
                LocationName req = new LocationName(
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("shortName"),
                    edu.wpi.punchy_pegasi.schema.LocationName.NodeType.valueOf((String)rs.getObject("nodeType")));
                map.put(String.valueOf(req.getLongName()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(LocationName locationName) {
        Object[] values = {locationName.getLongName(), locationName.getShortName(), locationName.getNodeType()};
        try {
            dbController.insertQuery(TableType.LOCATIONNAMES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(LocationName locationName, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(LocationName locationName) {
        try {
            dbController.deleteQuery(TableType.LOCATIONNAMES, "longName", String.valueOf(locationName.getLongName()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}