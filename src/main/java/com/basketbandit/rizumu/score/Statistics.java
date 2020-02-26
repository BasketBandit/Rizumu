package com.basketbandit.rizumu.score;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Statistics {
    private Image image;
    private int hitNotes, missedNotes, combo, highestCombo;

    public Statistics(Image image) {
        this.image = image;
        this.hitNotes = 0;
        this.missedNotes = 0;
        this.combo = 0;
        this.highestCombo = 0;
    }

    public void incrementHit() {
        hitNotes++;
        if(++combo > highestCombo) {
            highestCombo = combo;
        }
    }

    public void incrementMissed() {
        missedNotes++;
        combo = 0;
    }

    public int getHitNotes() {
        return hitNotes;
    }

    public int getMissedNotes() {
        return missedNotes;
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
        return image;
    }
}
