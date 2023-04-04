package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.Edge;
import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.App;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class EdgeDaoImpl implements IDao<Edge, String> {

    static String[] fields = {"uuid", "startNode", "endNode"};
    private final PdbController dbController = App.getSingleton().getPdb();

    @Override
    public Optional<Edge> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.EDGES, "uuid", key)) {
            rs.next();
            Edge req = new Edge(
                    (java.lang.Long) rs.getObject("uuid"),
                    (java.lang.String) rs.getObject("startNode"),
                    (java.lang.String) rs.getObject("endNode"));
            return Optional.ofNullable(req);
        } catch (PdbController.DatabaseException | SQLException e) {
            log.error("", e);
            return Optional.empty();
        }
    }

    @Override
    public Map<String, Edge> getAll() {
        var map = new HashMap<String, Edge>();
        try (var rs = dbController.searchQuery(PdbController.TableType.EDGES)) {
            while (rs.next()) {
                Edge req = new Edge(
                        (java.lang.Long) rs.getObject("uuid"),
                        (java.lang.String) rs.getObject("startNode"),
                        (java.lang.String) rs.getObject("endNode"));
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
            dbController.insertQuery(PdbController.TableType.EDGES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Edge foodServiceRequestEntry, Object[] params) {
        // What does this even mean?
    }

    @Override
    public void delete(Edge foodServiceRequestEntry) {
        try {
            dbController.deleteQuery(PdbController.TableType.EDGES, "uuid", String.valueOf(foodServiceRequestEntry.getUuid()));
        } catch (PdbController.DatabaseException e) {
            log.error("Error deleting", e);
        }
    }
}