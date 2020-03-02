package com.basketbandit.rizumu.input;

import com.basketbandit.rizumu.Rizumu;

import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class MouseListeners {
    private static final HashMap<String, MouseAdapter> mouseListeners = new HashMap<>();

    public static void setMouseListener(String identifier, MouseAdapter mouseListener) {
        mouseListeners.values().forEach(Rizumu::removeMouseListener);
        mouseListeners.clear();

        mouseListeners.put(identifier, mouseListener);
        Rizumu.addMouseListener(mouseListener);
    }
}
