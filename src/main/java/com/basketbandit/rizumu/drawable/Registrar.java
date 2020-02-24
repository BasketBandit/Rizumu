package com.basketbandit.rizumu.drawable;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class Registrar extends Rectangle {
    private Color color = Colours.MEDIUM_GREY_100;

    public Registrar() {
        super(0, Configuration.getDefaultRegistrarYPosition(), Configuration.getContentWidth(), 20);
    }

    public Color getColor() {
        return color;
    }
}
