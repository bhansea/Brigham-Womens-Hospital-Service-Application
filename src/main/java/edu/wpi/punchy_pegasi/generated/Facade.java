package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.schema.*;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Map;
import java.util.Optional;


public class Facade {
    private final PdbController dbController; 
    public Facade(PdbController dbController) {
        this.dbController = dbController;
    }

	public Optional<Node> getNode(java.lang.Long key) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
		return nodeDao.get(key);
	}
	public Map<java.lang.Long, Node> getNode(Node.Field column, Object value) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
		return nodeDao.get(column, value);
	}
	public Map<java.lang.Long, Node> getNode(Node.Field[] params, Object[] value) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
		return nodeDao.get(params, value);
	}
	public Map<java.lang.Long, Node> getAllNode() {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
		return nodeDao.getAll();
	}
	public void saveNode(Node node) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
	}
	public void updateNode(Node node, Node.Field[] params) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
	}
	public void deleteNode(Node node) {
		NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
	}
	public Optional<Edge> getEdge(java.lang.Long key) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
		return edgeDao.get(key);
	}
	public Map<java.lang.Long, Edge> getEdge(Edge.Field column, Object value) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
		return edgeDao.get(column, value);
	}
	public Map<java.lang.Long, Edge> getEdge(Edge.Field[] params, Object[] value) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
		return edgeDao.get(params, value);
	}
	public Map<java.lang.Long, Edge> getAllEdge() {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
		return edgeDao.getAll();
	}
	public void saveEdge(Edge edge) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
	}
	public void updateEdge(Edge edge, Edge.Field[] params) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
	}
	public void deleteEdge(Edge edge) {
		EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
	}
	public Optional<Move> getMove(java.lang.Long key) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
		return moveDao.get(key);
	}
	public Map<java.lang.Long, Move> getMove(Move.Field column, Object value) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
		return moveDao.get(column, value);
	}
	public Map<java.lang.Long, Move> getMove(Move.Field[] params, Object[] value) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
		return moveDao.get(params, value);
	}
	public Map<java.lang.Long, Move> getAllMove() {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
		return moveDao.getAll();
	}
	public void saveMove(Move move) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
	}
	public void updateMove(Move move, Move.Field[] params) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
	}
	public void deleteMove(Move move) {
		MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
	}
	public Optional<LocationName> getLocationName(java.lang.Long key) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
		return locationNameDao.get(key);
	}
	public Map<java.lang.Long, LocationName> getLocationName(LocationName.Field column, Object value) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
		return locationNameDao.get(column, value);
	}
	public Map<java.lang.Long, LocationName> getLocationName(LocationName.Field[] params, Object[] value) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
		return locationNameDao.get(params, value);
	}
	public Map<java.lang.Long, LocationName> getAllLocationName() {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
		return locationNameDao.getAll();
	}
	public void saveLocationName(LocationName locationName) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
	}
	public void updateLocationName(LocationName locationName, LocationName.Field[] params) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
	}
	public void deleteLocationName(LocationName locationName) {
		LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
	}
	public Optional<FoodServiceRequestEntry> getFoodServiceRequestEntry(java.util.UUID key) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
		return foodServiceRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getFoodServiceRequestEntry(FoodServiceRequestEntry.Field column, Object value) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
		return foodServiceRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getFoodServiceRequestEntry(FoodServiceRequestEntry.Field[] params, Object[] value) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
		return foodServiceRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getAllFoodServiceRequestEntry() {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
		return foodServiceRequestEntryDao.getAll();
	}
	public void saveFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
	}
	public void updateFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry, FoodServiceRequestEntry.Field[] params) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
	}
	public void deleteFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry) {
		FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
	}
	public Optional<FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(java.util.UUID key) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
		return flowerDeliveryRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry.Field column, Object value) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
		return flowerDeliveryRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry.Field[] params, Object[] value) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
		return flowerDeliveryRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getAllFlowerDeliveryRequestEntry() {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
		return flowerDeliveryRequestEntryDao.getAll();
	}
	public void saveFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
	}
	public void updateFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field[] params) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
	}
	public void deleteFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
		FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
	}
	public Optional<ConferenceRoomEntry> getConferenceRoomEntry(java.util.UUID key) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
		return conferenceRoomEntryDao.get(key);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getConferenceRoomEntry(ConferenceRoomEntry.Field column, Object value) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
		return conferenceRoomEntryDao.get(column, value);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getConferenceRoomEntry(ConferenceRoomEntry.Field[] params, Object[] value) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
		return conferenceRoomEntryDao.get(params, value);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getAllConferenceRoomEntry() {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
		return conferenceRoomEntryDao.getAll();
	}
	public void saveConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
	}
	public void updateConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry, ConferenceRoomEntry.Field[] params) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
	}
	public void deleteConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry) {
		ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
	}
	public Optional<FurnitureRequestEntry> getFurnitureRequestEntry(java.util.UUID key) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
		return furnitureRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getFurnitureRequestEntry(FurnitureRequestEntry.Field column, Object value) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
		return furnitureRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getFurnitureRequestEntry(FurnitureRequestEntry.Field[] params, Object[] value) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
		return furnitureRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getAllFurnitureRequestEntry() {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
		return furnitureRequestEntryDao.getAll();
	}
	public void saveFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
	}
	public void updateFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry, FurnitureRequestEntry.Field[] params) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
	}
	public void deleteFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry) {
		FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
	}
	public Optional<OfficeServiceRequestEntry> getOfficeServiceRequestEntry(java.util.UUID key) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
		return officeServiceRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getOfficeServiceRequestEntry(OfficeServiceRequestEntry.Field column, Object value) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
		return officeServiceRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getOfficeServiceRequestEntry(OfficeServiceRequestEntry.Field[] params, Object[] value) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
		return officeServiceRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getAllOfficeServiceRequestEntry() {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
		return officeServiceRequestEntryDao.getAll();
	}
	public void saveOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
	}
	public void updateOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry, OfficeServiceRequestEntry.Field[] params) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
	}
	public void deleteOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry) {
		OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
	}
}