package com.basketbandit.rizumu.utility;

import java.awt.*;

public class Polygons {
    /**
     * Right-angled triangle, where the right-angle is on the right side of the shape (with positive integers)
     * @param x int coordinate
     * @param y int coordinate
     * @param width int width
     * @param height int height
     * @return {@link Polygon}
     */
    public static Polygon rightTriangle(int x, int y, int width, int height) {
        return new Polygon(new int[]{x, x+width, x+width}, new int[]{y+height, y+height, y},3);
    }
}
