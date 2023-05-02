package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;


public class AboutController {

    @FXML
    private GridPane sourceGrid;


    @FXML
    private void initialize() {
        TeamMember blake = new TeamMember("Blake Bruell", "Lead Software Engineer", "babruell@wpi.edu", "Computer Science (BS/MS)", "I play the piano", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/blake.PNG"));
        TeamMember charlotte = new TeamMember("Charlotte Carter", "Document Analyst", "ccarter@wpi.edu", "Data Science (BS)", "I have too many squishmallows", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/charlotte.PNG"));
        TeamMember brittany = new TeamMember("Brittany Ficarra", "FE Software Engineer", "bcficarra@wpi.edu", "IMGD & Computer Science (BS)", "I collect comic books", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/brittany.PNG"));
        TeamMember brandon = new TeamMember("Brandon Luong", "BE Software Engineer", "bvluong@wpi.edu", "Computer Science (BS)", "I am allergic to bananas", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/brandon.PNG"));
        TeamMember cooper = new TeamMember("Cooper Mann", "Scrum Master", "cpmann@wpi.edu", "Robotics Engineering (BS)", "I play the trumpet", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/cooper.PNG"));
        TeamMember arnav = new TeamMember("Arnav Mishra", "BE Assistant Lead Software Engineer", "amishra2@wpi.edu", "Computer Science & Data Science (BS)", "I play the guitar", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/arnav.jpg"));
        TeamMember ryan = new TeamMember("Ryan Nguyen", "FE Assistant Lead Software Engineer", "rnguyen@wpi.edu", "Computer Science (BS)", "I am allergic to the cold brrr", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/ryan.PNG"));
        TeamMember piper = new TeamMember("Piper O'Connell", "Project Manager", "proconnell@wpi.edu", "Computer Science (BS)", "I throw the javelin", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/piper.PNG"));
        TeamMember gabe = new TeamMember("Gabe Ward", "Product Owner", "gward@wpi.edu", "Robotics Engineering (BS)", "I am insane with the yo-yo", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/gabe.PNG"));
        TeamMember tommy = new TeamMember("Weizhe Wang", "FE & BE Software Engineer", "wwang8@wpi.edu", "Electrical Engineering (BS)", "I play flight and racing simulators", new ImageView("edu/wpi/punchy_pegasi/frontend/assets/headshots/tommy.PNG"));

        TeamMember[][] teamMembers = {
                {blake, charlotte, brittany, brandon, cooper},
                {arnav, ryan, piper, gabe, tommy}
        };

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 5; col++) {
                sourceGrid.add(teamMembers[row][col].getStackpane(), col, row);
            }
        }
    }

    @Getter
    @Setter
    private static class TeamMember {
        private String name;
        private String position;
        private String email;
        private String major;
        private String funFact;
        private ImageView image;
        private StackPane stackpane;
        private Label nameLabel;
        private Label positionLabel;
        private Label hiddenTextOverlay;
        private VBox textOverlay;

        public TeamMember(String name, String position, String email, String major, String funFact, ImageView image) {
            this.name = name;
            this.position = position;
            this.email = email;
            this.major = major;
            this.funFact = funFact;
            this.image = image;

            this.image.setFitWidth(250);
            this.image.setFitHeight(250);
            this.image.setPreserveRatio(false);
            this.image.setSmooth(true);

            stackpane = new StackPane(image);

            nameLabel = new Label(this.name);
            nameLabel.getStyleClass().add("info-about-team-member-name");


            DropShadow dropShadow = new DropShadow(5, Color.BLACK);
            positionLabel = new Label(this.position);
            positionLabel.getStyleClass().add("info-about-team-member-position");
            positionLabel.setEffect(dropShadow);
            GridPane.setVgrow(positionLabel, Priority.ALWAYS);

            hiddenTextOverlay = new Label();
            Text nameText = new Text(this.name);
            Text positionText = new Text(this.position);
            nameText.setStyle("-fx-font-weight: bold");
            positionText.setStyle("-fx-font-style: italic");
            Text description = new Text("____________________________\nMajor: " + this.major + "\nFun fact: " + this.funFact + "\nContact: " + this.email);
            VBox vbox = new VBox(nameText, positionText, description);
            vbox.setPadding(new Insets(8));
            hiddenTextOverlay.setGraphic(vbox);
            stackpane.getChildren().add(hiddenTextOverlay);
            StackPane.setAlignment(hiddenTextOverlay, Pos.TOP_LEFT);
            hiddenTextOverlay.getStyleClass().add("info-about-hidden-text");
            nameText.setWrappingWidth(240);
            positionText.setWrappingWidth(240);
            description.setWrappingWidth(240);
            hiddenTextOverlay.setVisible(false);
            GridPane.setVgrow(hiddenTextOverlay, Priority.ALWAYS);
            hiddenTextOverlay.setPrefWidth(230);

            textOverlay = new VBox(nameLabel, positionLabel);
            textOverlay.setPadding(new Insets(0, 0, 8, 8));
            stackpane.getChildren().add(textOverlay);
            textOverlay.setAlignment(Pos.BOTTOM_LEFT);

            stackpane.setOnMouseEntered(event -> {
                hiddenTextOverlay.setVisible(true);
                textOverlay.setVisible(false);
                hiddenTextOverlay.setVisible(true);
                stackpane.setStyle("-fx-background-color: white;");
                image.setOpacity(0.1);
            });

            stackpane.setOnMouseExited(event -> {
                hiddenTextOverlay.setVisible(false);
                textOverlay.setVisible(true);
                hiddenTextOverlay.setVisible(false);
                stackpane.setStyle("-fx-background-color: transparent;");
                image.setOpacity(1.0);
            });
        }
    }
}


