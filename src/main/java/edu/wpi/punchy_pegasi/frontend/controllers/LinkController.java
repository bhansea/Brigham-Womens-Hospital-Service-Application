package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class LinkController {
    @FXML
    public Button button;
    @FXML
    public void onMouseClick(){
        onClick.run();
    }
    private Runnable onClick;
    private String text;
    LinkController(String text, Runnable onClick) {
        this.onClick = onClick;
        this.text = text;
    }
    public void initialize(){
        button.setText(text);
    }
}
