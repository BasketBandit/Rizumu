package com.basketbandit.rizumu;

import java.util.Map;

public class SystemConfiguration {
    private static final int width = 1280;
    private static final int height = 720;
    private static double tickRate = 1000000000.0 / (60 + 0.0);
    private static boolean unlockedFramerate = false;
    private static String beatmapResourcePath = "src/main/resources/beatmaps/";
    private static float globalGain = -10.0f;
    private static int noteSpeedScale = 3;
    private static Map<Integer, Integer> speedAdjustment;

    public SystemConfiguration() {
        speedAdjustment = Map.of(
                2, 1,
                3, 10,
                4, 20,
                5, 30,
                6, 40,
                7, 50,
                8, 58,
                9, 68,
                10, 77
        );
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

    public static double getTickRate() {
        return tickRate;
    }

    public static int getNoteSpeedScale() {
        return noteSpeedScale;
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
