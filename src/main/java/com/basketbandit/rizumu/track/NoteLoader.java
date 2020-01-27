package com.basketbandit.rizumu.track;

import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.drawable.NoteGroup;
import com.basketbandit.rizumu.scene.TrackScene;

import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

public class NoteLoader extends TimerTask {
    private TrackScene trackScene;
    private Track track;
    private Iterator<NoteGroup> noteGroupIterator;

    public NoteLoader(TrackScene trackScene, Track track) {
        this.trackScene = trackScene;
        this.track = track;
        this.noteGroupIterator = track.getNoteGroupIterator();
    }

    @Override
    public void run() {
        if(!noteGroupIterator.hasNext()) {
            System.out.println("Finished loading.");
            cancel();
            return;
        }

        List<Note> notes = noteGroupIterator.next().getGroup();
        for(Note note: notes) {
            if(note != null) {
                this.trackScene.getNotes().add(note);
            }
        }

        System.out.println("Loaded next notes...");
    }
}
