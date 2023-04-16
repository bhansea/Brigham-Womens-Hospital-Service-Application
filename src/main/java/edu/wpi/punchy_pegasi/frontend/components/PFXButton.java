package edu.wpi.punchy_pegasi.frontend.components;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class PFXButton extends MFXButton {
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");

    private BooleanProperty selected = new SimpleBooleanProperty();

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
    public PFXButton() {
        super();
        this.getStyleClass().add("pfx-button");
    }

    /**
     * Creates a button with the specified text as its label.
     *
     * @param text A text string for its label.
     */
    public PFXButton(String text) {
        super(text);
    }

    /**
     * Creates a button with the specified text and icon for its label.
     *
     * @param text    A text string for its label.
     * @param graphic the icon for its label.
     */
    public PFXButton(String text, Node graphic) {
        super(text, graphic);
    }
}
