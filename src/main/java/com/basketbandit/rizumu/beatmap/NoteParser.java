package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Segment;
import com.basketbandit.rizumu.resource.Image;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapEndJob;
import com.basketbandit.rizumu.stage.scene.TrackScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class NoteParser {
    private static final Logger log = LoggerFactory.getLogger(NoteParser.class);
    private TrackScene scene;
    private Beatmap beatmap;

    public NoteParser(TrackScene scene, Beatmap beatmap) {
        this.scene = scene;
        this.beatmap = beatmap;
        parseNotes();
    }

    private void parseNotes() {
        List<Segment> segments = beatmap.getSegments();
        int noteGap = Configuration.getNoteGap();
        int loadOffset = 0; // Counter for overall segment length, used to help time the next segment
        java.awt.Image noteImage = Image.getBufferedImage("note").getScaledInstance(Configuration.getDefaultNoteWidth(), Configuration.getDefaultNoteHeight(), 0);
        java.awt.Image notebImage =  Image.getBufferedImage("noteb").getScaledInstance(Configuration.getDefaultNoteWidth(), Configuration.getDefaultNoteHeight(), 0);

        for(Segment segment: segments) {
            List<Note> notes = segment.getNotes();
            for(Note note: notes) {
                note.setTime(loadOffset + note.getTime()); // set the relative segment note time to absolute beatmap time

                int noteXPosition = Configuration.getDefaultBeatmapXPosition() + noteGap*2 + 1; // formula w/o noteGap - (Configuration.getContentWidth()/2) - (50*beatmap.getKeys()/2)
                int noteYPosition = new BigDecimal((note.getTime() / Configuration.getTickRateMs()) * Configuration.getNoteSpeedScale()).setScale(0, RoundingMode.DOWN).intValue(); // calculate the position of the note
                int noteHeight = new BigDecimal((note.getNoteLength() / Configuration.getTickRateMs()) * Configuration.getNoteSpeedScale()).setScale(0, RoundingMode.DOWN).intValue(); // calculate the height of the note

                // deal with Osu transferred keyNums -- floor(x * columnCount / 512)
                if(note.getKeyNum() > 9) {
                    note.setKeyNum((int) Math.floor(note.getKeyNum() * beatmap.getKeys() / 512.0));
                }

                switch(note.getKeyNum()) {
                    case 0:
                        note.x = noteXPosition;
                        note.setKey(getKey(0));
                        break;
                    case 1:
                        note.x = noteXPosition + ((note.width+noteGap));
                        note.setKey(getKey(1));
                        break;
                    case 2:
                        note.x = noteXPosition + ((note.width+noteGap)*2);
                        note.setKey(getKey(2));
                        break;
                    case 3:
                        note.x = noteXPosition + ((note.width+noteGap)*3);
                        note.setKey(getKey(3));
                        break;
                    case 4:
                        note.x = noteXPosition + ((note.width+noteGap)*4);
                        note.setKey(getKey(4));
                        break;
                    case 5:
                        note.x = noteXPosition + ((note.width+noteGap)*5);
                        note.setKey(getKey(5));
                        break;
                    case 6:
                        note.x = noteXPosition + ((note.width+noteGap)*6);
                        note.setKey(getKey(6));
                        break;
                }

                note.y += note.getNoteType().equals("single_long") ? -noteYPosition-noteHeight : -noteYPosition-Configuration.getDefaultNoteHeight();
                note.height = note.getNoteType().equals("single_long") ? noteHeight : note.height;

                // pre-scale notes (this saves A LOT of cpu cycles compared to scaling on the fly... as fast as the cpu can render)
                if(note.getNoteType().equals("single_long")) {
                    note.initImages(noteImage, notebImage.getScaledInstance(note.width, note.height, 0));
                } else {
                    note.initImages(noteImage);
                }

                scene.getNotes().add(note);
            }
            loadOffset += segment.getLength();
        }

        ScheduleHandler.registerUniqueJob(new BeatmapEndJob(scene, loadOffset));
    }

    public static int getKey(int num) {
        switch(num) {
            case 0:
                return KeyEvent.VK_Q;
            case 1:
                return KeyEvent.VK_W;
            case 2:
                return KeyEvent.VK_E;
            case 3:
                return KeyEvent.VK_R;
            case 4:
                return KeyEvent.VK_T;
            case 5:
                return KeyEvent.VK_Y;
            case 6:
                return KeyEvent.VK_U;
            default:
                return KeyEvent.VK_UNDEFINED;
        }
    }
}

