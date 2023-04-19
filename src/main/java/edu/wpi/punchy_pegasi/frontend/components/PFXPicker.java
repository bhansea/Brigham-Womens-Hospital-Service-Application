package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import static edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols.HEART_MINUS;

public class PFXPicker extends HBox {
    private final Label quantity = new Label("0");
    private final PFXButton minus = new PFXButton("", new PFXIcon(MaterialSymbols.REMOVE));
    private final PFXButton plus = new PFXButton("", new PFXIcon(MaterialSymbols.ADD));
    public PFXPicker() {
        super();
        getStyleClass().add("pfx-picker-container");
        getChildren().addAll(minus, quantity, plus);
        minus.getStyleClass().add("pfx-picker-minus");
        plus.getStyleClass().add("pfx-picker-plus");
        HBox.setHgrow(minus, Priority.ALWAYS);
        HBox.setHgrow(quantity, Priority.ALWAYS);
        HBox.setHgrow(plus, Priority.ALWAYS);
        minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int val = Integer.parseInt(quantity.getText()) - 1;
                if(val >= 0) {
                    minus.setDisable(false);
                    quantity.setText(Integer.toString(val));
                }
                else
                    minus.setDisable(true);
            }
        });
        plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int val = Integer.parseInt(quantity.getText()) + 1;
                quantity.setText(Integer.toString(val));
                minus.setDisable(false);
            }
        });
    }

    public int getQuantity() {
        return Integer.parseInt(quantity.getText());
    }
    public void clearQuantity() {
        quantity.setText("0");
        minus.setDisable(true);
    }
}
