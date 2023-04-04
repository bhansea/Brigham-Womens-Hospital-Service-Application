package edu.wpi.punchy_pegasi.frontend.navigation;

import edu.wpi.punchy_pegasi.frontend.App;
import javafx.scene.Parent;

public class Navigation {
    public static void navigate(final Screen screen) {
        try {
            Parent p = screen.getSupplier().get();
            App.getSingleton().getViewPane().setCenter(p);
            App.getSingleton().setCurrentScreen(screen);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
