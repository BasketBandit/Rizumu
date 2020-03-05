package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.input.MouseMovementAdapter;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class Button extends Rectangle {
    private String buttonText;

    private Color color = Colours.DARK_GREY;

    public Button(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public String getButtonText() {
        return buttonText;
    }

    public Color getColor() {
        return color;
    }

    public Button setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    public Button setColor(Color color) {
        this.color = color;
        return this;
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
