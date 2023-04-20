package edu.wpi.punchy_pegasi.frontend.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class PFXTab extends VBox {
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private final Label label = new Label();
    private final BooleanProperty selected = new SimpleBooleanProperty();
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

    public BooleanProperty selectedProperty() {
        return this.selected;
    }

    public boolean getSelected() {
        return this.selected.get();
    }

    public void setSelected(boolean value) {
        pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, value);
        this.selected.set(value);
    }
}
