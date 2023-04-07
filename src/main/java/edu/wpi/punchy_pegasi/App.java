package edu.wpi.punchy_pegasi;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.frontend.controllers.SplashController;
import edu.wpi.punchy_pegasi.frontend.navigation.Navigation;
import edu.wpi.punchy_pegasi.frontend.navigation.Screen;
import edu.wpi.punchy_pegasi.schema.TableType;
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
import java.net.URL;

@Slf4j
public class App extends Application {
    @Getter
    private static App singleton;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @Getter
    private PdbController pdb;
    @Setter
    @Getter
    private Stage primaryStage;
    @Getter
    @Setter
    private BorderPane viewPane;
    @Getter
    private Screen currentScreen;
    @Getter
    private Scene scene;

    public void exit() {
        Platform.exit();
    }

    public void loadStylesheet(String resourcePath) {
        var resource = App.class.getResource(resourcePath);
        if(resource != null)
            scene.getStylesheets().add(resource.toExternalForm());
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

    @Getter
    private FXMLLoader loader = new FXMLLoader();

    @Override
    public void stop() {
        log.info("Shutting Down");
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;

        final BorderPane loadedSplash = loadWithCache(App.class.getResource("frontend/views/Splash.fxml"));
        final SplashController splashController = loader.getController();
        scene = new Scene(loadedSplash, 600, 400);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);
        loadStylesheet("frontend/css/MFXColors.css");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();


        splashController.setOnConnection(pdb -> Platform.runLater(() -> loadUI(pdb)));
        splashController.getConnection();
    }

    private void loadUI(PdbController pdb) {
        if(pdb == null) {
            log.error("No database connection");
            return;
        }
        this.pdb = pdb;
        try {
            final BorderPane loadedLayout = loadWithCache(App.class.getResource("frontend/layouts/AppLayout.fxml"));
            final LayoutController layoutController = loader.getController();

            viewPane = layoutController.getViewPane();

            scene = new Scene(loadedLayout, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
            Navigation.navigate(Screen.HOME);
            MFXThemeManager.addOn(scene, Themes.DEFAULT);
            loadStylesheet("frontend/css/MFXColors.css");
            loadStylesheet("frontend/css/Button.css");
            new Thread(this::initDatabaseTables).start();
        } catch (IOException e) {
            log.error("Failed to load application", e);
        }
    }

    private void initDatabaseTables() {
        for (var tt : TableType.values()) {
            try {
                pdb.initTableByType(tt);
            } catch (PdbController.DatabaseException e) {
                log.error("Could not init table " + tt.name());
            }
        }
    }

    public <T> T loadWithCache(URL url) throws IOException {
        return loadWithCache(url, null, null);
    }

    public <T> T loadWithCache(URL url, Object controller) throws IOException {
        return loadWithCache(url, null, controller);
    }

    public <T> T loadWithCache(URL url, Object root, Object controller) throws IOException {
        loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        loader.setLocation(url);
        return loader.load();
    }
}
