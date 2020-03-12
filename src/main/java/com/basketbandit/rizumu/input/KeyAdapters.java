package com.basketbandit.rizumu.input;

import com.basketbandit.rizumu.Engine;

import java.awt.event.KeyAdapter;
import java.util.HashMap;

public class KeyAdapters {
    private static final HashMap<String, KeyAdapter> keyAdapters = new HashMap<>();

    public static void setKeyAdapter(String identifier, KeyAdapter keyAdapter) {
        keyAdapters.values().forEach(Engine::removeKeyAdapter);
        keyAdapters.clear();

        keyAdapters.put(identifier, keyAdapter);
        Engine.addKeyAdapter(keyAdapter);
    }
}
