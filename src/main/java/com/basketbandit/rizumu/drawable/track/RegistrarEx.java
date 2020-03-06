package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class RegistrarEx extends Rectangle {
    private Color color = Colours.BLUE_25;

    public RegistrarEx() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYFromBottom() - 10, Configuration.getWidth(), Configuration.getDefaultNoteHeight() + 20);
    }

    public Color getColor() {
        return color;
    }
}
