package edu.wpi.punchy_pegasi.generated;

import edu.wpi.punchy_pegasi.schema.*;
import edu.wpi.punchy_pegasi.backend.PdbController;
import java.util.Map;
import java.util.Optional;


public class Facade {
    private PdbController dbController = null; 
    public Facade(PdbController dbController) {
        this.dbController = dbController;
    }

	private NodeDaoImpl nodeDao = new NodeDaoImpl(dbController);
	public Optional<Node> getNode(java.lang.Long key) {
		return nodeDao.get(key);
	}
	public Map<java.lang.Long, Node> getNode(Node.Field column, Object value) {
		return nodeDao.get(column, value);
	}
	public Map<java.lang.Long, Node> getNode(Node.Field[] params, Object[] value) {
		return nodeDao.get(params, value);
	}
	public Map<java.lang.Long, Node> getAllNode() {
		return nodeDao.getAll();
	}
	public void saveNode(Node node) {
		nodeDao.save(node);
	}
	public void updateNode(Node node, Node.Field[] params) {
		nodeDao.update(node, params);
	}
	public void deleteNode(Node node) {
		nodeDao.delete(node);
	}
	private EdgeDaoImpl edgeDao = new EdgeDaoImpl(dbController);
	public Optional<Edge> getEdge(java.lang.Long key) {
		return edgeDao.get(key);
	}
	public Map<java.lang.Long, Edge> getEdge(Edge.Field column, Object value) {
		return edgeDao.get(column, value);
	}
	public Map<java.lang.Long, Edge> getEdge(Edge.Field[] params, Object[] value) {
		return edgeDao.get(params, value);
	}
	public Map<java.lang.Long, Edge> getAllEdge() {
		return edgeDao.getAll();
	}
	public void saveEdge(Edge edge) {
		edgeDao.save(edge);
	}
	public void updateEdge(Edge edge, Edge.Field[] params) {
		edgeDao.update(edge, params);
	}
	public void deleteEdge(Edge edge) {
		edgeDao.delete(edge);
	}
	private MoveDaoImpl moveDao = new MoveDaoImpl(dbController);
	public Optional<Move> getMove(java.lang.Long key) {
		return moveDao.get(key);
	}
	public Map<java.lang.Long, Move> getMove(Move.Field column, Object value) {
		return moveDao.get(column, value);
	}
	public Map<java.lang.Long, Move> getMove(Move.Field[] params, Object[] value) {
		return moveDao.get(params, value);
	}
	public Map<java.lang.Long, Move> getAllMove() {
		return moveDao.getAll();
	}
	public void saveMove(Move move) {
		moveDao.save(move);
	}
	public void updateMove(Move move, Move.Field[] params) {
		moveDao.update(move, params);
	}
	public void deleteMove(Move move) {
		moveDao.delete(move);
	}
	private LocationNameDaoImpl locationNameDao = new LocationNameDaoImpl(dbController);
	public Optional<LocationName> getLocationName(java.lang.Long key) {
		return locationNameDao.get(key);
	}
	public Map<java.lang.Long, LocationName> getLocationName(LocationName.Field column, Object value) {
		return locationNameDao.get(column, value);
	}
	public Map<java.lang.Long, LocationName> getLocationName(LocationName.Field[] params, Object[] value) {
		return locationNameDao.get(params, value);
	}
	public Map<java.lang.Long, LocationName> getAllLocationName() {
		return locationNameDao.getAll();
	}
	public void saveLocationName(LocationName locationName) {
		locationNameDao.save(locationName);
	}
	public void updateLocationName(LocationName locationName, LocationName.Field[] params) {
		locationNameDao.update(locationName, params);
	}
	public void deleteLocationName(LocationName locationName) {
		locationNameDao.delete(locationName);
	}
	private FoodServiceRequestEntryDaoImpl foodServiceRequestEntryDao = new FoodServiceRequestEntryDaoImpl(dbController);
	public Optional<FoodServiceRequestEntry> getFoodServiceRequestEntry(java.util.UUID key) {
		return foodServiceRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getFoodServiceRequestEntry(FoodServiceRequestEntry.Field column, Object value) {
		return foodServiceRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getFoodServiceRequestEntry(FoodServiceRequestEntry.Field[] params, Object[] value) {
		return foodServiceRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FoodServiceRequestEntry> getAllFoodServiceRequestEntry() {
		return foodServiceRequestEntryDao.getAll();
	}
	public void saveFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry) {
		foodServiceRequestEntryDao.save(foodServiceRequestEntry);
	}
	public void updateFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry, FoodServiceRequestEntry.Field[] params) {
		foodServiceRequestEntryDao.update(foodServiceRequestEntry, params);
	}
	public void deleteFoodServiceRequestEntry(FoodServiceRequestEntry foodServiceRequestEntry) {
		foodServiceRequestEntryDao.delete(foodServiceRequestEntry);
	}
	private FlowerDeliveryRequestEntryDaoImpl flowerDeliveryRequestEntryDao = new FlowerDeliveryRequestEntryDaoImpl(dbController);
	public Optional<FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(java.util.UUID key) {
		return flowerDeliveryRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry.Field column, Object value) {
		return flowerDeliveryRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry.Field[] params, Object[] value) {
		return flowerDeliveryRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FlowerDeliveryRequestEntry> getAllFlowerDeliveryRequestEntry() {
		return flowerDeliveryRequestEntryDao.getAll();
	}
	public void saveFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
		flowerDeliveryRequestEntryDao.save(flowerDeliveryRequestEntry);
	}
	public void updateFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry, FlowerDeliveryRequestEntry.Field[] params) {
		flowerDeliveryRequestEntryDao.update(flowerDeliveryRequestEntry, params);
	}
	public void deleteFlowerDeliveryRequestEntry(FlowerDeliveryRequestEntry flowerDeliveryRequestEntry) {
		flowerDeliveryRequestEntryDao.delete(flowerDeliveryRequestEntry);
	}
	private ConferenceRoomEntryDaoImpl conferenceRoomEntryDao = new ConferenceRoomEntryDaoImpl(dbController);
	public Optional<ConferenceRoomEntry> getConferenceRoomEntry(java.util.UUID key) {
		return conferenceRoomEntryDao.get(key);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getConferenceRoomEntry(ConferenceRoomEntry.Field column, Object value) {
		return conferenceRoomEntryDao.get(column, value);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getConferenceRoomEntry(ConferenceRoomEntry.Field[] params, Object[] value) {
		return conferenceRoomEntryDao.get(params, value);
	}
	public Map<java.util.UUID, ConferenceRoomEntry> getAllConferenceRoomEntry() {
		return conferenceRoomEntryDao.getAll();
	}
	public void saveConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry) {
		conferenceRoomEntryDao.save(conferenceRoomEntry);
	}
	public void updateConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry, ConferenceRoomEntry.Field[] params) {
		conferenceRoomEntryDao.update(conferenceRoomEntry, params);
	}
	public void deleteConferenceRoomEntry(ConferenceRoomEntry conferenceRoomEntry) {
		conferenceRoomEntryDao.delete(conferenceRoomEntry);
	}
	private FurnitureRequestEntryDaoImpl furnitureRequestEntryDao = new FurnitureRequestEntryDaoImpl(dbController);
	public Optional<FurnitureRequestEntry> getFurnitureRequestEntry(java.util.UUID key) {
		return furnitureRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getFurnitureRequestEntry(FurnitureRequestEntry.Field column, Object value) {
		return furnitureRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getFurnitureRequestEntry(FurnitureRequestEntry.Field[] params, Object[] value) {
		return furnitureRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, FurnitureRequestEntry> getAllFurnitureRequestEntry() {
		return furnitureRequestEntryDao.getAll();
	}
	public void saveFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry) {
		furnitureRequestEntryDao.save(furnitureRequestEntry);
	}
	public void updateFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry, FurnitureRequestEntry.Field[] params) {
		furnitureRequestEntryDao.update(furnitureRequestEntry, params);
	}
	public void deleteFurnitureRequestEntry(FurnitureRequestEntry furnitureRequestEntry) {
		furnitureRequestEntryDao.delete(furnitureRequestEntry);
	}
	private OfficeServiceRequestEntryDaoImpl officeServiceRequestEntryDao = new OfficeServiceRequestEntryDaoImpl(dbController);
	public Optional<OfficeServiceRequestEntry> getOfficeServiceRequestEntry(java.util.UUID key) {
		return officeServiceRequestEntryDao.get(key);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getOfficeServiceRequestEntry(OfficeServiceRequestEntry.Field column, Object value) {
		return officeServiceRequestEntryDao.get(column, value);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getOfficeServiceRequestEntry(OfficeServiceRequestEntry.Field[] params, Object[] value) {
		return officeServiceRequestEntryDao.get(params, value);
	}
	public Map<java.util.UUID, OfficeServiceRequestEntry> getAllOfficeServiceRequestEntry() {
		return officeServiceRequestEntryDao.getAll();
	}
	public void saveOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry) {
		officeServiceRequestEntryDao.save(officeServiceRequestEntry);
	}
	public void updateOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry, OfficeServiceRequestEntry.Field[] params) {
		officeServiceRequestEntryDao.update(officeServiceRequestEntry, params);
	}
	public void deleteOfficeServiceRequestEntry(OfficeServiceRequestEntry officeServiceRequestEntry) {
		officeServiceRequestEntryDao.delete(officeServiceRequestEntry);
	}
}