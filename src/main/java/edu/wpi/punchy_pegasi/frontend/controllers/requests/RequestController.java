package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.App;
import edu.wpi.punchy_pegasi.frontend.RequestEntry;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public abstract class RequestController<T extends RequestEntry> {
    @FXML
    protected T requestEntry;
    @FXML
    protected TextField patientName;
    @FXML
    protected TextField roomNumber;
    @FXML
    protected TextField additionalNotes;

    public static BorderPane create(RequestController controller, String path) {
        final var genericResource = App.class.getResource("components/Request.fxml");
        FXMLLoader generic = new FXMLLoader(genericResource);
        final var resource = App.class.getResource(path);
        FXMLLoader loader = new FXMLLoader(resource);

        generic.setController(controller);
        loader.setController(controller);
        try {
            Parent l = loader.load();
            BorderPane g = generic.load();
            g.setCenter(l);
            return g;
        } catch (IOException e) {
            log.error("create error", e);
            return null;
        }
    }

    @FXML
    protected void submitEntry() {
    }

    protected boolean isLoaded() {
        return patientName != null;
    }

    @FXML
    protected final void initialize() {
        if (!isLoaded()) return;
        init();
    }

    public abstract void init();

    protected boolean checkSumbit() {
        return (patientName.getText().isBlank() || roomNumber.getText().isBlank() || additionalNotes.getText().isBlank());
    }
}
