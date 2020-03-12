package com.basketbandit.rizumu.input;

import com.basketbandit.rizumu.Engine;

import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class MouseAdapters {
    private static final HashMap<String, MouseAdapter> mouseAdapters = new HashMap<>();

    public static void setMouseAdapter(String identifier, MouseAdapter mouseAdapter) {
        mouseAdapters.values().forEach(Engine::removeMouseAdapter);
        mouseAdapters.clear();

        mouseAdapters.put(identifier, mouseAdapter);
        Engine.addMouseAdapter(mouseAdapter);
    }
}
