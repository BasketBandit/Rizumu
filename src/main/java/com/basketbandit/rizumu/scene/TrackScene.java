package com.basketbandit.rizumu.scene;


import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.drawable.ExtendedRegistrator;
import com.basketbandit.rizumu.drawable.Note;
import com.basketbandit.rizumu.drawable.Registrator;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.score.Statistics;
import com.basketbandit.rizumu.track.NoteAutoLoader;
import com.basketbandit.rizumu.track.Track;

import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

public class TrackScene implements Scene {
    private SystemConfiguration sys;
    private TrackRender renderObject = new TrackRender();
    private TrackTicker tickObject = new TrackTicker();

    private Statistics statistics = new Statistics();

    private Track track;
    private ArrayList<Note> notes = new ArrayList<>();
    private Registrator registrator = new Registrator();
    private ExtendedRegistrator extendedRegistrator = new ExtendedRegistrator();

    public TrackScene(SystemConfiguration sys, Track track) {
        this.sys = sys;
        this.track = track;
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
            g.setColor(Color.WHITE);
            g.drawString("Hit: " + statistics.getHitNotes() + " | Missed: " + statistics.getMissedNotes() + " | %: " + statistics.getHitRate(), 10, 40);

            g.setColor(extendedRegistrator.getColor());
            g.fillRect(extendedRegistrator.x, extendedRegistrator.y, extendedRegistrator.width, extendedRegistrator.height);

            g.setColor(registrator.getColor());
            g.fillRect(registrator.x, registrator.y, registrator.width, registrator.height);

            for(Note note: notes) {
                g.setColor(note.getColor());
                g.fillRect(note.x, note.y, note.width, note.height);
            }
        }

    }

    private class TrackTicker implements TickObject {
        boolean isInit = false;
        Timer timer = new Timer(true);

        @Override
        public void tick() {
            if(!isInit) {
                timer.schedule(new NoteAutoLoader(TrackScene.this, track), track.getIntroLength(), (long)track.getNoteInterval());
                isInit = true;
            }

            for(Note note: notes) {
                note.translate(0, sys.getNoteSpeedScale());

                if(note.getType() == 1 && registrator.intersects(note) && KeyInput.isDown(note.getKey())) {
                    if(!note.hit()) {
                        note.setHit(true);
                        statistics.incrementHit();
                    }
                }

                //if(note.getType() == 2 && registrator.intersects(note) && KeyInput.isDown(note.getKey() && KeyInput.wasReleased())

                if((note.y >= sys.getHeight() || (extendedRegistrator.intersects(note) && KeyInput.isDown(note.getKey()))) && !note.hit()) {
                    statistics.incrementMissed();
                }
            }

            notes.removeIf(note -> note.hit() || note.y >= sys.getHeight() || (extendedRegistrator.intersects(note) && KeyInput.isDown(note.getKey())));
        }
    }
}
