package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class HomePageController {

    @FXML
    Button conferenceRoomButton;
    @FXML
    Button mealButton;
    @FXML
    Button flowerButton;
    @FXML
    Button furnitureButton;
    @FXML
    Button officeSuppliesButton;
    @FXML
    Button signageMapButton;

    @FXML
    public void initialize() {
        conferenceRoomButton.setOnMouseClicked(
                event -> Navigation.navigate(Screen.HOME)); // Change to Screen.NameOfConferencePage
        mealButton.setOnMouseClicked(event -> Navigation.navigate(Screen.FOOD_SERVICE_REQUEST));
        flowerButton.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWERDELIVERY_REQUEST));
        furnitureButton.setOnMouseClicked(
                event -> Navigation.navigate(Screen.HOME)); // Change to Screen.NameOfFurniturePage
        officeSuppliesButton.setOnMouseClicked(
                event -> Navigation.navigate(Screen.HOME)); // Change to Screen.NameOfOfficeSuppliesPage
        signageMapButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE));
    }
}
