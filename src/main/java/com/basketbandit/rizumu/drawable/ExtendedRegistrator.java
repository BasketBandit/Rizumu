package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class ExtendedRegistrator extends Rectangle {
    private Color color = new Color(100, 0,0, 50);

    public ExtendedRegistrator() {
        super(0, 585, 1280, 150);
    }

    public Color getColor() {
        return color;
    }
}
