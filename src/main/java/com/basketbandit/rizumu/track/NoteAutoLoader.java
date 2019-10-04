package com.basketbandit.rizumu.track;

import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.drawable.NoteGroup;
import com.basketbandit.rizumu.scene.TrackScene;

import java.util.List;
import java.util.TimerTask;

public class NoteAutoLoader extends TimerTask {
    private TrackScene trackScene;
    private Track track;

    public NoteAutoLoader(TrackScene trackScene, Track track) {
        this.trackScene = trackScene;
        this.track = track;
    }

    @Override
    public void run() {
        NoteGroup noteGroup;
        if((noteGroup = track.nextNoteGroup()) == null) {
            System.out.println("Finished loading.");
            cancel();
            return;
        }

        List<Note> notes = noteGroup.getGroup();
        for(Note note: notes) {
            if(note != null) {
                this.trackScene.getNotes().add(note);
            }
        }

        System.out.println("Loaded next notes...");
    }
}
