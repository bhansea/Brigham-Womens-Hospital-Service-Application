package edu.wpi.punchy_pegasi.backend;


import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
public class PdbController {

    @AllArgsConstructor
    @Getter
    public enum Source {
        Wong("database.cs.wpi.edu", 5432, "teampdb", "teamp", "teamp130"),
        Blake("bruellcarlisle.dyndns.org", 54321, "softeng", "teamp", "teamp130"),
        Local("localhost", 54321, "postgres", "", "");
        private String url;
        private int port;
        private String database;
        private String username;
        private String password;
    }
    private Connection connection;
    public final Source source;
    public PdbController(Source source) throws SQLException, ClassNotFoundException {
        this.source = source;
        Class.forName("org.postgresql.Driver");
        connection = DriverManager.getConnection("jdbc:postgresql://" + source.url + ":" + source.port + "/" + source.database, source.username, source.password);
    }

    private static String objectToPsqlString(Object o) {
        return objectToPsqlString(o, true);
    }

    public static String objectToPsqlString(Object o, boolean first) {
        if (o instanceof String || o instanceof UUID || o instanceof LocalDate || o.getClass().isEnum()) {
            return "'" + o + "'";
        } else if (o instanceof List<?>) {
            return (first ? "ARRAY" : "") + "[" + String.join(", ", ((List<?>) o).stream().map(v -> objectToPsqlString(v, false)).toList()) + "]";
        } else if (o instanceof Object[]) {
            return (first ? "ARRAY" : "") + "[" + String.join(", ", Arrays.stream((Object[]) o).map(v -> objectToPsqlString(v, false)).toList()) + "]";
        } else {
            return o.toString();
        }
    }

    public void initTableByType(TableType tableType) throws DatabaseException {
        try {
            initTable(tableType);
            log.info(tableType.name() + " table initialized");
        } catch (SQLException e) {
            log.error("Failed to initialize " + tableType.name() + " table", e);
            throw new DatabaseException("SQL error");
        }
    }

