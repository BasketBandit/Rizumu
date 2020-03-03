package com.basketbandit.rizumu.drawable;

import java.awt.*;

public class TextLine extends Rectangle {
    private Color color = new Color(50, 50,50, 100);
    private StringBuilder text = new StringBuilder();
    private Rectangle innerBounds;

    public TextLine(int x, int y, int width, int height, int pad) {
        super(x, y, width, height);
        innerBounds = new Rectangle(x + pad/2, y + pad/2, width - pad, height - pad);
    }

    public Rectangle getInnerBounds() {
        return innerBounds;
    }

    public void setText(String text) {
        this.text = new StringBuilder();
        this.text.append(text);
    }

    public void append(String text) {
        this.text.append(text);
    }

    public void deleteChar() {
        this.text.deleteCharAt(text.length() - 1);
    }

    public String getText() {
        return text.toString();
    }
}
