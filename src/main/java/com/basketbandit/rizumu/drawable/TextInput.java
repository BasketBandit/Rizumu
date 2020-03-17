package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.input.MouseMovementAdapter;

import java.awt.*;

public class TextInput extends Button {
    private Color color = new Color(50, 50,50, 100);
    private StringBuilder text = new StringBuilder();
    private Rectangle innerBounds;

    public TextInput(int x, int y, int width, int height, int pad) {
        super(x, y, width, height);
        innerBounds = new Rectangle(x + pad/2, y + pad/2, width - pad, height - pad);
    }

    public Rectangle getInnerBounds() {
        return innerBounds;
    }

    public TextInput setText(String text) {
        this.text = new StringBuilder();
        this.text.append(text);
        return this;
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

    /**
     * Determines if the mouse's x, y coordinates are within the bounds of the button.
     * Requires {@link MouseMovementAdapter} to function correctly. (kinda bad practice?)
     * @return boolean
     */
    public boolean isHovered() {
        return getBounds().contains(MouseMovementAdapter.getX(), MouseMovementAdapter.getY());
    }
}
