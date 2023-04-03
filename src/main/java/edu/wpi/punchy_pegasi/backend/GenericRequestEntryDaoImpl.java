package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.GenericRequestEntry;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<GenericRequestEntry, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {/*fields*/};

    @Override
    public Optional<GenericRequestEntry> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.GENERIC, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, GenericRequestEntry> getAll() {
        return null;
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

    }

    @Override
    public void delete(GenericRequestEntry foodServiceRequestEntry) {

    }
}
