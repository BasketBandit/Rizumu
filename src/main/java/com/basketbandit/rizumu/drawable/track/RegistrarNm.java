package com.basketbandit.rizumu.drawable.track;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class RegistrarNm extends Rectangle {
    private Color color = Colours.BLUE_50;

    public RegistrarNm() {
        super(Configuration.getDefaultBeatmapXPosition(), Configuration.getDefaultRegistrarYPosition() - 20, Configuration.getContentWidth(), Configuration.getDefaultNoteHeight() + 40);
    }

    public Color getColor() {
        return color;
    }
}
