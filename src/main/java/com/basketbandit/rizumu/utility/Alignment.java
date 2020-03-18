package com.basketbandit.rizumu.utility;

import java.awt.*;

// formulas modified from http://www.java2s.com/Tutorial/Java/0261__2D-Graphics/Centertext.htm
public class Alignment {
    /**
     * Centers the input string in the x axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param containerX container X
     * @param containerWidth container width
     * @return int value representing new x axis value
     */
    public static int center(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string)) / 2;
    }

    /**
     * Centers the input string in the x axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param rect rectangle used to get x, y, width and height values
     * @return int value representing new x axis value
     */
    public static int center(String string, FontMetrics metrics, Rectangle rect) {
        return rect.x + (rect.width - metrics.stringWidth(string)) / 2;
    }

    /**
     * Centers the input string in both the x and y axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param rect rectangle used to get x, y, width and height values
     * @return array of ints, with int[0] being the new x value for the x axis and int[1] being the nex y value for the y axis
     */
    public static int[] centerBoth(String string, FontMetrics metrics, Rectangle rect) {
        int[] ints = new int[2];
        ints[0] = rect.x + (rect.width - metrics.stringWidth(string)) / 2;
        ints[1] = rect.y + (metrics.getAscent() + (rect.height - (metrics.getAscent() + metrics.getDescent())) / 2);
        return ints;
    }

    /**
     * Centers the input string in both the x and y axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param containerX container X
     * @param containerY container Y
     * @param containerWidth container width
     * @param containerHeight container height
     * @return array of ints, with int[0] being the new x value for the x axis and int[1] being the nex y value for the y axis
     */
    public static int[] centerBoth(String string, FontMetrics metrics, int containerX, int containerY, int containerWidth, int containerHeight) {
        int[] ints = new int[2];
        ints[0] = containerX + (containerWidth - metrics.stringWidth(string)) / 2;
        ints[1] = containerY + (metrics.getAscent() + (containerHeight - (metrics.getAscent() + metrics.getDescent())) / 2);
        return ints;
    }

    /**
     * Right-align the input string on the x axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param containerX container X
     * @param containerWidth container width
     * @return int value representing new x axis value
     */
    public static int right(String string, FontMetrics metrics, int containerX, int containerWidth) {
        return containerX + (containerWidth - metrics.stringWidth(string));
    }

    /**
     * Right-align the input string on the x axis
     * @param string input text
     * @param metrics used to calculate the width of the text in pixels
     * @param rect rectangle used to get x, y, width and height values
     * @return int value representing new x axis value
     */
    public static int right(String string, FontMetrics metrics, Rectangle rect) {
        return rect.x + (rect.width - metrics.stringWidth(string));
    }
}
