package com.basketbandit.rizumu.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseMovementAdapter extends MouseAdapter {
    private static int x;
    private static int y;

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y = e.getY();
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }
}
