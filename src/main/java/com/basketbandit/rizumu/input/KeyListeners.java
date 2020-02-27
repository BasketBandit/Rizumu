package com.basketbandit.rizumu.input;

import com.basketbandit.rizumu.Rizumu;

import java.awt.event.KeyAdapter;
import java.util.HashMap;

public class KeyListeners {
    private static final HashMap<String, KeyAdapter> keyListeners = new HashMap<>();

    public static void setKeyListener(String identifier, KeyAdapter keyListener) {
        keyListeners.values().forEach(Rizumu::removeKeyListener);
        keyListeners.clear();

        keyListeners.put(identifier, keyListener);
        Rizumu.addKeyListener(keyListener);
    }
}
