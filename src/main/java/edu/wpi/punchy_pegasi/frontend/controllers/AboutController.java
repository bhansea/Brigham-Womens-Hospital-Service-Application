package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



public class AboutController {

    List<TeamMember> resources = new ArrayList<>();
    @FXML
    private GridPane sourceGrid;

    @FXML
    private void initialize(){
        sourceGrid.getColumnConstraints().add(new ColumnConstraints(150));
        sourceGrid.getColumnConstraints().add(new ColumnConstraints(150));
        resources.add(new TeamMember("Blake Bruell", "Lead Software Engineer", "babruell@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/blake.PNG")));
        resources.add(new TeamMember("Charlotte Carter", "Document Analyst", "ccarter@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/charlotte.PNG")));
        resources.add(new TeamMember("Brittany Ficarra", "FE Software Engineer", "bcficarra@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/brittany.PNG")));
        resources.add(new TeamMember("Brandon Luong", "BE Software Engineer", "bvluong@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/brandon.PNG")));
        resources.add(new TeamMember("Cooper Mann", "Scrum Master", "cpmann@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/cooper.PNG")));
        resources.add(new TeamMember("Arnav Mishra", "BE Assistant Lead Software Engineer", "amishra2@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/arnav.PNG")));
        resources.add(new TeamMember("Ryan Nguyen", "FE Assistant Lead Software Engineer", "rnguyen@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/ryan.PNG")));
        resources.add(new TeamMember("Piper O'Connell", "Project Manager", "proconnell@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/piper.PNG")));
        resources.add(new TeamMember("Gabe Ward", "Product Owner", "gward@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/gabe.PNG")));
        resources.add(new TeamMember("Weizhe Wang", "FE & BE Software Engineer", "wwang8@wpi.edu", new Image("edu/wpi/punchy_pegasi/frontend/assets/headshots/tommy.PNG")));


        int rowIndex = 1;
        for (TeamMember resource : resources) {
            Label nameLabel = new Label(resource.getName());
            nameLabel.setAlignment(Pos.CENTER_LEFT);

            Label position = new Label(resource.getPosition());
            position.setAlignment(Pos.CENTER);

            Label email = new Label(resource.getEmail());
            email.setAlignment(Pos.CENTER_RIGHT);

            sourceGrid.addRow(rowIndex, nameLabel, position, email);
            rowIndex++;
        }
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(100);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPrefWidth(100);
        sourceGrid.getColumnConstraints().addAll(new ColumnConstraints(), new ColumnConstraints(), col2, col3);
    }

    @Getter@Setter
    private static class TeamMember {
        private String name;
        private String position;
        private String email;
        private Image link;

        public TeamMember(String name, String position, String email, Image link) {
            this.name = name;
            this.position = position;
            this.email = email;
            this.link = link;
        }
    }
}


