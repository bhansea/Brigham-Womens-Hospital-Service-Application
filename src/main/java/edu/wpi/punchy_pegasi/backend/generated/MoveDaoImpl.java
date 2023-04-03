package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.Move;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class MoveDaoImpl implements IDao<Move, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"uuid", "nodeID", "longName", "date"};

    @Override
    public Optional<Move> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.MOVES, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, Move> getAll() {
        return null;
    }

    @Override
    public void save(Move move) {
        Object[] values = {move.getUuid(), move.getNodeID(), move.getLongName(), move.getDate()};
        try {
            dbController.insertQuery(PdbController.TableType.MOVES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Move foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(Move foodServiceRequestEntry) {

    }
}