package edu.wpi.punchy_pegasi.frontend.navigation;

public enum Screen {
    ROOT("views/Root.fxml"),
    HOME("views/HomePage.fxml"),

    FLOWERDELIVERY_REQUEST("views/FlowerDelivery.fxml"),
    SERVICE_REQUEST("views/ServiceRequest.fxml"),
    SIGNAGE("views/Signage.fxml"),
    FOOD_SERVICE_REQUEST("views/FoodServiceRequest.fxml"),
    MAP_PAGE("views/MapPage.fxml"),
    LOGIN("views/Login.fxml");

    private final String filename;

    Screen(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
