package edu.wpi.punchy_pegasi;

import edu.wpi.punchy_pegasi.backend.PdbController;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHolder;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardHorizontal;
import edu.wpi.punchy_pegasi.frontend.components.PFXCardVertical;
import edu.wpi.punchy_pegasi.frontend.components.PageLoading;
import edu.wpi.punchy_pegasi.frontend.controllers.ErrorController;
import edu.wpi.punchy_pegasi.frontend.controllers.LayoutController;
import edu.wpi.punchy_pegasi.frontend.controllers.SplashController;
import edu.wpi.punchy_pegasi.generated.Facade;
import edu.wpi.punchy_pegasi.schema.Account;
import edu.wpi.punchy_pegasi.schema.TableType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@Slf4j
public class App extends Application {
    @Getter
    private static App singleton;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    @Getter
    private final Stage popupStage = new Stage();
    private final List<LoadedFXML> loadedFXML = new ArrayList<>();
    private final Debouncer<String> fxmlDebouncer = new Debouncer<>(s -> {
        loadedFXML.stream().filter(l -> l.path.toExternalForm().endsWith(s)).forEach(fxml -> Platform.runLater(() -> {
            try {
                if (fxml.checkFXML())
                    fxml.load();
                else System.out.println("Failed to hot-load " + s + ", invalid XML");
            } catch (IOException ignored) {
            }
        }));
        System.out.println("Hot-loaded FXML: " + s);
    }, 250);
    @Getter
    private PdbController pdb;
    @Getter
    private Facade facade;
    @Getter
    private Stage primaryStage;
    @Getter
    private Screen currentScreen;
    @Getter
    private Scene scene;
    @Getter
    private Account account = new Account("", "", 0L, Account.AccountType.NONE);

    @Getter
    private LayoutController layout;
    @Getter
    private boolean development = false;
    private final Debouncer<String> cssDebouncer = new Debouncer<>(s -> {
        Platform.runLater(() -> {
            scene.getStylesheets().clear();
            loadStylesheet("frontend/css/DefaultTheme.css");
        });
        System.out.println("Hot-loaded CSS: " + s);
    }, 250);
    @Getter
    private BorderPane viewPane;
    private Thread loadingThread = null;

    private static void showError(Thread t, Throwable e) {
        log.error("An unexpected error occurred in " + t, e);
        if (Platform.isFxApplicationThread()) showErrorDialog(e);
    }

