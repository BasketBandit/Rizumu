package com.basketbandit.rizumu.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {

    private static final int numButtons = 10;
    private static final boolean[] buttons = new boolean[numButtons];
    private static final boolean[] lastButtons = new boolean[numButtons];
    private static int x;
    private static int y;

    @Override
    public void mousePressed(MouseEvent e) {
        buttons[e.getButton()] = true;
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        buttons[e.getButton()] = false;
        x = -1;
        y = -1;
    }

    public static boolean isPressed(int buttonCode) {
        return buttons[buttonCode];
    }

    public static int getX() {
        return x;
    }
    public static int getY() {
        return y;
    }
}
