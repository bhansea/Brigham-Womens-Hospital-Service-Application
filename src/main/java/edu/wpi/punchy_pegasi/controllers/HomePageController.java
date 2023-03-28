package edu.wpi.punchy_pegasi.controllers;

import edu.wpi.punchy_pegasi.navigation.Navigation;
import edu.wpi.punchy_pegasi.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;

public class HomePageController {

    @FXML
    MFXButton conferenceRoomButton;
    @FXML
    MFXButton mealButton;
    @FXML
    MFXButton flowerButton;
    @FXML
    MFXButton furnitureButton;
    @FXML
    MFXButton officeSuppliesButton;
    @FXML
    MFXButton signageMapButton;

    @FXML
    public void initialize() {
        conferenceRoomButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfConferencePage
        mealButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfMealPage
        flowerButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfFlowerPage
        furnitureButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfFurniturePage
        officeSuppliesButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfOfficeSuppliesPage
        signageMapButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME)); //Change to Screen.NameOfSignageMapPage
    }
}