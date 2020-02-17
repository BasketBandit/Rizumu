package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.Configuration;

import java.awt.*;

public class Registrar extends Rectangle {
    private Color color = Color.WHITE;

    public Registrar() {
        super(0, 600, Configuration.getContentWidth(), 25);
    }

    public Color getColor() {
        return color;
    }
}
