package com.basketbandit.rizumu.score;

public class Statistics {
    private int hitNotes, missedNotes;

    public Statistics() {
    }

    public void incrementHit() {
        hitNotes++;
    }

    public void incrementMissed() {
        missedNotes++;
    }

    public int getHitNotes() {
        return hitNotes;
    }

    public int getMissedNotes() {
        return missedNotes;
    }

    public double getAccuracy() {
        return ((double)hitNotes/((double)hitNotes+(double)missedNotes))*100;
    }

    public String getAccuracyString() {
        return "Accuracy: " + getAccuracy() + "% (" + hitNotes + "/" + (hitNotes+missedNotes) + ")";
    }
}
