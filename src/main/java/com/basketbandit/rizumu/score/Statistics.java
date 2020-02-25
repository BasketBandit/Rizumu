package com.basketbandit.rizumu.score;

public class Statistics {
    private int hitNotes, missedNotes;

    public Statistics() {
        this.hitNotes = 0;
        this.missedNotes = 0;
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
        if(hitNotes == 0 && missedNotes == 0) {
            return 100; // prevent NumberFormatException
        }
        return ((double)hitNotes/((double)hitNotes+(double)missedNotes))*100;
    }

    public String getAccuracyString() {
        return "Accuracy: " + getAccuracy() + "% (" + hitNotes + "/" + hitNotes+missedNotes + ")";
    }
}
