package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.Edge;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class EdgeDaoImpl implements IDao<Edge, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"uuid", "startNode", "endNode"};

    @Override
    public Optional<Edge> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.EDGES, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, Edge> getAll() {
        return null;
    }

    @Override
    public void save(Edge edge) {
        Object[] values = {edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try {
            dbController.insertQuery(PdbController.TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Edge foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(Edge foodServiceRequestEntry) {

    }
}