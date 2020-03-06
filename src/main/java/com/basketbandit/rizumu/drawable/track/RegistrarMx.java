package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class RegistrarMx extends Rectangle {
    private Color color = Colours.MEDIUM_GREY;

    public RegistrarMx() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYFromBottom(), Configuration.getWidth(), Configuration.getDefaultNoteHeight());
    }

    public Color getColor() {
        return color;
    }
}