    private void initTable(TableType tableType) throws SQLException {
        var statement = connection.createStatement();
        statement.execute("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";"); // create uuid extension
        var q = tableType.getTableSQL();
        statement.execute(q);
    }

    private String getFieldValueString(String[] fields, Object[] values, String equator, String delimiter) {
        String query = "";
        for (int i = 0; i < fields.length; i++) {
            query += fields[i] + " " + equator + " " + objectToPsqlString(values[i]);
            if (i != fields.length - 1) query += delimiter;
        }
        return query;
    }

    /**
     * Updates all entries in tableType table where the entries fields[0] value matches object[0]
     *
     * @param tableType
     * @param fields
     * @param values
     * @return The number of rows updated
     * @throws DatabaseException
     */
    public int updateQuery(TableType tableType, String keyField, Object keyValue, String[] fields, Object[] values) throws DatabaseException {
        if (fields.length != values.length) throw new DatabaseException("Fields and values must be the same length");
        try {
            var statement = connection.createStatement();
            var query = "UPDATE teamp." + tableType.name().toLowerCase() + " SET ";
            query += getFieldValueString(fields, values, " = ", ", ");
            query += " WHERE " + keyField + " = " + objectToPsqlString(keyValue);
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error("Failed to update node", e);
            throw new DatabaseException("SQL error");
        }
    }

    /**
     * Inserts a new entry into the tableType table
     *
     * @param tableType
     * @param fields
     * @param values
     * @return The number of rows inserted
     * @throws DatabaseException
     */
    public int insertQuery(TableType tableType, String[] fields, Object[] values) throws DatabaseException {
        if (fields.length != values.length) throw new DatabaseException("Fields and values must be the same length");
        try {
            var statement = connection.createStatement();
            var query = "INSERT INTO teamp." + tableType.name().toLowerCase() + " (";
            query += String.join(", ", fields);
            query += ") VALUES (";
            query += String.join(", ", Arrays.stream(values).map(PdbController::objectToPsqlString).toList());
            query += ");";
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error("Failed to insert row", e);
            throw new DatabaseException("SQL error");
        }
    }

    /**
     * Deletes all entries in tableType table whose fields match the given values
     *
     * @param tableType
     * @param field
     * @param value
     * @return The number of rows deleted
     * @throws DatabaseException
     */
    public int deleteQuery(TableType tableType, String[] field, Object[] value) throws DatabaseException {
        if (field.length != value.length) throw new DatabaseException("Fields and values must be the same length");
        try {
            var statement = connection.createStatement();
            var query = "DELETE FROM teamp." + tableType.name().toLowerCase() + " WHERE ";
            query += getFieldValueString(field, value, "=", " AND ");
            query += ";";
            return statement.executeUpdate(query);
        } catch (SQLException e) {
            log.error("Failed to delete node", e);
            throw new DatabaseException("SQL error");
        }
    }
    public String getDeleteQuery(TableType tableType, String[] field, Object[] value) throws DatabaseException {
        if (field.length != value.length) throw new DatabaseException("Fields and values must be the same length");
        var query = "DELETE FROM teamp." + tableType.name().toLowerCase() + " WHERE ";
        query += getFieldValueString(field, value, "=", " AND ");
        query += ";";
        return query;
    }

    public int[] executeDeletes(List<String> deleteQueries) throws DatabaseException {
        try {
            var statement = connection.createStatement();
            for(var query : deleteQueries)
                statement.addBatch(query);
            return statement.executeBatch();
        } catch (SQLException e) {
            log.error("Failed to delete node", e);
            throw new DatabaseException("SQL error");
        }
    }

    /**
     * Deletes all entries in tableType table whose value for "field" matches the given value
     *
     * @param tableType
     * @param field
     * @param value
     * @return The number of rows deleted
     * @throws DatabaseException
     */
    public int deleteQuery(TableType tableType, String field, Object value) throws DatabaseException {
        return deleteQuery(tableType, new String[]{field}, new Object[]{value});
    }

    /**
     * Searches for all entries in tableType table whose fields match the given values
     *
     * @param tableType
     * @param fields
     * @param values
     * @return A list of all matching entries
     * @throws DatabaseException
     */
    public ResultSet searchQuery(TableType tableType, String[] fields, Object[] values) throws DatabaseException {
        if (fields.length != values.length) throw new DatabaseException("Fields and values must be the same length");
        try {
            var statement = connection.createStatement();
            var query = "SELECT * FROM teamp." + tableType.name().toLowerCase();
            if (fields.length > 0) {
                query += " WHERE ";
                query += getFieldValueString(fields, values, "=", " AND ");
            }
            query += ";";
            return statement.executeQuery(query);
        } catch (SQLException e) {
            log.error("Failed to search node", e);
            throw new DatabaseException("SQL error");
        }
    }

    public ResultSet searchQuery(TableType tableType) throws DatabaseException {
        return searchQuery(tableType, new String[]{}, new Object[]{});
    }

    /**
     * Searches for all entries in tableType table whose value for "field" matches the given value
     *
     * @param tableType
     * @param field
     * @param value
     * @return A list of all matching entries
     * @throws DatabaseException
     */
    public ResultSet searchQuery(TableType tableType, String field, Object value) throws DatabaseException {
        return searchQuery(tableType, new String[]{field}, new Object[]{value});
    }

    public void exportTable(String path, TableType tableType) throws DatabaseException {
        try {
            exportToCSV(path, tableType);
            log.info("Exported table successfully");
        } catch (SQLException | IOException e) {
            log.error("Failed to export table:", e);
            if (e instanceof IOException) throw new DatabaseException("Failed to open selected file");
            else if (e instanceof SQLException) throw new DatabaseException("Table does not exist");
        }
    }

    public void importTable(TableType tableType, String path) throws DatabaseException {
        try {
            initTableByType(tableType);
        } catch (DatabaseException e) {
            log.error("Failed to initialize table:", e);
        }
        try {
            insertfromCSV(path, tableType);
            log.info("Imported table successfully");

        } catch (SQLException | IOException e) {
            log.error("Failed to import table:", e);
            if (e instanceof IOException) {
                throw new DatabaseException("Failed to open selected file");
            } else if (e instanceof SQLException) {
                if (e.getMessage().contains("violates unique constraint")) {
                    throw new DatabaseException("One or more nodes in csv already exist");
                } else if (Pattern.matches("[\\S\\s]*column.+does not exist[\\S\\s]*", e.getMessage())) {
                    throw new DatabaseException("CSV headers do not match table headers");
                }
            }
            throw new DatabaseException("Table does not exist, CSV has incorrect headers");
        }
    }

    private int insertfromCSV(String path, TableType tableType) throws IOException, SQLException {
        // read the file one line at a time
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(path));

        var sb = new StringBuilder();
        sb.append("INSERT INTO ").append(tableType.name().toLowerCase()).append("(").append(reader.readLine()).append(") VALUES ");

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append("(");
            Arrays.stream(line.split(",")).forEach(val -> {
                sb.append("'");
                sb.append(val);
                sb.append("',");
            });
            sb.setLength(sb.length() - 1);
            sb.append("),");
        }
        sb.setLength(sb.length() - 1);
        sb.append("ON CONFLICT DO NOTHING;");
        var statement = connection.createStatement();
        return statement.executeUpdate(sb.toString());
    }

    private void exportToCSV(String path, TableType tableType) throws SQLException, DatabaseException, IOException {
        try (var result = searchQuery(tableType); var fw = new FileWriter(path)) {
            var metaData = result.getMetaData();
            var headerCount = metaData.getColumnCount();
            for (int i = 1; i <= headerCount; i++) {
                fw.append(metaData.getColumnName(i));
                fw.append(i < headerCount ? ',' : '\n');
            }
            while (result.next()) {
                for (int i = 1; i <= headerCount; i++) {
                    fw.append(result.getString(i));
                    fw.append(i <= headerCount - 1 ? ',' : '\n');
                }
            }
        }
    }

    public class DatabaseException extends Exception {
        public DatabaseException(String e) {
            super(e);
        }
    }
}
