package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Objects;

public class SidebarController {
    @FXML HBox home;
    @FXML VBox serviceRequestDropDown;
    @FXML Label conferenceRoomText;
    @FXML Label officeSuppliesText;
    @FXML Label furnitureText;
    @FXML Label flowersText;
    @FXML Label mealText;
    @FXML HBox serviceRequests;
    @FXML HBox map;
    @FXML HBox adminPage;
    @FXML HBox signagePage;
    @FXML HBox logout;
    @FXML ImageView profileImage;
    @FXML Label nameText;
    @FXML Label roleText;

    public void initialize() {

        Image defaultImage = new Image (Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
        profileImage.imageProperty().set(defaultImage);

        serviceRequestDropDown.setVisible(false);
        serviceRequestDropDown.setManaged(false);

        furnitureText.setOnMouseEntered(e -> {
            furnitureText.setOpacity(1.0);
        });

        furnitureText.setOnMouseExited(e -> {
            furnitureText.setOpacity(.5);
        });

        furnitureText.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.FURNITURE_DELIVERY_SERVICE_REQUEST);
        });

        flowersText.setOnMouseEntered(e -> {
            flowersText.setOpacity(1.0);
        });

        flowersText.setOnMouseExited(e -> {
            flowersText.setOpacity(.5);
        });

        flowersText.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.FLOWER_DELIVERY_REQUEST);
        });

        mealText.setOnMouseEntered(e -> {
            mealText.setOpacity(1.0);
        });

        mealText.setOnMouseExited(e -> {
            mealText.setOpacity(.5);
        });

        mealText.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.FOOD_SERVICE_REQUEST);
        });

        conferenceRoomText.setOnMouseEntered(e -> {
            conferenceRoomText.setOpacity(1.0);
        });

        conferenceRoomText.setOnMouseExited(e -> {
            conferenceRoomText.setOpacity(.5);
        });

        conferenceRoomText.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.CONFERENCE_ROOM_SERVICE_REQUEST);
        });

        officeSuppliesText.setOnMouseEntered(e -> {
            officeSuppliesText.setOpacity(1.0);
        });

        officeSuppliesText.setOnMouseExited(e -> {
            officeSuppliesText.setOpacity(.5);
        });

        officeSuppliesText.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.OFFICE_SERVICE_REQUEST);
        });

        map.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.MAP_PAGE);
        });

        adminPage.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.ADMIN_PAGE);
        });

        signagePage.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.SIGNAGE);
        });

        home.setOnMouseClicked(e -> {
            App.getSingleton().navigate(Screen.HOME);
        });

        logout.setOnMouseClicked(e -> {
            App.getSingleton().exit();
        });


        serviceRequests.onMouseClickedProperty().set(e -> {
            if (serviceRequestDropDown.isVisible()) {
                serviceRequestDropDown.setVisible(false);
                serviceRequestDropDown.setManaged(false);
            } else {
                serviceRequestDropDown.setVisible(true);
                serviceRequestDropDown.setManaged(true);
            }
        });




    }


}

