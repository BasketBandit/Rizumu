package com.basketbandit.rizumu.track;

import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.drawable.NoteGroup;
import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.NoteLoadJob;

import java.util.Iterator;
import java.util.List;

public class NoteLoader{
    private Track track;
    private TrackScene scene;

    public NoteLoader(Track track, TrackScene scene) {
        this.track = track;
        this.scene = scene;
        parseNotes();
    }

    private void parseNotes() {
        Iterator<NoteGroup> noteGroupIterator = track.getNoteGroupIterator();
        if(!noteGroupIterator.hasNext()) {
            System.out.println("Finished loading.");
            return;
        }

        while(noteGroupIterator.hasNext()) {
            List<Note> notes = noteGroupIterator.next().getGroup();
            for(Note note: notes) {
                if(note != null) {
                    ScheduleHandler.registerUniqueJob(new NoteLoadJob(note, scene));
                }
            }
        }

        System.out.println("Loaded next notes...");
    }
}
