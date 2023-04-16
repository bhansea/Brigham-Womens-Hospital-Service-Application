package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXSidebarItem;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class SidebarController extends BorderPane implements PropertyChangeListener {
    @FXML
    HBox loginLogout;
    @FXML
    ImageView profileImage;
    @FXML
    Label nameText;
    @FXML
    Label roleText;
    @FXML
    private VBox sidebar;
    @FXML
    private Label loginLogoutText;
    @FXML
    private VBox accountInfo;

    private final ObservableList<PFXSidebarItem> sidebarItems = FXCollections.observableArrayList(
            new PFXSidebarItem(Screen.HOME, MaterialSymbols.HOME),
            new PFXSidebarItem("Service Requests", MaterialSymbols.WORK, Arrays.stream(Screen.values()).filter(v -> v.name().toLowerCase().contains("request")).toList()),
            new PFXSidebarItem(Screen.MAP_PAGE, MaterialSymbols.MAP),
            new PFXSidebarItem(Screen.ADMIN_PAGE, MaterialSymbols.ADMIN_PANEL_SETTINGS),
            new PFXSidebarItem(Screen.EDIT_MAP_PAGE, MaterialSymbols.REBASE_EDIT),
            new PFXSidebarItem(Screen.SIGNAGE, MaterialSymbols.SIGNPOST)
    );

    public SidebarController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/components/Sidebar.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void reload() {
        try {
            App.getSingleton().loadWithCache("frontend/components/Sidebar.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    private BorderPane root;

    private void setAccount(Account account) {
        sidebarItems.forEach(s -> {
            if (s.isDropdown()) {
                var min = s.getDropdownItems().stream().mapToInt(s2 -> s2.getShield().getShieldLevel()).min();
                if (min.isEmpty()) return;
                if (min.getAsInt() > account.getAccountType().getShieldLevel()) {
                    s.setVisible(false);
                    s.setManaged(false);
                } else {
                    s.setVisible(true);
                    s.setManaged(true);
                    s.getDropdownItems().forEach(dI -> {
                        if (account.getAccountType().getShieldLevel() < dI.getShield().getShieldLevel()) {
                            //hide
                        } else {
                            //show
                        }
                    });
                }
            } else {
                var screen = s.getScreen();
                if (screen == null) return;
                if (account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()) {
                    s.setVisible(true);
                    s.setManaged(true);
                } else {
                    s.setVisible(false);
                    s.setManaged(false);
                }
            }
        });

        if (account.getAccountType() == Account.AccountType.NONE) {
            accountInfo.setManaged(false);
            accountInfo.setVisible(false);
            loginLogoutText.setText("Login");
            return;
        }

        accountInfo.setManaged(true);
        accountInfo.setVisible(true);
        loginLogoutText.setText("Logout");
        nameText.setText(account.getUsername());
        roleText.setText(account.getAccountType().name());
    }

    private void setSelected(PFXSidebarItem item) {
        sidebarItems.forEach(d -> d.setSelected(d == item));
    }

    public void initialize() {
        App.getSingleton().addPropertyChangeListener(this);
        setAccount(App.getSingleton().getAccount());

        Image defaultImage = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
        profileImage.imageProperty().set(defaultImage);

        sidebar.getChildren().addAll(0, sidebarItems);
        sidebarItems.forEach(s -> s.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> setSelected(s)));

        loginLogout.setOnMouseClicked(e -> {
            if (App.getSingleton().getAccount().getAccountType() == Account.AccountType.NONE)
                App.getSingleton().navigate(Screen.LOGIN);
            else
                App.getSingleton().setAccount(null);
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }

    @FXML
    private void exit() {
        App.getSingleton().exit();
    }
}

