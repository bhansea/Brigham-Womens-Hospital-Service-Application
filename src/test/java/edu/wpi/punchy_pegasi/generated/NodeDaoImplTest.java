package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.TableType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class NodeDaoImplTest {
    PdbController pdbController;

    @BeforeAll
    void init(){
        pdbController = new PdbController("jdbc:postgresql://database.cs.wpi.edu:5432/teampdb", "teamp", "teamp130");
        try {
            pdbController.initTableByType(TableType.NODES);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void get() {
    }

    @Test
    void testGet() {
    }

    @Test
    void getAll() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}