package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.schema.Node;
import edu.wpi.punchy_pegasi.schema.TableType;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class FacadeTest {
    static PdbController pdbController;
    static NodeDaoImpl nodeDao;
    static EdgeDaoImpl edgeDao;
    static MoveDaoImpl moveDao;
    static LocationNameDaoImpl locationNameDao;
    static RequestEntryDaoImpl requestEntryDao;
    static FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao;
    static FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao;
    static ConferenceRoomEntryDaoImpl conferenceRoomEntryDao;
    static FurnitureRequestEntryDaoImpl furnitureRequestEntryDao;
    static OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao;
    static EmployeeDaoImpl employeeDao;
    static AccountDaoImpl accountDao;
    static String[] nodeFields;

    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        nodeFields = new String[]{"nodeID", "xcoord", "ycoord", "floor", "building"};
        pdbController = new PdbController(Config.source, "test");
        for (var tt : TableType.values()) {
            try {
                pdbController.initTableByType(tt);
            } catch (PdbController.DatabaseException e) {
                log.error("Could not init table " + tt.name());
            }
        }
        nodeDao = new NodeDaoImpl(pdbController);
        edgeDao = new EdgeDaoImpl(pdbController);
        moveDao = new MoveDaoImpl(pdbController);
        locationNameDao = new LocationNameDaoImpl(pdbController);
        requestEntryDao = new RequestEntryDaoImpl(pdbController);
        foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(pdbController);
        flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(pdbController);
        conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(pdbController);
        furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(pdbController);
        officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(pdbController);
        employeeDao = new EmployeeDaoImpl(pdbController);
        accountDao = new AccountDaoImpl(pdbController);

    }

    @AfterAll
    static void tearDown() throws SQLException {
        var statement = pdbController.exposeConnection().createStatement();
        statement.execute("drop schema test cascade;");
    }

    @Test
    void getNode() {
        Node node = new Node(100L, 500, 500, "L1", "testBuilding");
        Object[] values = new Object[]{node.getNodeID(), node.getXcoord(), node.getYcoord(), node.getFloor(), node.getBuilding()};
        try {
            pdbController.insertQuery(TableType.NODES, nodeFields, values);
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
        Optional<Node> results = nodeDao.get(node.getNodeID());
        Node daoresult = results.get();
        assertEquals(daoresult, node);
        try {
            pdbController.deleteQuery(TableType.NODES, "nodeID", node.getNodeID());
        } catch (PdbController.DatabaseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetNode() {
    }

    @Test
    void testGetNode1() {
    }

    @Test
    void getAllNode() {
    }

    @Test
    void saveNode() {
    }

    @Test
    void updateNode() {
    }

    @Test
    void deleteNode() {
    }

    @Test
    void getEdge() {
    }

    @Test
    void testGetEdge() {
    }

    @Test
    void testGetEdge1() {
    }

    @Test
    void getAllEdge() {
    }

    @Test
    void saveEdge() {
    }

    @Test
    void updateEdge() {
    }

    @Test
    void deleteEdge() {
    }

    @Test
    void getMove() {
    }

    @Test
    void testGetMove() {
    }

    @Test
    void testGetMove1() {
    }

    @Test
    void getAllMove() {
    }

    @Test
    void saveMove() {
    }

    @Test
    void updateMove() {
    }

    @Test
    void deleteMove() {
    }

    @Test
    void getLocationName() {
    }

    @Test
    void testGetLocationName() {
    }

    @Test
    void testGetLocationName1() {
    }

    @Test
    void getAllLocationName() {
    }

    @Test
    void saveLocationName() {
    }

    @Test
    void updateLocationName() {
    }

    @Test
    void deleteLocationName() {
    }

    @Test
    void getRequestEntry() {
    }

    @Test
    void testGetRequestEntry() {
    }

    @Test
    void testGetRequestEntry1() {
    }

    @Test
    void getAllRequestEntry() {
    }

    @Test
    void saveRequestEntry() {
    }

    @Test
    void updateRequestEntry() {
    }

    @Test
    void deleteRequestEntry() {
    }

    @Test
    void getFoodServiceRequestEntry() {
    }

    @Test
    void testGetFoodServiceRequestEntry() {
    }

    @Test
    void testGetFoodServiceRequestEntry1() {
    }

    @Test
    void getAllFoodServiceRequestEntry() {
    }

    @Test
    void saveFoodServiceRequestEntry() {
    }

    @Test
    void updateFoodServiceRequestEntry() {
    }

    @Test
    void deleteFoodServiceRequestEntry() {
    }

    @Test
    void getFlowerDeliveryRequestEntry() {
    }

    @Test
    void testGetFlowerDeliveryRequestEntry() {
    }

    @Test
    void testGetFlowerDeliveryRequestEntry1() {
    }

    @Test
    void getAllFlowerDeliveryRequestEntry() {
    }

    @Test
    void saveFlowerDeliveryRequestEntry() {
    }

    @Test
    void updateFlowerDeliveryRequestEntry() {
    }

    @Test
    void deleteFlowerDeliveryRequestEntry() {
    }

    @Test
    void getConferenceRoomEntry() {
    }

    @Test
    void testGetConferenceRoomEntry() {
    }

    @Test
    void testGetConferenceRoomEntry1() {
    }

    @Test
    void getAllConferenceRoomEntry() {
    }

    @Test
    void saveConferenceRoomEntry() {
    }

    @Test
    void updateConferenceRoomEntry() {
    }

    @Test
    void deleteConferenceRoomEntry() {
    }

    @Test
    void getFurnitureRequestEntry() {
    }

    @Test
    void testGetFurnitureRequestEntry() {
    }

    @Test
    void testGetFurnitureRequestEntry1() {
    }

    @Test
    void getAllFurnitureRequestEntry() {
    }

    @Test
    void saveFurnitureRequestEntry() {
    }

    @Test
    void updateFurnitureRequestEntry() {
    }

    @Test
    void deleteFurnitureRequestEntry() {
    }

    @Test
    void getOfficeServiceRequestEntry() {
    }

    @Test
    void testGetOfficeServiceRequestEntry() {
    }

    @Test
    void testGetOfficeServiceRequestEntry1() {
    }

    @Test
    void getAllOfficeServiceRequestEntry() {
    }

    @Test
    void saveOfficeServiceRequestEntry() {
    }

    @Test
    void updateOfficeServiceRequestEntry() {
    }

    @Test
    void deleteOfficeServiceRequestEntry() {
    }

    @Test
    void getEmployee() {
    }

    @Test
    void testGetEmployee() {
    }

    @Test
    void testGetEmployee1() {
    }

    @Test
    void getAllEmployee() {
    }

    @Test
    void saveEmployee() {
    }

    @Test
    void updateEmployee() {
    }

    @Test
    void deleteEmployee() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void testGetAccount() {
    }

    @Test
    void testGetAccount1() {
    }

    @Test
    void getAllAccount() {
    }

    @Test
    void saveAccount() {
    }

    @Test
    void updateAccount() {
    }

    @Test
    void deleteAccount() {
    }
}