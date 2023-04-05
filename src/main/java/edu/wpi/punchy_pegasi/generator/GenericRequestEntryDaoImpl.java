package edu.wpi.punchy_pegasi.generator;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.GenericRequestEntry;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.*;

@Slf4j
public class GenericRequestEntryDaoImpl implements IDao<String/*idFieldType*/, GenericRequestEntry, GenericRequestEntryDaoImpl.Column> {

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
    public Optional<GenericRequestEntry> get(Column column, Object value) {
        try (var rs = dbController.searchQuery(TableType.GENERIC, column.name(), value)) {
            rs.next();
            GenericRequestEntry req/*fromResultSet*/ = null;
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
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
    public void update(GenericRequestEntry genericRequestEntry, Column[] params) {
        Object[] values = {/*getFields*/};
        List<Object> pruned = new ArrayList<>();
        for(var column : params)
            pruned.add(values[Arrays.asList(Column.values()).indexOf(column)]);
        try {
            dbController.updateQuery(TableType.GENERIC, ""/*idField*/, "genericRequestEntry"/*getID*/, (String[])Arrays.stream(params).map(p->p.getColName()).toArray(), pruned.toArray());
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

    @RequiredArgsConstructor
    public enum Column {
        ;
        @Getter
        private final String colName;
    }
}
