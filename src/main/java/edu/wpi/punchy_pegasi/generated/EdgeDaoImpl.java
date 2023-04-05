package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Arrays;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.IDao;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class EdgeDaoImpl implements IDao<Edge, String> {

    static String[] fields = {"uuid", "startNode", "endNode"};
    private final PdbController dbController;

    public EdgeDaoImpl(PdbController dbController) {
        this.dbController = dbController;
    }

    public EdgeDaoImpl() {
        this.dbController = App.getSingleton().getPdb();
    }

    @Override
    public Optional<Edge> get(String key) {
        try (var rs = dbController.searchQuery(TableType.EDGES, "uuid", key)) {
            rs.next();
            Edge req = new Edge(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.Long)rs.getObject("startNode"),
                    (java.lang.Long)rs.getObject("endNode"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Edge> getAll() {
        var map = new HashMap<String, Edge>();
        try (var rs = dbController.searchQuery(TableType.EDGES)) {
            while (rs.next()) {
                Edge req = new Edge(
                    (java.lang.Long)rs.getObject("uuid"),
                    (java.lang.Long)rs.getObject("startNode"),
                    (java.lang.Long)rs.getObject("endNode"));
                if (req != null)
                    map.put(String.valueOf(req.getUuid()), req);
            }
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
        }
        return map;
    }

    @Override
    public void save(Edge edge) {
        Object[] values = {edge.getUuid(), edge.getStartNode(), edge.getEndNode()};
        try {
            dbController.insertQuery(TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Edge edge, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(Edge edge) {
        try {
            dbController.deleteQuery(TableType.EDGES, "uuid", String.valueOf(edge.getUuid()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}