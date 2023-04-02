package edu.wpi.punchy_pegasi.backend;

import edu.wpi.punchy_pegasi.frontend.FoodServiceRequestEntry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestDB {
    public static TestDB singleton = new TestDB();
    public static TestDB getSingleton() {
        return singleton;
    }
    @Getter
    PdbController pdb = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb",
        "teamp", "teamp130");
    public static void main(String[] args) {
        try {
            singleton.pdb.initTableByType(PdbController.TableType.FOODREQUESTS);
            new FoodServiceRequestDaoImpl().save(new FoodServiceRequestEntry("test", "test", "test", "test", "test",
                    List.of(new String[]{"test", "test", "test"}), "test"));
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
