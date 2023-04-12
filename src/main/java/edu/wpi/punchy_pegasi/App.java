package edu.wpi.punchy_pegasi;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.controllers.ErrorController;
import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.frontend.controllers.SplashController;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Account;
import edu.wpi.punchy_pegasi.schema.TableType;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.SQLException;

@Slf4j
public class App extends Application {
    @Getter
    private static App singleton;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @Getter
    private PdbController pdb;
    @Getter
    private Facade facade;
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
    @Getter
    private Account account = new Account("", "", 0L, Account.AccountType.NONE);
    public void setAccount(Account account) {
        if(account == null){
            navigate(Screen.LOGIN);
            account = new Account("", "", 0L, Account.AccountType.NONE);
        }
        support.firePropertyChange("account", this.account, account);
        this.account = account;
    }

    private static void showError(Thread t, Throwable e) {
        log.error("An unexpected error occurred in " + t, e);
        if (Platform.isFxApplicationThread()) {
            showErrorDialog(e);
        }
    }

    public void exit() {
        Platform.exit();
    }

    private static void showErrorDialog(Throwable e) {
        StringWriter errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader(App.class.getResource("frontend/components/Error.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg.toString());
            dialog.setScene(new Scene(root, 600, 400));
            dialog.show();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
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

    public void navigate(final Screen screen) {
        if(screen == null) return;
        if(account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()){
            getViewPane().setCenter(screen.get());
            setCurrentScreen(screen);
        }
    }

    public void loadStylesheet(String resourcePath) {
        var resource = App.class.getResource(resourcePath);
        if (resource != null)
            scene.getStylesheets().add(resource.toExternalForm());
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(App::showError);
        this.primaryStage = primaryStage;

        final BorderPane loadedSplash = loadWithCache("frontend/views/Splash.fxml");
        final SplashController splashController = loader.getController();
        scene = new Scene(loadedSplash, 600, 400);
        MFXThemeManager.addOn(scene, Themes.DEFAULT);
        loadStylesheet("frontend/css/Default.css");
        this.primaryStage.setScene(scene);
        this.primaryStage.show();


        splashController.setOnConnection(pdb -> Platform.runLater(() -> loadUI(pdb)));
        splashController.getConnection();
    }

    private void loadUI(PdbController pdb) {
        if (pdb == null) {
            log.error("No database connection");
            return;
        }
        log.info("Application started with database {}", pdb.source);
        this.pdb = pdb;
        facade = new Facade(pdb);
        try {
            final BorderPane loadedLayout = loadWithCache("frontend/layouts/AppLayout.fxml");
            final LayoutController layoutController = loader.getController();

            viewPane = layoutController.getViewPane();

            scene = new Scene(loadedLayout, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
            navigate(Screen.LOGIN);
            MFXThemeManager.addOn(scene, Themes.DEFAULT);
            loadStylesheet("frontend/css/Default.css");
            new Thread(this::initDatabaseTables).start();
        } catch (IOException e) {
            log.error("Failed to load application ", e);
            throw new RuntimeException(e);
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

    public <T> T loadWithCache(String path) throws IOException {
        return loadWithCache(path, null, null);
    }

    public <T> T loadWithCache(String path, Object controller) throws IOException {
        return loadWithCache(path, null, controller);
    }

    public <T> T loadWithCache(String path, Object root, Object controller) throws IOException {
        var resource = App.class.getResource(path);
        if(resource == null){
            log.error("Could not find file {}", path);
            throw new IOException("No such file");
        }
        loader = new FXMLLoader();
        loader.setRoot(root);
        loader.setController(controller);
        loader.setLocation(resource);
        return loader.load();
    }
}
