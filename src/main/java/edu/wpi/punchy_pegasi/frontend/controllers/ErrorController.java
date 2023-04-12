package edu.wpi.punchy_pegasi.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

public class ErrorController {
    @FXML
    public ScrollPane errorView;
    @FXML
    private Label errorMessage;

    public void setErrorText(String text) {
        errorMessage.setText(text);
    }

    @FXML
    private void close() {
        errorMessage.getScene().getWindow().hide();
    }

    @FXML
    private void showError() {
        errorView.setVisible(true);
        errorView.setManaged(true);
    }

    @FXML
    private void copyError() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(errorMessage.getText());
        clipboard.setContent(content);
    }

    @FXML
    private void sendError() {
        // TODO: Implement remote error destination
    }
}