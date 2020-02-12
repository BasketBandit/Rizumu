package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.beatmap.Note;
import com.basketbandit.rizumu.drawable.ExtendedRegistrar;
import com.basketbandit.rizumu.drawable.Registrar;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapInitJob;
import com.basketbandit.rizumu.score.Statistics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackScene implements Scene {
    private TrackRenderer renderObject = new TrackRenderer();
    private TrackTicker tickObject = new TrackTicker();

    private boolean isPaused;
    private AudioPlayer audioPlayer;
    private Statistics statistics = new Statistics();

    private Beatmap beatmap;
    private List<Note> notes = new CopyOnWriteArrayList<>(); // Use this type of ArrayList to overcome concurrent modification exceptions. (it's costly, is this method suitable)
    private Registrar registrar = new Registrar();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    public TrackScene(Beatmap beatmap) {
        this.audioPlayer = AudioPlayerController.getAudioPlayer("beatmap");
        this.beatmap = beatmap;
        ScheduleHandler.registerUniqueJob(new BeatmapInitJob(this)); // Will load beatmap notes, start audio, etc.
    }

    public List<Note> getNotes() {
        return notes;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public Beatmap getBeatmap() {
        return beatmap;
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class TrackRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            // Draw framerate and tickrate.
            g.setFont(fonts[368].deriveFont(Font.PLAIN,12));
            g.setColor(Color.GRAY);
            g.drawString("Hit: " + statistics.getHitNotes() + " | Missed: " + statistics.getMissedNotes() + " | %: " + statistics.getHitRate(), 10, 40);

            g.setColor(extendedRegistrar.getColor());
            g.fill(extendedRegistrar);

            g.setColor(registrar.getColor());
            g.fill(registrar);

            for(Note note: notes) {
                g.setColor(note.getColor());
                g.fill(note);

                // creates a boarder around notes and make single_long's hit/release radius easy to see
                g.setColor(Color.BLACK);
                g.drawRect(note.x, note.y, note.width, note.height);
                if(note.getNoteType().equals("single_long")) {
                    g.drawRect(note.x, note.y, note.width, 23);
                    g.drawRect(note.x, note.y + (note.height - 23), note.width, 23);
                }
            }
        }
    }

    private class TrackTicker implements TickObject {
        @Override
        public void tick() {
            // if the secondary render object is NOT null (implies pause menu is open)
            if(!Rizumu.secondaryRenderObjectIsNull()) {
                audioPlayer.pause();
                return;
            }

            if(KeyInput.wasPressed(KeyEvent.VK_ESCAPE)) {
                Rizumu.setSecondaryScene(Rizumu.getStaticScene(Scenes.PAUSE));
            }

            audioPlayer.resume();

            for(Note note: notes) {
                note.translate(0, SystemConfiguration.getNoteSpeedScale()); // translate each note in positive y

                if(!note.hit() && note.getNoteType().equals("single") && registrar.intersects(note) && KeyInput.isDown(note.getKey())) {
                    note.setHit();
                    statistics.incrementHit();
                }

                // TODO: make missing single_long increase miss counter
                if(note.getNoteType().equals("single_long") && registrar.intersects(note)) {
                    if(KeyInput.isDown(note.getKey()) && ((note.getMaxY() <= registrar.getMaxY()+23) && !note.wasHeld())) {
                        note.setHeld();
                    }
                    if(!KeyInput.isDown(note.getKey()) && note.wasHeld()) {
                        note.setHit();
                    }
                    if(KeyInput.isDown(note.getKey()) && note.wasHeld()) {
                        statistics.incrementHit();
                    }
                }

                // we use .getMinY() here because Y = 0 starts at the top left of the shape and extends downwards. (negative height to reverse this isn't possible)
                if((note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && !registrar.intersects(note) && KeyInput.isDown(note.getKey()))) && !note.hit() || ((note.getMaxY() >= registrar.getMaxY()+25) && !note.wasHeld())) {
                    statistics.incrementMissed();
                }
            }

            // single was hit (hit on time)
            // single has passed the registrar (hit too late or not at all)
            // single hasn't passed the registrar but intersects the extendedRegistrar with the key down (hit to early)
            // single_long has passed the registrar for the length of a single and the single_long isn't being held (hit too late)
            notes.removeIf(note -> note.hit() || note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && KeyInput.isDown(note.getKey()) && !note.wasHeld()) || ((note.getMaxY() >= registrar.getMaxY()+25) && !note.wasHeld()));
        }
    }
}
