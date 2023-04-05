package edu.wpi.punchy_pegasi.frontend.navigation;

import edu.wpi.punchy_pegasi.App;
import javafx.scene.Parent;

public class Navigation {
    public static void navigate(final Screen screen) {
        try {
            App.getSingleton().getViewPane().setCenter(screen.get());
            App.getSingleton().setCurrentScreen(screen);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
