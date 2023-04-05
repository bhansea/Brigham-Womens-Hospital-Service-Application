package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.generated.EdgeDaoImpl;
import edu.wpi.punchy_pegasi.generated.FoodServiceRequestEntryDaoImpl;
import edu.wpi.punchy_pegasi.generated.LocationNameDaoImpl;
import edu.wpi.punchy_pegasi.schema.Edge;
import edu.wpi.punchy_pegasi.schema.FoodServiceRequestEntry;
import edu.wpi.punchy_pegasi.schema.LocationName;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
public class TestDB {

    @Getter
    private static final TestDB singleton = new TestDB();
    @Getter
//    PdbController pdb = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb",
//            "teamp", "teamp130");
    PdbController pdb = new PdbController("jdbc:postgresql://bruellcarlisle.dyndns.org:54321/softeng",
            "teamp", "teamp130");


    public static void main(String[] args) {
        try {
            singleton.pdb.initTableByType(TableType.LOCATIONNAMES);
            var LocationNameDAO = new LocationNameDaoImpl();
            LocationNameDAO.save(new LocationName("lllllllong Name", "short name", LocationName.NodeType.EXIT));
            LocationNameDAO.save(new LocationName("lllllllongg Name", "short name", LocationName.NodeType.DEPT));
            var locationNameMap = LocationNameDAO.getAll();
            System.out.println();
//            singleton.pdb.initTableByType(TableType.EDGES);
//            var EdgeDAO = new EdgeDaoImpl();
//            EdgeDAO.save(new Edge(new Random().nextLong(), "test", "test"));
//            var edgeMap = EdgeDAO.getAll();

            // same thing but for FoodDeliveryServiceRequest
//            singleton.pdb.initTableByType(TableType.FOODREQUESTS);
//            FoodServiceRequestEntryDaoImpl FoodServiceRequestEntryDAO = new FoodServiceRequestEntryDaoImpl();

            // test get
//            Optional<FoodServiceRequestEntry> ret = FoodServiceRequestEntryDAO.get("11888e2b-0e84-4fd0-8ce5-0bdfbb098b2e");
//            System.out.println("Food Service Retrieved");

            // test save
//            FoodServiceRequestEntryDAO.save(new FoodServiceRequestEntry(
//                    "test",
//                    "test",
//                    "test",
//                    "test",
//                    "test",
//                    "test",
//                    Arrays.asList("test", "test2"),
//                    "test"
//            ));
//            var foodServiceRequestEntryMap = FoodServiceRequestEntryDAO.getAll();
//            System.out.println();
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }

//        try {
//            pdb.importTable(TableType.NODES, "C:\\Documents\\p2\\Node.csv");
//            pdb.importTable(TableType.EDGES, "C:\\Documents\\p2\\Edge.csv");
//            pdb.importTable(TableType.MOVES, "C:\\Documents\\p2\\Move.csv");
//            pdb.importTable(TableType.LOCATIONNAMES, "C:\\Documents\\p2\\LocationName.csv");
//
//        } catch (PdbController.DatabaseException e) {
//            log.error(e.getMessage());
//        }
//
//        try {
//            pdb.exportTable("C:\\Documents\\p2\\export\\Node.csv", TableType.NODES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\Edge.csv", TableType.EDGES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\Move.csv", TableType.MOVES);
//            pdb.exportTable("C:\\Documents\\p2\\export\\LocationName.csv", TableType.LOCATIONNAMES);
//        } catch (PdbController.DatabaseException e) {
//            log.error(e.getMessage());
//        }
    }

}
