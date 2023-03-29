package edu.wpi.punchy_pegasi.navigation;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/Home.fxml"),

  FLOWERDELIVERY_REQUEST("views/FlowerDelivery.fxml"),
  SERVICE_REQUEST("views/ServiceRequest.fxml"),

  LOGIN("views/Login.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
