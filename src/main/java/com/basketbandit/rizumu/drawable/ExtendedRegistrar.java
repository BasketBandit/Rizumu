package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class ExtendedRegistrar extends Rectangle {
    private Color color = new Color(100, 0,0, 50);

    public ExtendedRegistrar() {
        super(0, 585, 1280, 150);
    }

    public Color getColor() {
        return color;
    }
}
