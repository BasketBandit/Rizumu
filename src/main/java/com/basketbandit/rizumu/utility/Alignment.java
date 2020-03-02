package com.basketbandit.rizumu.utility;

import java.awt.*;

public class Alignment {
    public static int center(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string)) / 2;
    }

    public static int center(String string, FontMetrics metrics, Rectangle rect) {
        return rect.x + (rect.width - metrics.stringWidth(string)) / 2;
    }

    public static int right(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string));
    }
}