    private static void showErrorDialog(Throwable e) {
        var errorMsg = new StringWriter();
        e.printStackTrace(new PrintWriter(errorMsg));
        var dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        var loader = new FXMLLoader(App.class.getResource("frontend/components/Error.fxml"));
        try {
            Parent root = loader.load();
            ((ErrorController) loader.getController()).setErrorText(errorMsg.toString());
            dialog.setScene(new Scene(root, 600, 400));
            dialog.show();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public void setAccount(Account account) {
        if (account == null) {
            navigate(Screen.LOGIN);
            account = new Account("", "", 0L, Account.AccountType.NONE);
        }
        support.firePropertyChange("account", this.account, account);
        this.account = account;
    }

    public void exit() {
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
    public void stop() {
        log.info("Shutting Down");
    }

    public synchronized void navigate(final Screen screen) {
        if (screen == null) return;
        if (loadingThread != null) loadingThread.interrupt();
        if (account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()) {
            getViewPane().setCenter(new PageLoading());
            loadingThread = new Thread(() -> {
                var loaded = screen.get();
                if (!Thread.interrupted())
                    Platform.runLater(() -> {
                        setCurrentScreen(screen);
                        getViewPane().setCenter(loaded);
                    });
            });
            loadingThread.setDaemon(true);
            loadingThread.start();
        }
    }

    public Optional<URL> resolveResource(String resourcePath) {
        if (development) {
            try {
                var resource = Paths.get(System.getProperty("user.dir"), "src/main/resources/edu/wpi/punchy_pegasi", resourcePath);
                if (Files.exists(resource)) return Optional.of(resource.toUri().toURL());
            } catch (MalformedURLException e) {
                return Optional.empty();
            }
        } else {
            var resource = App.class.getResource(resourcePath);
            if (resource != null) return Optional.of(resource);
        }
        return Optional.empty();
    }

    public void loadStylesheet(String resourcePath) {
        var resource = resolveResource(resourcePath);
        resource.ifPresent(url -> scene.getStylesheets().add(url.toExternalForm()));
    }

    private void registerRecursive(WatchService watchService, final Path root) throws IOException {
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                dir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private void loadUI(PdbController pdb) {
        if (pdb == null) {
            log.error("No database connection");
            return;
        }
        log.info("Application started with database {}", pdb.source);
        this.pdb = pdb;
        facade = new Facade(pdb);
        layout = new LayoutController();
        viewPane = layout.getViewPane();
        scene = new Scene(layout, 1280, 720);
        loadStylesheet("frontend/css/DefaultTheme.css");
        primaryStage.setScene(scene);
        primaryStage.show();
        navigate(Screen.LOGIN);
        new Thread(this::initDatabaseTables).start();
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

    @Override
    public void start(Stage primaryStage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(App::showError);
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Brigham and Women's Hospital");
        var genericResource = this.getClass().getResource("");
        if (genericResource != null && Objects.equals(genericResource.getProtocol(), "file")) development = true;

        final var loader = loadWithCache("frontend/views/Splash.fxml");
        final SplashController splashController = loader.getController();
        scene = new Scene(loader.getRoot(), 600, 400);
        splashController.setOnConnection(pdb -> Platform.runLater(() -> loadUI(pdb)));
        splashController.getConnection();

        loadStylesheet("frontend/css/DefaultTheme.css");

        if (development) {
            var hotloadCSS = new Thread(() -> {
                try {
                    WatchService watchService = FileSystems.getDefault().newWatchService();
                    Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/edu/wpi/punchy_pegasi/frontend");

                    registerRecursive(watchService, path);

                    WatchKey key;
                    while ((key = watchService.take()) != null) {
                        for (WatchEvent<?> event : key.pollEvents()) {
                            var context = event.context().toString().replace("~", "");
                            if (context.contains(".css")) cssDebouncer.call(context);
                            if (context.contains(".fxml")) fxmlDebouncer.call(context);
                        }
                        key.reset();
                    }
                } catch (Exception e) {
                    log.error("Error with hotloading css", e);
                }
            });
            hotloadCSS.setDaemon(true);
            hotloadCSS.start();
        }

        this.primaryStage.setScene(scene);
        this.primaryStage.show();


        //splashController.setOnConnection(pdb -> Platform.runLater(() -> loadUI(pdb)));
        //splashController.getConnection();
    }

    public FXMLLoader loadWithCache(String path) throws IOException {
        return loadWithCache(path, null, null);
    }

    public FXMLLoader loadWithCache(String path, Object controller) throws IOException {
        return loadWithCache(path, null, controller);
    }

    public FXMLLoader loadWithCache(String path, Node root, Object controller) throws IOException {
        var resource = resolveResource(path);
        if (resource.isEmpty()) {
            log.error("Could not find file {}", path);
            throw new IOException("No such file");
        }
        var loaded = new LoadedFXML(resource.get(), root, controller);
        loadedFXML.add(loaded);
        return loaded.load();
    }

    private static class LoadedFXML {
        private final URL path;
        @Getter
        private final FXMLLoader fxmlLoader;

        public LoadedFXML(URL path, Node root, Object controller) {
            fxmlLoader = new FXMLLoader();
            fxmlLoader.setRoot(root);
            fxmlLoader.setController(controller);
            fxmlLoader.setLocation(path);
            this.path = path;
        }

        public boolean checkFXML() {
            try {
                File fXmlFile = new File(path.toURI());
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                return true;
            } catch (ParserConfigurationException | IOException | SAXException | URISyntaxException e) {
                return false;
            }
        }

        public FXMLLoader load() throws IOException {
            fxmlLoader.load();
            return fxmlLoader;
        }
    }
}