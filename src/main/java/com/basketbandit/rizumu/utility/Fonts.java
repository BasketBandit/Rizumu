package com.basketbandit.rizumu.utility;

import java.awt.*;

public class Fonts {
    private static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private static Font[] fonts = ge.getAllFonts();

    public static Font default12;
    public static Font default24;
    public static Font default36;

    static {
        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream("/assets/font/Open_Sans/OpenSans-Regular.ttf"));
            default12 = openSans.deriveFont(12.0f);
            default24 = openSans.deriveFont(24.0f);
            default36 = openSans.deriveFont(36.0f);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
