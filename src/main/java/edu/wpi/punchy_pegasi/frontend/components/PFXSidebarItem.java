package edu.wpi.punchy_pegasi.frontend.components;

import edu.wpi.punchy_pegasi.App;
import edu.wpi.punchy_pegasi.frontend.Screen;
import edu.wpi.punchy_pegasi.frontend.icons.MaterialSymbols;
import edu.wpi.punchy_pegasi.frontend.icons.PFXIcon;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;

import java.util.List;

@Getter
public class PFXSidebarItem extends HBox {
    private static final PseudoClass SELECTED_PSEUDO_CLASS = PseudoClass.getPseudoClass("selected");
    private static final PseudoClass EXPANDED_PSEUDO_CLASS = PseudoClass.getPseudoClass("expanded");
    private final ObjectProperty<MaterialSymbols> icon = new SimpleObjectProperty<>();
    private final StringProperty text = new SimpleStringProperty();
    private final BooleanProperty selected = new SimpleBooleanProperty();
    private final BooleanProperty expanded = new SimpleBooleanProperty();
    private boolean dropdown;
    private String dropdownText;
    private final HBox container = new HBox();
    private final PFXIcon pfxIcon = new PFXIcon();
    private VBox expandedInfo = new VBox();
    private Screen screen;
    private ObservableList<Screen> dropdownItems;


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
        this.dropdown = false;
        this.screen = screen;
        setIcon(icon);
        setText(screen.getReadable());
        this.dropdownText = "";
        normal();
        setSelected(false);
    }

    public PFXSidebarItem(String text, MaterialSymbols icon, List<Screen> dropdownItems) {
        this();
        this.dropdown = true;
        this.screen = null;
        setIcon(icon);
        setText(text);
        this.dropdownText = text;
        this.dropdownItems = FXCollections.observableList(dropdownItems);
        dropdown();
        setSelected(false);
    }

    public VBox getExpandedInfo() {
        return this.expandedInfo;
    }

    public void setExpandedInfo(VBox expandedInfo) {
        this.expandedInfo = expandedInfo;
    }

    public final ObjectProperty<MaterialSymbols> iconProperty() {
        return icon;
    }

    public final MaterialSymbols getIcon() {
        return icon.get();
    }

    public final void setIcon(MaterialSymbols icon) {
        pfxIcon.setIcon(icon);
        this.icon.set(icon);
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

    private void normal() {
        setOnMouseClicked(e -> App.getSingleton().navigate(screen));
    }

    private void dropdown() {
        normal();

//        var vbox = new VBox();
//        getChildren().add(vbox);
//        vbox.setAlignment(Pos.CENTER_LEFT);
//        vbox.setSpacing(10);
//        HBox.setHgrow(vbox, Priority.ALWAYS);
//        var serviceRequestDropDown = new VBox();
//        serviceRequestDropDown.setAlignment(Pos.CENTER_LEFT);
//        serviceRequestDropDown.setSpacing(5);
//        serviceRequestDropDown.setPadding(new Insets(0, 0, 0, 65));
//        for (var item : dropdownItems) {
//            var label2 = new Label(item.getReadable().replaceAll("[Rr]equest", "").trim());
//            label2.setTextFill(Color.valueOf("#FFFFFF"));
//            label2.setOpacity(0.5);
//            label2.setOnMouseEntered(e -> label2.setOpacity(1.0));
//            label2.setOnMouseExited(e -> label2.setOpacity(.5));
//            label2.setOnMouseClicked(e -> App.getSingleton().navigate(item));
//            serviceRequestDropDown.getChildren().add(label2);
//        }
//        vbox.getChildren().addAll(this, serviceRequestDropDown);
//        serviceRequestDropDown.setVisible(false);
//        serviceRequestDropDown.setManaged(false);
//
//        onMouseClickedProperty().set(e -> {
//            if (serviceRequestDropDown.isVisible()) {
//                serviceRequestDropDown.setVisible(false);
//                serviceRequestDropDown.setManaged(false);
//            } else {
//                serviceRequestDropDown.setVisible(true);
//                serviceRequestDropDown.setManaged(true);
//            }
//        });
    }
}