package edu.wpi.punchy_pegasi.frontend.navigation;

import edu.wpi.punchy_pegasi.frontend.App;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Navigation {
    public static void navigate(final Screen screen) {
        final String filename = screen.getFilename();

        try {
            final var resource = App.class.getResource(filename);
            final FXMLLoader loader = new FXMLLoader(resource);

            App.getViewPane().setCenter(loader.load());
            App.getSingleton().setCurrentScreen(screen);
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
