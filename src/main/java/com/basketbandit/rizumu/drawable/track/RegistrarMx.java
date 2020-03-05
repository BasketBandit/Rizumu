package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class RegistrarMx extends Rectangle {
    private Color color = Colours.MEDIUM_GREY;

    public RegistrarMx() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYPosition(), Configuration.getContentWidth(), Configuration.getDefaultNoteHeight());
    }

    public Color getColor() {
        return color;
    }
}
