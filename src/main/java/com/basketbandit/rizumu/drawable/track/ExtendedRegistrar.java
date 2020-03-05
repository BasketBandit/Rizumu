package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;

import java.awt.*;

public class ExtendedRegistrar extends Rectangle {
    private Color color = new Color(100, 0,0, 50);

    public ExtendedRegistrar() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYPosition() + 40, Configuration.getContentWidth(), (Configuration.getHeight() - Configuration.getDefaultRegistrarYPosition()));
    }

    public Color getColor() {
        return color;
    }
}
