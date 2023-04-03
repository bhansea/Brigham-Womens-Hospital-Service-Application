package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.LocationName;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LocationNameDaoImpl implements IDao<LocationName, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"longName", "shortName", "nodeType"};

    @Override
    public Optional<LocationName> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.LOCATIONNAMES, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, LocationName> getAll() {
        return null;
    }

    @Override
    public void save(LocationName locationName) {
        Object[] values = {locationName.getLongName(), locationName.getShortName(), locationName.getNodeType()};
        try {
            dbController.insertQuery(PdbController.TableType.LOCATIONNAMES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(LocationName foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(LocationName foodServiceRequestEntry) {

    }
}