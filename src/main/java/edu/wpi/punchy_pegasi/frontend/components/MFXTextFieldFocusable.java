package edu.wpi.punchy_pegasi.frontend.components;

import io.github.palexdev.materialfx.controls.MFXTextField;

public class MFXTextFieldFocusable extends MFXTextField {

    public MFXTextFieldFocusable() {
        super();
    }

    @Override
    public void requestFocus() {
        super.requestFocus();
        boundField.requestFocus();
    }
}