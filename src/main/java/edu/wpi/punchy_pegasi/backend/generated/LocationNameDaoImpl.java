package edu.wpi.punchy_pegasi.backend.generated;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.backend.TestDB;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.backend.LocationName;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LocationNameDaoImpl implements IDao<LocationName, String> {

    static String[] fields = {"uuid", "longName", "shortName", "nodeType"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<LocationName> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.LOCATIONNAMES, "uuid", key)) {
            rs.next();
            LocationName req = new LocationName(
                    (java.util.UUID)rs.getObject("uuid"),
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("shortName"),
                    edu.wpi.punchy_pegasi.backend.LocationName.NodeType.valueOf((String)rs.getObject("nodeType")));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, LocationName> getAll() {
        var map = new HashMap<String, LocationName>();
        try (var rs = dbController.searchQuery(PdbController.TableType.LOCATIONNAMES)) {
            while (rs.next()) {
                LocationName req = new LocationName(
                    (java.util.UUID)rs.getObject("uuid"),
                    (java.lang.String)rs.getObject("longName"),
                    (java.lang.String)rs.getObject("shortName"),
                    edu.wpi.punchy_pegasi.backend.LocationName.NodeType.valueOf((String)rs.getObject("nodeType")));
                if (req != null)
                    map.put(String.valueOf(req.getUuid()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(LocationName locationName) {
        Object[] values = {locationName.getUuid(), locationName.getLongName(), locationName.getShortName(), locationName.getNodeType()};
        try {
            dbController.insertQuery(PdbController.TableType.LOCATIONNAMES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(LocationName foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(LocationName foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.LOCATIONNAMES, "uuid", String.valueOf(foodServiceRequestEntry.getUuid()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}