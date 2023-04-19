package edu.wpi.punchy_pegasi.frontend.controllers;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.ChangeSize;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.components.PFXAccount;
import edu.wpi.punchy_pegasi.frontend.components.PFXSidebarItem;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Objects;

public class SidebarController extends VBox implements PropertyChangeListener {
    private final ObservableList<PFXSidebarItem> sidebarItems = FXCollections.observableArrayList(
            new PFXSidebarItem(Screen.HOME, MaterialSymbols.HOME),
            new PFXSidebarItem(Screen.SERVICE_REQUEST, MaterialSymbols.WORK),
            new PFXSidebarItem(Screen.MAP_PAGE, MaterialSymbols.MAP),
            new PFXSidebarItem(Screen.ADMIN_PAGE, MaterialSymbols.ADMIN_PANEL_SETTINGS),
            new PFXSidebarItem(Screen.EDIT_MAP_PAGE, MaterialSymbols.REBASE_EDIT),
            new PFXSidebarItem(Screen.CONFERENCE_ROOM_SERVICE_REQUEST, MaterialSymbols.ROOM_SERVICE),
            new PFXSidebarItem(Screen.SIGNAGE, MaterialSymbols.SIGNPOST)
    );
    private final int maxWidth = 256;
    @FXML
    private PFXSidebarItem logout;
    private Boolean expanded = null;
    @FXML
    private BorderPane root;

    public SidebarController() {
        super();
        try {
            App.getSingleton().loadWithCache("frontend/components/Sidebar.fxml", this, this);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        setMaxWidth(getMinWidth());
        setExpanded(false);
        var clipper = new Rectangle();
        clipper.widthProperty().bind(widthProperty());
        clipper.heightProperty().bind(heightProperty());
        setClip(clipper);
        //pfxAccount.setOnMouseClicked(e -> setSelected(null));
        logout.setOnMouseClicked(e -> App.getSingleton().setAccount(null));
    }

    private void setAccount(Account account) {
        sidebarItems.forEach(s -> {
            var screen = s.getScreen();
            if (screen == null) return;
            if (account.getAccountType().getShieldLevel() >= screen.getShield().getShieldLevel()) {
                s.setVisible(true);
                s.setManaged(true);
            } else {
                s.setVisible(false);
                s.setManaged(false);
            }
        });
        var loggedIn = account.getAccountType() != Account.AccountType.NONE;
        logout.setVisible(loggedIn);
        logout.setManaged(loggedIn);
    }

    @FXML
    private void toggleExpand() {
        setExpanded(!expanded);
    }

    private synchronized void setExpanded(boolean expanded) {
        if (this.expanded != null && this.expanded == expanded) return;
        this.expanded = expanded;
        Platform.runLater(() ->
                ChangeSize.changeWidth(this, expanded ? maxWidth : getMinWidth(), e -> {
                })
        );
    }


    private void setSelected(PFXSidebarItem item) {
        sidebarItems.forEach(d -> d.setSelected(d == item));
    }

    public void initialize() {
        App.getSingleton().addPropertyChangeListener(this);
        setAccount(App.getSingleton().getAccount());

        getChildren().addAll(1, sidebarItems);
        sidebarItems.forEach(s -> s.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent e) -> setSelected(s)));
        setExpanded(false);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }
}

