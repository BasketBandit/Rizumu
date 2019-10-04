package com.basketbandit.rizumu.score;

public class Statistics {
    private double hitNotes, missedNotes = 0.0;

    public Statistics() {
    }

    public void incrementHit() {
        hitNotes++;
    }

    public void incrementMissed() {
        missedNotes++;
    }

    public double getHitNotes() {
        return hitNotes;
    }

    public double getMissedNotes() {
        return missedNotes;
    }

    public double getHitRate() {
        return (hitNotes/(hitNotes+missedNotes))*100;
    }
}
