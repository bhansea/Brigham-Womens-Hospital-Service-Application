package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.backend.generated.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.backend.generated.FoodServiceRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.backend.generated.LocationNameDaoImpl;
import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.Optional;

@Slf4j
public class TestDB {
    public static TestDB singleton = new TestDB();
    @Getter
//    PdbController pdb = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb",
//            "teamp", "teamp130");
    PdbController pdb = new PdbController("jdbc:postgresql://bruellcarlisle.dyndns.org:54321/softeng",
            "teamp", "teamp130");

    public static TestDB getSingleton() {
        return singleton;
    }

    public static void main(String[] args) {
        try {
            singleton.pdb.initTableByType(PdbController.TableType.LOCATIONNAMES);
            var LocationNameDAO = new LocationNameDaoImpl();
            LocationNameDAO.save(new LocationName(UUID.randomUUID(), "test", "test", LocationName.NodeType.EXIT));
            var locationNameMap = LocationNameDAO.getAll();

            singleton.pdb.initTableByType(PdbController.TableType.EDGES);
            var EdgeDAO = new EdgeDaoImpl();
            EdgeDAO.save(new Edge(new Random().nextLong(), "test", "test"));
            var edgeMap = EdgeDAO.getAll();

            // same thing but for FlowerDeliveryServiceRequest
            singleton.pdb.initTableByType(PdbController.TableType.FOODREQUESTS);
            FoodServiceRequestEntryDaoImpl FoodServiceRequestEntryDAO = new FoodServiceRequestEntryDaoImpl();
            Optional<FoodServiceRequestEntry> ret = FoodServiceRequestEntryDAO.get("11888e2b-0e84-4fd0-8ce5-0bdfbb098b2e");
            System.out.println("Food Service Retrieved");
            FoodServiceRequestEntryDAO.save(new FoodServiceRequestEntry(
                    "test",
                    "test",
                    "test",
                    "test",
                    "test",
                    "test",
                    Arrays.asList("test", "test2"),
                    "test"
            ));
            var foodServiceRequestEntryMap = FoodServiceRequestEntryDAO.getAll();
            System.out.println();
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }

//        try {
//            pdb.importTable(PdbController.TableType.NODES, "C:\\Documents\\p2\\Node.csv");
//            pdb.importTable(PdbController.TableType.EDGES, "C:\\Documents\\p2\\Edge.csv");
//            pdb.importTable(PdbController.TableType.MOVES, "C:\\Documents\\p2\\Move.csv");
//            pdb.importTable(PdbController.TableType.LOCATIONNAMES, "C:\\Documents\\p2\\LocationName.csv");
//
//        } catch (PdbController.DatabaseException e) {
//            log.error(e.getMessage());
//        }
//
//        try {
//            pdb.exportTable("C:\\Documents\\p2\\export\\Node.csv", PdbController.TableType.NODES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\Edge.csv", PdbController.TableType.EDGES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\Move.csv", PdbController.TableType.MOVES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\LocationName.csv", PdbController.TableType.LOCATIONNAMES);
//        } catch (PdbController.DatabaseException e) {
//            log.error(e.getMessage());
//        }
    }

}
