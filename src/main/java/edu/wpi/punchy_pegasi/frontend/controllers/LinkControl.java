package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;


public class LinkControl extends HBox {
    @FXML
    public Button button;

    @FXML
    public void onMouseClick() {
        onClick.run();
    }

    private Runnable onClick;

    public LinkControl() {
        App.getSingleton().loadStylesheet("frontend/css/Link.css");
        try {
            App.getSingleton().loadWithCache(App.class.getResource("frontend/components/Link.fxml"), this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public LinkControl(String readable, Runnable onClick) {
        this();
        setText(readable);
        setOnClick(onClick);
    }

    public void initialize() {
    }

    public StringProperty textProperty() {
        return button.textProperty();
    }

    public void setText(String text) {
        textProperty().set(text);
    }

    public String getText() {
        return textProperty().get();
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public Runnable getOnClick() {
        return onClick;
    }
}
