package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.GenericRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<GenericRequestEntry, String> {

    static String[] fields = {/*fields*/};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<GenericRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.GENERIC, ""/*idField*/, key)) {
            rs.next();
            GenericRequestEntry req/*fromResultSet*/ = null;
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, GenericRequestEntry> getAll() {
        var map = new HashMap<String, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(PdbController.TableType.GENERIC)) {
            while (rs.next()) {
                GenericRequestEntry req/*fromResultSet*/ = null;
                if (req != null)
                    map.put("req"/*getID*/, req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(GenericRequestEntry genericRequestEntry) {
        Object[] values = {/*getFields*/};
        try {
            dbController.insertQuery(PdbController.TableType.GENERIC, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(GenericRequestEntry foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(GenericRequestEntry foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.GENERIC, ""/*idField*/, "foodServiceRequestEntry"/*getID*/);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}
