package edu.wpi.punchy_pegasi.generator;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.GenericRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<String/*idFieldType*/, GenericRequestEntry, GenericRequestEntry.Field> {

    static String[] fields = {/*fields*/};
    private final PdbController dbController;

    public GenericRequestEntryDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public GenericRequestEntryDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<GenericRequestEntry> get(String/*idFieldType*/ key) {
        try (var rs = dbController.searchQuery(TableType.GENERIC, ""/*idField*/, key)) {
            rs.next();
            GenericRequestEntry req/*fromResultSet*/ = null;
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String/*idFieldType*/, GenericRequestEntry> get(GenericRequestEntry.Field column, Object value) {
        var map = new HashMap<String/*idFieldType*/, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.GENERIC, column.getColName(), value)) {
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
    public Map<String/*idFieldType*/, GenericRequestEntry> get(GenericRequestEntry.Field[] params, Object[] value) {
        var map = new HashMap<String/*idFieldType*/, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.GENERIC, Arrays.stream(params).map(GenericRequestEntry.Field::getColName).toList().toArray(new String[params.length]), value)) {
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
    public Map<String/*idFieldType*/, GenericRequestEntry> getAll() {
        var map = new HashMap<String/*idFieldType*/, GenericRequestEntry>();
        try (var rs = dbController.searchQuery(TableType.GENERIC)) {
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
            dbController.insertQuery(TableType.GENERIC, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(GenericRequestEntry genericRequestEntry, GenericRequestEntry.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.GENERIC, ""/*idField*/, "genericRequestEntry"/*getID*/, Arrays.stream(params).map(GenericRequestEntry.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(genericRequestEntry)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(GenericRequestEntry genericRequestEntry) {
        try {
            dbController.deleteQuery(TableType.GENERIC, ""/*idField*/, "genericRequestEntry"/*getID*/);
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}
