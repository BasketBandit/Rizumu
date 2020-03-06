package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;

import java.awt.*;

public class ExtendedRegistrar extends Rectangle {
    private Color color = new Color(100, 0,0, 50);

    public ExtendedRegistrar() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYFromBottom() + 45, Configuration.getWidth(), (Configuration.getHeight() - Configuration.getDefaultRegistrarYFromBottom()));
    }

    public Color getColor() {
        return color;
    }
}
