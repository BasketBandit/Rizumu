package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class Registrar extends Rectangle {
    private Color color = Color.WHITE;

    public Registrar() {
        super(0, 600, 1280, 25);
    }

    public Color getColor() {
        return color;
    }
}
