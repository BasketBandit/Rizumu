package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.NoteLoadJob;

import java.util.List;

public class NoteLoader{
    private TrackScene scene;
    private Beatmap beatmap;

    public NoteLoader(TrackScene scene, Beatmap beatmap) {
        this.scene = scene;
        this.beatmap = beatmap;
        parseNotes();
    }

    private void parseNotes() {
        List<Segment> segments = beatmap.getSegments();
        int loadOffset = 0; // Counter for overall segment length, used to help time the next segment

        for(Segment segment: segments) {
            List<Note> notes = segment.getNotes();
            for(Note note: notes) {
                note.setTime(loadOffset + note.getTime()); // Set the relative segment note time to absolute beatmap time
                note.initNote(note.getKeyNum()); // Set things such as x position, size, colour and hit_key of the note
                ScheduleHandler.registerUniqueJob(new NoteLoadJob(note, scene));
            }
            loadOffset += segment.getLength();
        }
        System.out.println("Loaded next notes...");
    }
}

