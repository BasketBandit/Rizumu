package com.basketbandit.rizumu;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Configuration {
    private static final int width = 1280; // pixels
    private static final int height = 720; // pixels
    private static int contentWidth = 0; // pixels
    private static int contentHeight = 0; // pixels
    private static double tickRateNs = 1000000000.0 / 60.0; // nanoseconds
    private static double tickRateMs = tickRateNs / 1000000.0; // milliseconds
    private static boolean unlockedFramerate = true;
    private static String beatmapResourcePath = "D:/Program Files (x86)/Rizumu/songs";
    private static float globalGain = -10.0f; // decibels

    private static int defaultNoteWidth = 50; // pixels
    private static int defaultNoteHeight = 20; // pixels
    private static int defaultRegistrarYPosition = 600; // pixels
    private static int noteGap = 3; // pixels

    private static int noteSpeedScale = 7; // default 3

    public Configuration() {
    }

    public static void setContentBounds(int width, int height) {
        contentWidth = width;
        contentHeight = height;
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

    public static void setNoteSpeedScale(int noteSpeed) {
        noteSpeedScale = noteSpeed;
    }

    public static int getNoteSpeedScale() {
        return noteSpeedScale;
    }

    public static int getNoteGap() {
        return noteGap;
    }

    public static int getDefaultNoteWidth() {
        return defaultNoteWidth;
    }

    public static int getDefaultNoteHeight() {
        return defaultNoteHeight;
    }

    /**
     * Calculates the track start delay time and returns that value.
     * # (defaultRegistrarYPosition + (defaultNoteHeight/2) / noteSpeedScale) --- (time in ticks to reach registrar, rounded up since we cannot work in half ticks)
     * # (above result/60) * 1000 --- (calculating in seconds how long it takes to reach registrar, then multiplying by 1000 to get time in milliseconds)
     * @return {@link Long}
     */
    public static long getTrackStartDelay() {
        return new BigDecimal((new BigDecimal((defaultRegistrarYPosition + (defaultNoteHeight/2.0)) / noteSpeedScale).setScale(0,  RoundingMode.UP).doubleValue() / 60.0) * 1000).setScale(1, RoundingMode.UP).longValue();
    }

    public static int getDefaultRegistrarYPosition() {
        return defaultRegistrarYPosition;
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

    public static void setBeatmapResourcePath(String beatmapResourcePath) {
        Configuration.beatmapResourcePath = beatmapResourcePath;
    }
}
