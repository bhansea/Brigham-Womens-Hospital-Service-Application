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
    ROOT("views/Root.fxml"),
    HOME("views/HomePage.fxml"),
    FLOWERDELIVERY_REQUEST(FlowerDeliveryRequestController::create),
    SERVICE_REQUEST("views/ServiceRequest.fxml"),
    SIGNAGE("views/Signage.fxml"),
    FOOD_SERVICE_REQUEST(FoodServiceRequestController::create),
    MAP_PAGE("views/MapPage.fxml"),
    CONFERENCE("views/ConferenceRoomRequest.fxml"),
    LOGIN("views/Login.fxml");
    private final Supplier<? extends Parent> supplier;

    Screen(Supplier<? extends Parent> supplier) {
        this.supplier = supplier;
    }

    Screen(String path) {
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
}
