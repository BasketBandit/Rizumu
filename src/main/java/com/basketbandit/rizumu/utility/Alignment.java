package com.basketbandit.rizumu.utility;

import java.awt.*;

// formulas modified from http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
public class Alignment {
    public static int center(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string)) / 2;
    }

    public static int center(String string, FontMetrics metrics, Rectangle rect) {
        return rect.x + (rect.width - metrics.stringWidth(string)) / 2;
    }

    public static int[] centerBoth(String string, FontMetrics metrics, Rectangle rect) {
        int[] ints = new int[2];
        ints[0] = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
        ints[1] = rect.y + (metrics.getAscent() + (rect.height - (metrics.getAscent() + metrics.getDescent())) / 2);
        return ints;
    }

    public static int right(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string));
    }
}
