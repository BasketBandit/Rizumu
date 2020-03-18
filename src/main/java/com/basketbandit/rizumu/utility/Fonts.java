package com.basketbandit.rizumu.utility;

import java.awt.*;

public class Fonts {
    public static Font default12;
    public static Font default16;
    public static Font default24;
    public static Font default36;
    public static Font default72;

    static {
        try {
            Font openSans = Font.createFont(Font.TRUETYPE_FONT, Fonts.class.getResourceAsStream("/assets/font/Open_Sans/OpenSans-Regular.ttf"));
            default12 = openSans.deriveFont(12.0f);
            default16 = openSans.deriveFont(16.0f);
            default24 = openSans.deriveFont(24.0f);
            default36 = openSans.deriveFont(36.0f);
            default72 = openSans.deriveFont(72.0f);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
