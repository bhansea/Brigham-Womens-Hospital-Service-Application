package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class PFXAccount extends PFXSidebarItem implements PropertyChangeListener {
    private final Label nameText = new Label();
    private final Label roleText = new Label();
    private final Label loginText = new Label("Login");
    private final Image defaultImage = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
    private final EventHandler<? super MouseEvent> noAccount = e -> App.getSingleton().navigate(Screen.LOGIN);

    public PFXAccount() {
        super(MaterialSymbols.ACCOUNT_CIRCLE);
        setAccount(App.getSingleton().getAccount());
        App.getSingleton().addPropertyChangeListener(this);
        nameText.textOverrunProperty().set(OverrunStyle.CLIP);
        roleText.textOverrunProperty().set(OverrunStyle.CLIP);
        loginText.textOverrunProperty().set(OverrunStyle.CLIP);
    }

    public void setAccount(Account account) {
        if (account.getAccountType() == Account.AccountType.NONE) {
            addEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            setIcon(MaterialSymbols.ACCOUNT_CIRCLE);
            getExpandedInfo().getChildren().clear();
            getExpandedInfo().getChildren().add(loginText);
        } else {
            removeEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            getExpandedInfo().getChildren().clear();
            getExpandedInfo().getChildren().addAll(nameText);
            nameText.setText(account.getUsername());
            setIcon(MaterialSymbols.MOOD);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }
}
