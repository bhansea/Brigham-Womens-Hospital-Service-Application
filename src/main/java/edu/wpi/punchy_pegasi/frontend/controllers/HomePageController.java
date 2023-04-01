package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import javax.imageio.stream.MemoryCacheImageOutputStream;

public class HomePageController {

    @FXML
    public void initialize() {
//
    }

    public void conferenceRoomClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.CONFERENCE);
    }

    public void mealButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.FOOD_SERVICE_REQUEST);
    }

    public void flowerButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.FLOWERDELIVERY_REQUEST);
    }

    public void furnitureButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.HOME);
    }

    public void officeSuppliesClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.HOME);
    }

    public void signageButtonClick(MouseEvent mouseEvent) {
        Navigation.navigate(Screen.SIGNAGE);
    }

    public void mapButtonClick(MouseEvent mouseEvent) {Navigation.navigate(Screen.MAP_PAGE);}
}
