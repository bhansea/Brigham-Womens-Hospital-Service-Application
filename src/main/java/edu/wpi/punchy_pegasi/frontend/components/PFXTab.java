package edu.wpi.punchy_pegasi.frontend.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PFXTab extends VBox {
    private final Label label = new Label();
    @Getter
    private Node node;

    public PFXTab() {
        super();
        HBox.setHgrow(this, Priority.ALWAYS);
        VBox.setVgrow(this, Priority.ALWAYS);
        this.getStyleClass().add("pfx-tab");
        getChildren().add(label);
        label.getStyleClass().add("pfx-tab-text");
    }

    public PFXTab(String text, Node node) {
        this();
        this.label.setText(text);
        this.node = node;
    }
}
