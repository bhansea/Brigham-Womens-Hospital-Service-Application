package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import edu.wpi.punchy_pegasi.schema.Account;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Objects;

public class PFXAccount extends PFXIcon implements PropertyChangeListener {
    private final Image defaultImage = new Image(Objects.requireNonNull(App.class.getResourceAsStream("frontend/assets/bwhlogo.png")));
    private final EventHandler<? super MouseEvent> noAccount = e -> App.getSingleton().navigate(Screen.LOGIN);

    public PFXAccount() {
        super(MaterialSymbols.ACCOUNT_CIRCLE);
        setAccount(App.getSingleton().getAccount());
        App.getSingleton().addPropertyChangeListener(this);
    }
    public void setAccount(Account account) {
        if (account.getAccountType() == Account.AccountType.NONE) {
            addEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            setIcon(MaterialSymbols.LOGIN);
        } else {
            removeEventFilter(MouseEvent.MOUSE_CLICKED, noAccount);
            setIcon(MaterialSymbols.ACCOUNT_CIRCLE);
        }
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "account")) {
            setAccount((Account) evt.getNewValue());
        }
    }
}
