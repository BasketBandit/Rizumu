package com.basketbandit.rizumu.score;

import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Score {
    private Track track;
    private Beatmap beatmap;

    private int hitNotes = 0, missedNotes = 0, combo = 0, highestCombo = 0;
    private int mxHit = 0, exHit = 0, nmHit = 0;
    private int score = 0, multiplier = 1;

    public Score(Track track, Beatmap beatmap) {
        this.track = track;
        this.beatmap = beatmap;
    }

    public Track getTrack() {
        return track;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    public void incrementHit() {
        hitNotes++;
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
        return hitNotes;
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

    public double getAccuracy() {
        if(hitNotes == 0 && missedNotes == 0) {
            return 100; // prevent NumberFormatException
        }
        return new BigDecimal((double)hitNotes/((double)hitNotes+(double)missedNotes)*100).setScale(2, RoundingMode.DOWN).doubleValue();
    }

    public String getAccuracyString() {
        return "Accuracy: " + getAccuracy() + "% (" + hitNotes + "/" + (hitNotes+missedNotes) + ")";
    }

    public Image getImage() {
        return track.getImage();
    }
}
