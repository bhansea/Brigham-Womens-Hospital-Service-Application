package edu.wpi.punchy_pegasi.frontend.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

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
        scrollPane.getStyleClass().add("pfx-cardholder-scrollpane");
        scrollPane.setContent(elements);
        elements.getStyleClass().add("pfx-cardholder-container-gridpane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        HBox.setHgrow(this, Priority.ALWAYS);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setBorder(Border.EMPTY);
        elements.setBorder(Border.EMPTY);
    }

    public PFXCardHolder(List<PFXCardVertical> cards) {
        super();
        setCenter(scrollPane);
        scrollPane.setContent(elements);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elements.getStyleClass().add("pfx-cardholder-container-gridpane");
        HBox.setHgrow(this, Priority.ALWAYS);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);

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

    public String getChosenItems() {
        StringBuilder items = new StringBuilder();
        for(int i=0;i<numElements;i++) {
            PFXCardVertical ele = (PFXCardVertical) elements.getChildren().get(i);
            if(ele.getQuantity() > 0) {
                items.append(ele.getTitle()).append(" ").append(ele.getQuantity()).append(", ");
            }
        }
        return items.toString();
    }

    public void clear() {
        for(int i=0;i<numElements;i++) {
            PFXCardVertical ele = (PFXCardVertical) elements.getChildren().get(i);
            ele.clearQuantity();
        }
    }
}
