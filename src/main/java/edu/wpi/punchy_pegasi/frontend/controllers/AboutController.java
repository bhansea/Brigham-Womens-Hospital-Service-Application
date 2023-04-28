package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



public class AboutController {

    @FXML
    private GridPane sourceGrid;


    @FXML
    private void initialize(){
        TeamMember blake = new TeamMember("Blake Bruell", "Lead Software Engineer", "babruell@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/blake.PNG"));
        TeamMember charlotte = new TeamMember("Charlotte Carter", "Document Analyst", "ccarter@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/charlotte.PNG"));
        TeamMember brittany = new TeamMember("Brittany Ficarra", "FE Software Engineer", "bcficarra@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/brittany.PNG"));
        TeamMember brandon = new TeamMember("Brandon Luong", "BE Software Engineer", "bvluong@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/brandon.PNG"));
        TeamMember cooper = new TeamMember("Cooper Mann", "Scrum Master", "cpmann@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/cooper.PNG"));
        TeamMember arnav = new TeamMember("Arnav Mishra", "BE Assistant Lead Software Engineer", "amishra2@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/arnav.PNG"));
        TeamMember ryan = new TeamMember("Ryan Nguyen", "FE Assistant Lead Software Engineer", "rnguyen@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/ryan.PNG"));
        TeamMember piper = new TeamMember("Piper O'Connell", "Project Manager", "proconnell@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/piper.PNG"));
        TeamMember gabe = new TeamMember("Gabe Ward", "Product Owner", "gward@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/gabe.PNG"));
        TeamMember tommy = new TeamMember("Weizhe Wang", "FE & BE Software Engineer", "wwang8@wpi.edu", "", "", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/tommy.PNG"));

        TeamMember[][] teamMembers = {
                {blake, charlotte, brittany, brandon, cooper},
                {arnav, ryan, piper, gabe, tommy}
        };

        for(int row = 0; row < 2; row++) {
            for (int col = 0; col < 5; col++) {
                sourceGrid.add(teamMembers[row][col].getLink(), col, row);
            }
        }
    }

    @Getter@Setter
    private static class TeamMember {
        private String name;
        private String position;
        private String email;
        private String major;
        private String funFact;
        private ImageView link;

        public TeamMember(String name, String position, String email, String major, String funFact, ImageView link) {
            this.name = name;
            this.position = position;
            this.email = email;
            this.major = major;
            this.funFact = funFact;
            this.link = link;
            this.link.setFitWidth(200);
            this.link.setFitHeight(200);
            this.link.setPreserveRatio(false);
            this.link.setSmooth(true);
        }
    }
}


