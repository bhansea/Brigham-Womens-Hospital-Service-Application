package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PFXPicker extends HBox {
    private final Label quantity = new Label("5");
    private final PFXButton minus = new PFXButton();
    private final PFXButton plus = new PFXButton();
    public PFXPicker() {
        super();
        minus.setText("-");
        plus.setText("+");
        getStyleClass().add("pfx-picker-container");
        getChildren().addAll(minus, quantity, plus);
        HBox.setHgrow(minus, Priority.ALWAYS);
        HBox.setHgrow(quantity, Priority.ALWAYS);
        HBox.setHgrow(plus, Priority.ALWAYS);
    }
}
