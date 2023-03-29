package edu.wpi.punchy_pegasi.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),
  FOOD_SERVICE_REQUEST("views/FoodServiceRequest.fxml"),

  LOGIN("views/Login.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
