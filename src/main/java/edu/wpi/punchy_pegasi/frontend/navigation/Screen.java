package edu.wpi.punchy_pegasi.frontend.navigation;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.FlowerDeliveryRequestController;
import edu.wpi.punchy_pegasi.frontend.controllers.requests.FoodServiceRequestController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.function.Supplier;

@Slf4j
public enum Screen {
    ROOT("Root", "views/Root.fxml"),
    HOME("Home", "views/HomePage.fxml"),
    FLOWERDELIVERY_REQUEST("Flower Delivery Request", FlowerDeliveryRequestController::create),
    SERVICE_REQUEST("Service Request", "views/ServiceRequest.fxml"),
    SIGNAGE("Signage", "views/Signage.fxml"),
    FOOD_SERVICE_REQUEST("Food Service Request", FoodServiceRequestController::create),
    MAP_PAGE("Map", "views/MapPage.fxml"),
    LOGIN("Login", "views/Login.fxml");
    private final Supplier<? extends Parent> supplier;
    private final String readable;

    Screen(String readable, Supplier<? extends Parent> supplier) {
        this.readable = readable.toUpperCase();
        this.supplier = supplier;
    }

    Screen(String readable, String path) {
        this.readable = readable.toUpperCase();
        this.supplier = new Supplier<Parent>() {
            @Override
            public Parent get() {
                final var genericResource = App.class.getResource(path);
                FXMLLoader generic = new FXMLLoader(genericResource);
                try {
                    return generic.load();
                } catch (IOException e) {
                    log.error("Error in screen", e);
                    return null;
                }
            }
        };
    }

    public Supplier<? extends Parent> getSupplier() {
        return supplier;
    }

    public String getReadable() {
        return readable;
    }
}