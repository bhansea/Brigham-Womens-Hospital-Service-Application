package edu.wpi.punchy_pegasi.frontend.controllers.requests;

import edu.wpi.punchy_pegasi.frontend.components.PFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NotificationWindowController {
    @FXML
    PFXButton close;

    public void start(Stage primaryStage) throws IOException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        NotificationWindowController controller = new NotificationWindowController();
        loader.setController(controller);
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void end(ActionEvent actionEvent) {
    }
}
