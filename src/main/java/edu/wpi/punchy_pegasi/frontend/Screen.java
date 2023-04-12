package edu.wpi.punchy_pegasi.frontend;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.*;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.scene.Parent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

@Slf4j
public enum Screen {
    HOME("Home", "frontend/views/HomePage.fxml", Account.AccountType.STAFF),
    SIGNAGE("Signage", "frontend/views/Signage.fxml", Account.AccountType.STAFF),
    MAP_PAGE("Map", "frontend/views/PathfindingMap.fxml", Account.AccountType.STAFF),
    EDIT_MAP_PAGE("Edit Map", "frontend/views/EditMap.fxml", Account.AccountType.ADMIN),
    LOGIN("Login", "frontend/views/Login.fxml", Account.AccountType.NONE),
    FLOWER_DELIVERY_REQUEST("Request Flower Delivery", "frontend/requests/FlowerDeliveryRequest.fxml", Account.AccountType.STAFF, FlowerDeliveryRequestController::create),
    ADMIN_PAGE("Admin Page", "frontend/requests/AdminPage.fxml", Account.AccountType.ADMIN),
    OFFICE_SERVICE_REQUEST("Request office supplies", "frontend/requests/OfficeServiceRequest.fxml", Account.AccountType.STAFF, OfficeServiceRequestController::create),
    FOOD_SERVICE_REQUEST("Request Food Delivery", "frontend/requests/FoodServiceRequest.fxml", Account.AccountType.STAFF, FoodServiceRequestController::create),
    CONFERENCE_ROOM_SERVICE_REQUEST("Request Conference Room", "frontend/requests/ConferenceRoomRequest.fxml", Account.AccountType.STAFF, ConferenceRoomController::create),
    FURNITURE_DELIVERY_SERVICE_REQUEST("Request Furniture Delivery", "frontend/requests/FurnitureDeliveryRequest.fxml", Account.AccountType.STAFF, FurnitureRequestController::create);
    private final Function<String, ? extends Parent> createFunction;
    private final String path;
    private final String readable;
    @Getter
    private final Account.AccountType shield;
    Screen(String readable, String path, Account.AccountType shield, Function<String, ? extends Parent> createFunction) {
        this.path = path;
        this.readable = readable.toUpperCase();
        this.createFunction = createFunction;
        this.shield = shield;
    }

    Screen(String readable, String path, Account.AccountType shield) {
        this(readable, path, shield, Screen::defaultCreate);
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