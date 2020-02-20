package com.basketbandit.rizumu;

import java.util.Map;

public class Configuration {
    private static final int width = 1280;
    private static final int height = 720;
    private static int contentWidth = 0;
    private static int contentHeight = 0;
    private static double tickRateNs = 1000000000.0 / 60.0;
    private static double tickRateMs = tickRateNs / 1000000.0;
    private static boolean unlockedFramerate = true;
    private static String beatmapResourcePath = "src/main/resources/beatmaps/";
    private static float globalGain = -10.0f;
    private static int noteSpeedScale = 3;
    private static int noteGap = 3;
    private static Map<Integer, Integer> speedAdjustment = Map.of(2, 1, 3, 10, 4, 20, 5, 30, 6, 40, 7, 50, 8, 58, 9, 68, 10, 77);

    public Configuration() {
    }

    public static void setContentBounds(int width, int height) {
        contentWidth = width;
        contentHeight = height;
    }

    public static int getSpeedMultiplier() {
        return speedAdjustment.get(noteSpeedScale);
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
    }

    public static int getContentWidth() {
        return contentWidth;
    }

    public static int getContentHeight() {
        return contentHeight;
    }

    public static double getTickRateNs() {
        return tickRateNs;
    }

    public static double getTickRateMs() {
        return tickRateMs;
    }

    public static int getNoteSpeedScale() {
        return noteSpeedScale;
    }

    public static int getNoteGap() {
        return noteGap;
    }

    public static void toggleUnlockedFramerate() {
        unlockedFramerate = !unlockedFramerate;
    }

    public static boolean isUnlockedFramerate() {
        return unlockedFramerate;
    }

    public static String getBeatmapResourcePath() {
        return beatmapResourcePath;
    }
}
