package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class PFXSidebarItem extends HBox {
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private static final PseudoClass EXPANDED_PSEUDO_CLASS = PseudoClass.getPseudoClass("expanded");
    private final StringProperty text = new SimpleStringProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty();
    private final BooleanProperty expanded = new SimpleBooleanProperty();
    private final HBox container = new HBox();
    private final PFXIcon pfxIcon = new PFXIcon();
    private String dropdownText;
    private VBox expandedInfo = new VBox();
    private Screen screen;

    public PFXSidebarItem() {
        super();
        getStyleClass().add("pfx-sidebar-container");
        getChildren().add(container);
        container.getStyleClass().add("pfx-sidebar-item");
        container.getChildren().addAll(pfxIcon, expandedInfo);
        var label = new Label();
        label.textOverrunProperty().set(OverrunStyle.CLIP);
        getExpandedInfo().getChildren().add(label);
        label.textProperty().bind(this.textProperty());
        setExpanded(true);
    }

    public PFXSidebarItem(MaterialSymbols icon) {
        super();
        getStyleClass().add("pfx-sidebar-container");
        getChildren().add(container);
        container.getStyleClass().add("pfx-sidebar-item");
        container.getChildren().addAll(pfxIcon, expandedInfo);
        setExpanded(true);
    }

    public PFXSidebarItem(Screen screen, MaterialSymbols icon) {
        this();
        this.screen = screen;
        setIcon(icon);
        setText(screen.getReadable());
        this.dropdownText = "";
        setOnMouseClicked(e -> App.getSingleton().navigate(screen));
        setSelected(false);
    }

    public VBox getExpandedInfo() {
        return this.expandedInfo;
    }

    public void setExpandedInfo(VBox expandedInfo) {
        this.expandedInfo = expandedInfo;
    }

    public final ObjectProperty<MaterialSymbols> iconProperty() {
        return pfxIcon.iconProperty();
    }

    public final MaterialSymbols getIcon() {
        return pfxIcon.getIcon();
    }
    public final void setIcon(MaterialSymbols icon) {
        pfxIcon.setIcon(icon);
    }
    public final StringProperty textProperty() {
        return text;
    }

    public final String getText() {
        return text.get();
    }

    public final void setText(String text) {
        this.text.set(text);
    }

    public BooleanProperty selectedProperty() {
        return this.selected;
    }

    public boolean getSelected() {
        return this.selected.get();
    }

    public void setSelected(boolean value) {
        pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, value);
        container.pseudoClassStateChanged(SELECTED_PSEUDO_CLASS, value);
        pfxIcon.setOutlined(!value);
        this.selected.set(value);
    }

    public BooleanProperty expandedProperty() {
        return this.expanded;
    }

    public boolean getExpanded() {
        return this.expanded.get();
    }

    public void setExpanded(boolean value) {
        pseudoClassStateChanged(EXPANDED_PSEUDO_CLASS, value);
        container.pseudoClassStateChanged(EXPANDED_PSEUDO_CLASS, value);
        expandedInfo.setVisible(value);
        expandedInfo.setManaged(value);
        this.expanded.set(value);
    }
}