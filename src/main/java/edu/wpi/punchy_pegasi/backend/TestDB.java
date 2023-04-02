package edu.wpi.punchy_pegasi.backend;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDB {
    public static void main(String[] args) {
        PdbController pdb = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb",
                "teamp", "teamp130");
        try {
            pdb.importTable(PdbController.TableType.NODES, "C:\\Documents\\p2\\Node.csv");
            pdb.importTable(PdbController.TableType.EDGES, "C:\\Documents\\p2\\Edge.csv");
            pdb.importTable(PdbController.TableType.MOVES, "C:\\Documents\\p2\\Move.csv");
            pdb.importTable(PdbController.TableType.LOCATIONNAMES, "C:\\Documents\\p2\\LocationName.csv");

        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }

        try {
            pdb.exportTable("C:\\Documents\\p2\\export\\Node.csv", PdbController.TableType.NODES);
            pdb.exportTable("C:\\Documents\\p2\\export\\Edge.csv", PdbController.TableType.EDGES);
            pdb.exportTable("C:\\Documents\\p2\\export\\Move.csv", PdbController.TableType.MOVES);
            pdb.exportTable("C:\\Documents\\p2\\export\\LocationName.csv", PdbController.TableType.LOCATIONNAMES);
        } catch (PdbController.DatabaseException e) {
            log.error(e.getMessage());
        }
    }

}
