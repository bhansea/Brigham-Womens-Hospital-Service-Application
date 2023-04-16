package edu.wpi.punchy_pegasi.frontend.components;

import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.scene.layout.BorderPane;

public class PageLoading extends BorderPane {
    public PageLoading() {
        super(new MFXProgressSpinner(-1));
    }
}
