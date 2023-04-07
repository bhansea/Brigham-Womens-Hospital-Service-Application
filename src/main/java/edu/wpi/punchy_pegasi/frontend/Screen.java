package edu.wpi.punchy_pegasi.frontend;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.*;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

@Slf4j
public enum Screen {
    HOME("Home", "frontend/views/HomePage.fxml"),
    SIGNAGE("Signage", "frontend/views/Signage.fxml"),
    MAP_PAGE("Map", "frontend/views/MapPage.fxml"),
    LOGIN("Login", "frontend/views/Login.fxml"),
    FLOWER_DELIVERY_REQUEST("Request Flower Delivery", "frontend/requests/FlowerDeliveryRequest.fxml", FlowerDeliveryRequestController::create),
    ADMIN_PAGE("Admin Page", "frontend/requests/AdminPage.fxml"),
    OFFICE_SERVICE_REQUEST("Request office supplies", "frontend/requests/OfficeServiceRequest.fxml", OfficeServiceRequestController::create),
    FOOD_SERVICE_REQUEST("Request Food Delivery", "frontend/requests/FoodServiceRequest.fxml", FoodServiceRequestController::create),
    CONFERENCE_ROOM_SERVICE_REQUEST("Request Conference Room", "frontend/requests/ConferenceRoomRequest.fxml", ConferenceRoomController::create),
    FURNITURE_DELIVERY_SERVICE_REQUEST("Request Furniture Delivery", "frontend/requests/FurnitureDeliveryRequest.fxml", FurnitureRequestController::create);
    private final Function<String, ? extends Parent> createFunction;
    private final String path;
    private final String readable;

    Screen(String readable, String path, Function<String, ? extends Parent> createFunction) {
        this.path = path;
        this.readable = readable.toUpperCase();
        this.createFunction = createFunction;
    }

    Screen(String readable, String path) {
        this(readable, path, Screen::defaultCreate);
    }

    private static Parent defaultCreate(String path) {
        try {
            return App.getSingleton().loadWithCache(path);
        } catch (IOException e) {
            log.error("Error in screen", e);
            throw new RuntimeException(e);
        }
    }

    public Parent get() {
        return createFunction.apply(path);
    }

    public String getReadable() {
        return readable;
    }
}