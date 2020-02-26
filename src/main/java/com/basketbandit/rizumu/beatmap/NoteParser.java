package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Segment;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapEndJob;
import com.basketbandit.rizumu.stage.scene.TrackScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
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
        Image noteImage = null;
        Image notebImage = null;

        try {
            noteImage = ImageIO.read(new File("src/main/resources/assets/note.png")).getScaledInstance(Configuration.getDefaultNoteWidth(), Configuration.getDefaultNoteHeight(), 0);
            notebImage = ImageIO.read(new File("src/main/resources/assets/noteb.png")).getScaledInstance(Configuration.getDefaultNoteWidth(), Configuration.getDefaultNoteHeight(), 0);
        } catch(IOException ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

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
                        note.setKey(KeyEvent.VK_Q);
                        break;
                    case 1:
                        note.x = noteXPosition + ((note.width+noteGap));
                        note.setKey(KeyEvent.VK_W);
                        break;
                    case 2:
                        note.x = noteXPosition + ((note.width+noteGap)*2);
                        note.setKey(KeyEvent.VK_E);
                        break;
                    case 3:
                        note.x = noteXPosition + ((note.width+noteGap)*3);
                        note.setKey(KeyEvent.VK_R);
                        break;
                    case 4:
                        note.x = noteXPosition + ((note.width+noteGap)*4);
                        note.setKey(KeyEvent.VK_T);
                        break;
                    case 5:
                        note.x = noteXPosition + ((note.width+noteGap)*5);
                        note.setKey(KeyEvent.VK_Y);
                        break;
                    case 6:
                        note.x = noteXPosition + ((note.width+noteGap)*6);
                        note.setKey(KeyEvent.VK_U);
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
}

