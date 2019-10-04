package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class Button extends Rectangle {
    private Color color = new Color(200, 0,0, 50);

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }
}
