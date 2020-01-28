package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.scene.TrackScene;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.NoteLoadJob;

import java.util.List;

public class NoteLoader{
    private Beatmap beatmap;
    private TrackScene scene;

    public NoteLoader(Beatmap beatmap, TrackScene scene) {
        this.beatmap = beatmap;
        this.scene = scene;
        parseNotes();
    }

    private void parseNotes() {
        List<Segment> segments = beatmap.getSegments();
        int loadOffset = 0; // Counter for overall segment length, used to help time the next segment

        for(Segment segment: segments) {
            List<Note> notes = segment.getNotes();
            for(Note note: notes) {
                note.setTime(loadOffset + note.getTime()); // Set the relative segment note time to absolute beatmap time
                note.setColor(note.getKeyNum()); // Sets note colour based on key number
                switch(note.getKeyNum()) {
                    case 0:
                        note.x = 25;
                        note.setColor(0);
                        break;
                    case 1:
                        note.x = 75;
                        note.setColor(1);
                        break;
                    case 2:
                        note.x = 125;
                        note.setColor(2);
                        break;
                    case 3:
                        note.x = 175;
                        note.setColor(3);
                }
                ScheduleHandler.registerUniqueJob(new NoteLoadJob(note, scene));
            }
            loadOffset += segment.getLength();
        }
        System.out.println("Loaded next notes...");
    }
}

