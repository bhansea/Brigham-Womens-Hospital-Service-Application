package edu.wpi.punchy_pegasi.frontend.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;


import java.util.ArrayList;
import java.util.List;


public class PFXCardHolder extends BorderPane {
    ScrollPane scrollPane = new ScrollPane();
    GridPane elements = new GridPane();
    Label filterLabel;
    int numElements = 0;

    public PFXCardHolder(List<PFXCardVertical> cards, String filterName) {
        super();
        filterLabel = new Label(filterName);
        filterLabel.getStyleClass().add("pfx-cardholder-container-label");
        setTop(filterLabel);
        setCenter(scrollPane);
        scrollPane.setContent(elements);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elements.getStyleClass().add("pfx-cardholder-container-gridpane");
        HBox.setHgrow(this, Priority.ALWAYS);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        for(int i=0;i<cards.size();i++) {
            elements.add(cards.get(i), i, 0);
        }
    }

    public List<String> getChosenItems() {
        List<String> items = new ArrayList<String>();
        for(int i=0;i<numElements;i++) {
            PFXCardVertical ele = (PFXCardVertical) elements.getChildren().get(i);
            if(ele.getQuantity() > 0) {
                items.add(ele.getTitle() + " " + ele.getQuantity());
            }
        }
        return items;
    }

    public void clear() {
        for(int i=0;i<numElements;i++) {
            PFXCardVertical ele = (PFXCardVertical) elements.getChildren().get(i);
            ele.clearQuantity();
        }
    }
}
