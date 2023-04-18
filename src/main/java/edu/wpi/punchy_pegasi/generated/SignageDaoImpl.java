package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Signage;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class SignageDaoImpl implements IDao<java.lang.String, Signage, Signage.Field> {

    static String[] fields = {"longName", "directionType"};
    private final PdbController dbController;

    public SignageDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public SignageDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Signage> get(java.lang.String key) {
        try (var rs = dbController.searchQuery(TableType.SIGNAGE, "longName", key)) {
            rs.next();
            Signage req = new Signage(
                    rs.getObject("longName", java.lang.String.class),
                    edu.wpi.punchy_pegasi.schema.Signage.DirectionType.valueOf(rs.getString("directionType")));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<java.lang.String, Signage> get(Signage.Field column, Object value) {
        return get(new Signage.Field[]{column}, new Object[]{value});
    }

    @Override
    public Map<java.lang.String, Signage> get(Signage.Field[] params, Object[] value) {
        var map = new HashMap<java.lang.String, Signage>();
        try (var rs = dbController.searchQuery(TableType.SIGNAGE, Arrays.stream(params).map(Signage.Field::getColName).toList().toArray(new String[params.length]), value)) {
            while (rs.next()) {
                Signage req = new Signage(
                        rs.getObject("longName", java.lang.String.class),
                        edu.wpi.punchy_pegasi.schema.Signage.DirectionType.valueOf(rs.getString("directionType")));
                if (req != null)
                    map.put(req.getLongName(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public Map<java.lang.String, Signage> getAll() {
        var map = new HashMap<java.lang.String, Signage>();
        try (var rs = dbController.searchQuery(TableType.SIGNAGE)) {
            while (rs.next()) {
                Signage req = new Signage(
                        rs.getObject("longName", java.lang.String.class),
                        edu.wpi.punchy_pegasi.schema.Signage.DirectionType.valueOf(rs.getString("directionType")));
                if (req != null)
                    map.put(req.getLongName(), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Signage signage) {
        Object[] values = {signage.getLongName(), signage.getDirectionType()};
        try {
            dbController.insertQuery(TableType.SIGNAGE, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Signage signage, Signage.Field[] params) {
        if (params.length < 1)
            return;
        try {
            dbController.updateQuery(TableType.SIGNAGE, "longName", signage.getLongName(), Arrays.stream(params).map(Signage.Field::getColName).toList().toArray(new String[params.length]), Arrays.stream(params).map(p -> p.getValue(signage)).toArray());
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }
    }

    @Override
    public void delete(Signage signage) {
        try {
            dbController.deleteQuery(TableType.SIGNAGE, "longName", signage.getLongName());
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}