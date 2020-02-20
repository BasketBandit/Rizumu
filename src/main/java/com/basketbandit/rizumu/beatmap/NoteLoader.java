package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Segment;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapEndJob;
import com.basketbandit.rizumu.stage.scene.TrackScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
                note.setTime(loadOffset + note.getTime()); // set the relative segment note time to absolute beatmap time

                int noteXPosition = (Configuration.getContentWidth()/2) - (50*beatmap.getKeys()/2);
                int noteYPosition = new BigDecimal((note.getTime() / Configuration.getTickRateMs()) * Configuration.getNoteSpeedScale()).setScale(0, RoundingMode.HALF_UP).intValue(); // calculate the position of the note
                int noteHeight = new BigDecimal((note.getNoteLength() / Configuration.getTickRateMs()) * Configuration.getNoteSpeedScale()).setScale(0, RoundingMode.HALF_UP).intValue(); // calculate the height of the note

                switch(note.getKeyNum()) {
                    case 0:
                        note.x = noteXPosition;
                        note.setKey(KeyEvent.VK_Q);
                        note.setColor(Color.GREEN);
                        break;
                    case 1:
                        note.x = noteXPosition+50;
                        note.setKey(KeyEvent.VK_W);
                        note.setColor(Color.RED);
                        break;
                    case 2:
                        note.x = noteXPosition+100;
                        note.setKey(KeyEvent.VK_E);
                        note.setColor(Color.YELLOW);
                        break;
                    case 3:
                        note.x = noteXPosition+150;
                        note.setKey(KeyEvent.VK_R);
                        note.setColor(Color.BLUE);
                        break;
                    case 4:
                        note.x = noteXPosition+200;
                        note.setKey(KeyEvent.VK_T);
                        note.setColor(Color.ORANGE);
                        break;
                    case 5:
                        note.x = noteXPosition+250;
                        note.setKey(KeyEvent.VK_Y);
                        note.setColor(Color.PINK);
                        break;
                    case 6:
                        note.x = noteXPosition+300;
                        note.setKey(KeyEvent.VK_U);
                        note.setColor(Color.MAGENTA);
                        break;
                }

                note.y += note.getNoteType().equals("single_long") ? -noteYPosition-noteHeight : -noteYPosition-25;
                note.height = note.getNoteType().equals("single_long") ? noteHeight : note.height;

                scene.getNotes().add(note);
            }
            loadOffset += segment.getLength();
        }

        ScheduleHandler.registerUniqueJob(new BeatmapEndJob(scene, loadOffset));
    }
}

