package com.basketbandit.rizumu.score;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Score {
    private Track track;
    private Beatmap beatmap;
    private String username = Configuration.getUser();
    private int userId = Configuration.getUserId();

    private int missedNotes = 0, combo = 0, highestCombo = 0;
    private int mxHit = 0, exHit = 0, nmHit = 0;
    private int score = 0, multiplier = 1;

    public Score(Track track, Beatmap beatmap) {
        this.track = track;
        this.beatmap = beatmap;
    }

    public Score(Track track, Beatmap beatmap, String username, int score, int highestCombo, int mxHit, int exHit, int nmHit, int missedNotes) {
        this.track = track;
        this.beatmap = beatmap;
        this.username = username;
        this.userId = -1;
        this.score = score;
        this.highestCombo = highestCombo;
        this.mxHit = mxHit;
        this.exHit = exHit;
        this.nmHit = nmHit;
        this.missedNotes = missedNotes;
    }

    public Track getTrack() {
        return track;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void incrementHit() {
        combo++;
        multiplier = (combo >= 40) ? 4 : (combo >= 30) ? 3 : (combo >= 20) ? 2 : 1;
        if(combo > highestCombo) {
            highestCombo = combo;
        }
    }

    public void incrementMissed() {
        missedNotes++;
        combo = 0;
        multiplier = 1;
    }

    public void incrementMxHit() {
        incrementHit();
        mxHit++;
        score += (500 * multiplier);
    }

    public void incrementExHit() {
        incrementHit();
        exHit++;
        score += (250 * multiplier);
    }

    public void incrementNmHit() {
        incrementHit();
        nmHit++;
        score += (125 * multiplier);
    }

    public int getHitNotes() {
        return mxHit + exHit + nmHit;
    }

    public int getMissedNotes() {
        return missedNotes;
    }

    public int getMxHit() {
        return mxHit;
    }

    public int getExHit() {
        return exHit;
    }

    public int getNmHit() {
        return nmHit;
    }

    public int getScore() {
        return score;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public int getCombo() {
        return combo;
    }

    public int getHighestCombo() {
        return highestCombo;
    }

    /**
     * Provides a accuracy percentage (double) between 0.00 and 100.00
     * @return double between 0.00 - 100.00
     */
    public double getAccuracy() {
        if(getHitNotes() == 0 && missedNotes == 0) {
            return 100; // prevent NumberFormatException
        }
        return BigDecimal.valueOf((double) getHitNotes() / ((double) getHitNotes() + (double) missedNotes) * 100).setScale(2, RoundingMode.DOWN).doubleValue();
    }

    /**
     * Provides a formatted {@link String} of the players accuracy as a percentage, as well as how many notes were hit in total
     * @return {@link String}
     */
    public String getAccuracyString() {
        return getAccuracy() + "% (" + getHitNotes() + "/" + (getHitNotes()+missedNotes) + ")";
    }

    public Image getImage() {
        return track.getImage();
    }
}
