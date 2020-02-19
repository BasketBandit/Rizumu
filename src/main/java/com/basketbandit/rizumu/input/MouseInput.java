package com.basketbandit.rizumu.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseInput extends MouseAdapter {
    private static final int numButtons = 10;
    private static final boolean[] buttons = new boolean[numButtons];
    private static final boolean[] lastButtons = new boolean[numButtons];
    private static int x;
    private static int y;
    private static int mouseWheelRotation;
    private static int mouseWheelScrollUnits;

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

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseWheelRotation = e.getWheelRotation();
        mouseWheelScrollUnits = e.getUnitsToScroll();
    }

    public static boolean isPressed(int buttonCode) {
        return buttons[buttonCode];
    }

    public static void resetMouseWheel() {
        mouseWheelRotation = 0;
        mouseWheelScrollUnits = 0;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static int getMouseWheelRotation() {
        return mouseWheelRotation;
    }

    public static int getMouseWheelScrollUnits() {
        return mouseWheelScrollUnits < 0 ? mouseWheelScrollUnits -1 : mouseWheelScrollUnits + 1; // default on my mouse seems a bit too little (needs further testing)
    }
}
