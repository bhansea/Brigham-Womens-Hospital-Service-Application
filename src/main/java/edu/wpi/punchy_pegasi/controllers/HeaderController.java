package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.navigation.Header;
import edu.wpi.punchy_pegasi.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class HeaderController {
  @FXML MFXButton home_button;

  @FXML
  public void toHome() {
    home_button.setOnAction(event -> Header.toHome(Screen.HOME));
  }
}
