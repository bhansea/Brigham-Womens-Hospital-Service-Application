package edu.wpi.punchy_pegasi.frontend;

import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

@Slf4j
public class App extends Application {
    @Getter
    private static App singleton;
    @Setter
    @Getter
    private static Stage primaryStage;
    @Getter
    @Setter
    private static BorderPane viewPane;

    @Getter
    private static Screen currentScreen;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static void exit() {
        Platform.exit();
    }

    public void setCurrentScreen(Screen value) {
        support.firePropertyChange("page", currentScreen, value);
        currentScreen = value;
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @Override
    public void init() {
        singleton = this;
        log.info("Starting Up");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        /* primaryStage is generally only used if one of your components require the stage to display */
        App.primaryStage = primaryStage;

        final FXMLLoader loader = new FXMLLoader(App.class.getResource("views/Root.fxml"));
        final BorderPane root = loader.load();

        final var layoutLoader = new FXMLLoader(App.class.getResource("views/Layout.fxml"));
        final BorderPane loadedLayout = layoutLoader.load();
        final LayoutController layoutController = layoutLoader.getController();

        root.setCenter(loadedLayout);
        App.viewPane = layoutController.getViewPane();

        final Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();

        Navigation.navigate(Screen.LOGIN);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);
    }

    @Override
    public void stop() {
        log.info("Shutting Down");
    }
}
