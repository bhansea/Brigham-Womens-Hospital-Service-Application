package edu.wpi.punchy_pegasi.backend.generated;

import edu.wpi.punchy_pegasi.backend.IDao;
import edu.wpi.punchy_pegasi.backend.Node;
import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.backend.TestDB;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class NodeDaoImpl implements IDao<Node, String> {

    private final PdbController dbController = TestDB.getSingleton().getPdb();

    static String[] fields = {"nodeID", "xcoord", "ycoord", "floor", "building"};

    @Override
    public Optional<Node> get(String key) {
        try (var rs = dbController.searchQuery(PdbController.TableType.NODES, fields[0], key)) {
        } catch (PdbController.DatabaseException | SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Map<String, Node> getAll() {
        return null;
    }

    @Override
    public void save(Node node) {
        Object[] values = {node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            dbController.insertQuery(PdbController.TableType.NODES, fields, values);
        } catch (PdbController.DatabaseException e) {
            log.error("Error saving", e);
        }

    }

    @Override
    public void update(Node foodServiceRequestEntry, Object[] params) {

    }

    @Override
    public void delete(Node foodServiceRequestEntry) {

    }
}