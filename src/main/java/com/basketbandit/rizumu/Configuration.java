package com.basketbandit.rizumu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static int width = 1280; // pixels
    private static int height = 720; // pixels
    private static double scale = width / 1280.0;;

    private static double tickRateNs = 1000000000.0 / 60.0; // nanoseconds
    private static double tickRateMs = tickRateNs / 1000000.0; // milliseconds
    private static boolean frameLock = false;

    private static String userDirectory;
    private static String tracksPath;
    private static float globalGain = -10.0f; // decibels

    private static int defaultBeatmapXPosition = (int) (250 * scale); // pixels
    private static int defaultNoteWidth = 50; // pixels
    private static int defaultNoteHeight = 20; // pixels
    private static int defaultRegistrarYFromBottom = height - 120; // pixels
    private static int noteGap = 3; // pixels
    private static int noteSpeedScale = 7; // default 3

    private static String username; // can logged in user be as simple as having this username set? (should we check login information on each score upload?)

    public Configuration() {
        File file = new File(System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Rizumu");
        userDirectory = file.getPath();
        if(!file.exists()) {
            if(file.mkdirs() && new File(userDirectory + File.separator + "tracks").mkdir()) {
                log.info("Successfully created directory created at: " + userDirectory);
            }

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(userDirectory + File.separator + "rizumu.ini").getAbsoluteFile()))) {
                bw.write("width = 1280");
                bw.newLine();
                bw.write("height = 720");
                bw.newLine();
                bw.write("frame_lock = false");
                bw.newLine();
                bw.write("songs_directory = " + userDirectory + File.separator + "tracks");
            } catch(Exception ex) {
                log.error("An error occurred during directory setup, message: {}", ex.getMessage(), ex);
            }
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(new File(userDirectory + File.separator + "rizumu.ini")))) {
            List<String> vals = reader.lines().map(s -> {s = s.substring(s.indexOf("=") + 1).strip(); return s;}).collect(Collectors.toList());
            width = Integer.parseInt(vals.get(0));
            height = Integer.parseInt(vals.get(1));
            scale = width / 1280.0;
            frameLock = Boolean.parseBoolean(vals.get(2));
            tracksPath = vals.get(3);

            log.info("Successfully read configuration file!");
        } catch(Exception ex) {
            log.error("An error occurred while reading configuration file, message: {}", ex.getMessage(), ex);
        }
    }

    public static int getHeight() {
        return height;
    }

    public static int getWidth() {
        return width;
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

    public static int getDefaultBeatmapXPosition() {
        return defaultBeatmapXPosition;
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
        return BigDecimal.valueOf((new BigDecimal((defaultRegistrarYFromBottom + (defaultNoteHeight / 2.0)) / noteSpeedScale).setScale(0, RoundingMode.UP).doubleValue() / 60.0) * 1000).setScale(1, RoundingMode.UP).longValue();
    }

    public static int getDefaultRegistrarYFromBottom() {
        return defaultRegistrarYFromBottom;
    }

    public static void toggleUnlockedFramerate() {
        frameLock = !frameLock;
    }

    public static boolean isFrameLock() {
        return frameLock;
    }

    public static String getTracksPath() {
        return tracksPath;
    }

    public static void setTracksPath(String path) {
        tracksPath = path;
    }

    public static void setUser(String user) {
        username = user;
    }

    public static String getUser() {
        return username;
    }
}
