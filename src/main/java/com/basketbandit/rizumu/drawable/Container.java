package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class Container extends Rectangle {
    private Color color = new Color(50, 50,50, 100);

    public Container(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public Container setColor(Color color) {
        this.color = color;
        return this;
    }
}
