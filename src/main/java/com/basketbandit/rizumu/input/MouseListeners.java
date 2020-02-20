package com.basketbandit.rizumu.input;

import com.basketbandit.rizumu.Rizumu;

import java.awt.event.MouseListener;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;

public class MouseListeners {
    private static final HashMap<String, MouseListener> mouseListeners = new HashMap<>();

    public static void setMouseListener(String identifier, MouseListener mouseListener) {
        mouseListeners.values().forEach(listener -> {
            Rizumu.removeMouseListener(listener);
            Rizumu.removeMouseWheelListener((MouseWheelListener) listener);
        });
        mouseListeners.clear();

        mouseListeners.put(identifier, mouseListener);
        Rizumu.addMouseListener(mouseListener);
        Rizumu.addMouseWheelListener((MouseWheelListener) mouseListener);
    }
}
