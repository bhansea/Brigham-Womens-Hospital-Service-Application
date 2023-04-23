package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



public class CreditsController {

    List<Resource> resources = new ArrayList<>();
    @FXML
    private GridPane sourceGrid;

    @FXML
    private void initialize(){
        resources.add(new Resource("Source 1", "an image", new Hyperlink("https://docs.oracle.com/javafx/2/ui_controls/hyperlink.htm")));
        resources.add(new Resource("Source 2", "an icon", new Hyperlink("https://www.freecodecamp.org/news/java-operator-and-or-logical-operators/")));
        resources.add(new Resource("Source 3", "an icon", new Hyperlink("https://www.freecodecamp.org/news/java-operator-and-or-logical-operators/")));
        resources.add(new Resource("Source 4", "an image", new Hyperlink("https://docs.oracle.com/javafx/2/ui_controls/hyperlink.htm")));

        int rowIndex = 1;
        for (Resource resource : resources) {
            Label nameLabel = new Label(resource.getName());
            nameLabel.setAlignment(Pos.CENTER_LEFT);

            Label description = new Label(resource.getDescription());
            description.setAlignment(Pos.CENTER);

            Label link = new Label(resource.getLink().getText());
            link.setAlignment(Pos.CENTER_RIGHT);

            sourceGrid.addRow(rowIndex, nameLabel, description, link);
            rowIndex++;
        }
    }

    @Getter@Setter
    private static class Resource {
        private String name;
        private String description;
        private Hyperlink link;

        public Resource(String name, String description, Hyperlink link) {
            this.name = name;
            this.description = description;
            this.link = link;
        }
    }
}


