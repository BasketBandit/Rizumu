package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.input.MouseMovementAdapter;

import java.awt.*;

public class Button extends Rectangle {
    private String buttonText;
    private Color color = new Color(50, 50,50, 100);

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public Color getColor() {
        return color;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
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
