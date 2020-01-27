package com.basketbandit.rizumu;

import java.util.Map;

public class SystemConfiguration {
    public final static int width = 1280;
    public final static int height = 720;
    public static double tickRate = 1000000000.0 / (60 + 0.0);
    public static boolean cappedFramerate = false;
    public static int noteSpeedScale = 9;
    public static Map<Integer, Integer> speedAdjustment;

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
}
