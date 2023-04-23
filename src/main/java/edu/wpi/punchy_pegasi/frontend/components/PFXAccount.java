package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class PFXAccount extends HBox implements PropertyChangeListener {
    private final Image defaultImage = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
    private final EventHandler<? super MouseEvent> noAccount = e -> App.getSingleton().navigate(Screen.LOGIN);
    private final Label label = new Label("Login");
    private final PFXIcon loginIcon = new PFXIcon(MaterialSymbols.LOGIN);
    private final PFXSidebarItem logout = new PFXSidebarItem();
    private final PopOver accountMenu = new PopOver();
    private final EventHandler<? super MouseEvent> isAccount = e -> accountMenu.show(this);
    private final PFXIcon defaultIcon = new PFXIcon(MaterialSymbols.ACCOUNT_CIRCLE);

    public PFXAccount() {
        super();
        var content = new VBox(logout);
        content.getStyleClass().add("pfx-account-menu");
        accountMenu.setContentNode(content);
        accountMenu.setDetachable(false);
        accountMenu.setArrowLocation(PopOver.ArrowLocation.TOP_RIGHT);
        logout.setText("Logout");
        logout.setIcon(MaterialSymbols.LOGOUT);
        logout.setOnMouseClicked(e -> {
            App.getSingleton().setAccount(null);
            accountMenu.hide();
        });
        defaultIcon.getStyleClass().add("pfx-default-account-icon");
        getStyleClass().add("pfx-account");
        setAccount(App.getSingleton().getAccount());
        App.getSingleton().addPropertyChangeListener(this);
    }

    public void setAccount(Account account) {
        if (account.getAccountType() == Account.AccountType.NONE) {
            removeEventFilter(MouseEvent.MOUSE_CLICKED, isAccount);
            addEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            getChildren().clear();
            getChildren().addAll(loginIcon, label);
        } else {
            removeEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            addEventFilter(MouseEvent.MOUSE_CLICKED, isAccount);
            getChildren().clear();
            getChildren().addAll(defaultIcon);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }


}
