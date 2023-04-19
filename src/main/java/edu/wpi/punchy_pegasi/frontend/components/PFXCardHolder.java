package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;


public class PFXCardHolder extends BorderPane {
    ScrollPane scrollPane = new ScrollPane();
    GridPane elements = new GridPane();
    int numElements = 0;
    public PFXCardHolder() {
        super();
        setCenter(scrollPane);
        getStyleClass().add("pfx-cardholder-container");
        scrollPane.setContent(elements);
        elements.getStyleClass().add("pfx-cardholder-container-gridpane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

    }

    public PFXCardHolder(List<PFXCardVertical> cards) {
        super();
        setCenter(scrollPane);
        scrollPane.setContent(elements);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elements.getStyleClass().add("pfx-cardholder-container-gridpane");

        int val = 0;
        for(int i=0;i<(int)Math.ceil(cards.size()/3.0);i++) {
            for(int j=0;j<2;j++) {
                if(cards.size() <= val) {
                    break;
                }
                elements.add(cards.get(val), j, i);
                numElements++;
                val++;
            }
        }
    }

    public String getChosenMeals() {
        StringBuilder meals = new StringBuilder();
        for(int i=0;i<numElements;i++) {
            System.out.println("here");
            PFXCardVertical ele = (PFXCardVertical) elements.getChildren().get(i);
            System.out.println("hereeeee");
            if(ele.getQuantity() > 0) {
                meals.append(ele.getTitle()).append(" ").append(ele.getQuantity()).append(", ");
            }
        }
        return meals.toString();
    }
}
