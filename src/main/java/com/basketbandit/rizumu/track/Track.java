package com.basketbandit.rizumu.track;

import com.basketbandit.rizumu.drawable.NoteGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Track {
    private String title;
    private int beatsPerMinute;
    private double noteInterval;
    private double trackLength;
    private int introLength;
    private ArrayList<NoteGroup> noteGroups = new ArrayList<>();
    private Iterator<NoteGroup> noteIterator;

    public Track(String title, int beatsPerMinute, int introLength, Collection<NoteGroup> noteGroups) {
        this.title = title;
        this.beatsPerMinute = beatsPerMinute;
        this.introLength = introLength;
        this.noteGroups.addAll(noteGroups);
        this.noteInterval = (60.0/beatsPerMinute)*1000;
        this.trackLength = noteGroups.size()*noteInterval;
        this.noteIterator = noteGroups.iterator();
    }

    public void addNote(NoteGroup noteGroup) {
        this.noteGroups.add(noteGroup);
    }

    public void addNotes(Collection<NoteGroup> noteGroups) {
        this.noteGroups.addAll(noteGroups);
    }

    public String getTitle() {
        return title;
    }

    public int getBeatsPerMinute() {
        return beatsPerMinute;
    }

    public double getNoteInterval() {
        return noteInterval;
    }

    public int getIntroLength() {
        return introLength;
    }

    public double getTrackLength() {
        return trackLength;
    }

    public ArrayList<NoteGroup> getNoteGroups() {
        return noteGroups;
    }

    public NoteGroup nextNoteGroup() {
        return noteIterator.hasNext() ? noteIterator.next() : null;
    }
}
