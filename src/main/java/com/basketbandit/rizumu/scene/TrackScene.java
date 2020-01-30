package com.basketbandit.rizumu.scene;


import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.beatmap.Note;
import com.basketbandit.rizumu.drawable.ExtendedRegistrar;
import com.basketbandit.rizumu.drawable.Registrar;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapDelayJob;
import com.basketbandit.rizumu.score.Statistics;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TrackScene implements Scene {
    private TrackRender renderObject = new TrackRender();
    private TrackTicker tickObject = new TrackTicker();

    private Statistics statistics = new Statistics();

    private Beatmap beatmap;
    private ArrayList<Note> notes = new ArrayList<>();
    private Registrar registrar = new Registrar();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    public TrackScene(Beatmap beatmap) {
        this.beatmap = beatmap;
        ScheduleHandler.registerUniqueJob(new BeatmapDelayJob(beatmap, this)); // Will start loading the notes for the beatmap after the map-defined delay period.
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class TrackRender implements RenderObject {
        private String framesPerSecondText = "0 FPS";
        private String ticksPerSecondText = "0 TPS";

        private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        private Font[] fonts = ge.getAllFonts();

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
                if((note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && !registrar.intersects(note) && KeyInput.isDown(note.getKey()))) && !note.hit()) {
                    statistics.incrementMissed();
                }
            }

            // single was hit (hit on time)
            // single has passed the registrar (hit too late or not at all)
            // single hasn't passed the registrar but intersects the extendedRegistrar with the key down (hit to early)
            // single_long has passed the registrar for the length of a single and the single_long isn't being held (hit too late)
            notes.removeIf(note -> note.hit() || note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && KeyInput.isDown(note.getKey()) && !note.wasHeld()) || ((note.getMaxY() >= registrar.getMaxY()+23) && !note.wasHeld()));

            if(KeyInput.isDown(KeyEvent.VK_ESCAPE)) {
                Rizumu.engine.changeScene(new MenuScene());
            }
        }
    }
}
