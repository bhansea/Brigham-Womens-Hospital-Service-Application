package edu.wpi.punchy_pegasi.frontend.icons;

import javafx.beans.property.*;
import javafx.css.PseudoClass;
import javafx.scene.text.Text;

public class PFXIcon extends Text {
    private static final PseudoClass OUTLINED_PSEUDO_CLASS = PseudoClass.getPseudoClass("outlined");
    private final ObjectProperty<MaterialSymbols> icon = new SimpleObjectProperty<>();
    private final DoubleProperty size = new SimpleDoubleProperty();
    private final BooleanProperty outlined = new SimpleBooleanProperty();

    public PFXIcon() {
        super();
        this.getStyleClass().add("pfx-icon");
    }

    public PFXIcon(MaterialSymbols icon) {
        this();
        this.setIcon(icon);
    }

    public PFXIcon(MaterialSymbols icon, double size) {
        this();
        this.setIcon(icon);
        this.setSize(size);
    }

    public final ObjectProperty<MaterialSymbols> iconProperty() {
        return icon;
    }

    public final MaterialSymbols getIcon() {
        return icon.get();
    }

    public final void setIcon(MaterialSymbols icon) {
        this.setText(icon.getUnicode());
        this.icon.set(icon);
    }

    public final DoubleProperty sizeProperty() {
        return size;
    }

    public final Double getSize() {
        return size.get();
    }

    public final void setSize(Double size) {
        this.setStyle("-fx-font-size: " + size);
        this.size.set(size);
    }

    public final BooleanProperty outlinedProperty(){
        return outlined;
    }

    public final boolean getOutlined() {
        return outlined.get();
    }

    public final void setOutlined(boolean outlined) {
        this.pseudoClassStateChanged(OUTLINED_PSEUDO_CLASS, outlined);
        this.outlined.set(outlined);
    }
}
