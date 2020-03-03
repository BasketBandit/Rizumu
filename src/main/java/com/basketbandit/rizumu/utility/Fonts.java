package com.basketbandit.rizumu.utility;

import java.awt.*;

public class Fonts {
    private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static Font[] fonts = ge.getAllFonts();

    public static final Font default12 = fonts[368].deriveFont(12.0f);
    public static final Font default24 = fonts[368].deriveFont(24.0f);
    public static final Font default36 = fonts[368].deriveFont(36.0f);
}
