package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class SignageMap {

  @FXML MFXButton homeButton;

  @FXML
  public void initialize() {
    homeButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
  }
}
