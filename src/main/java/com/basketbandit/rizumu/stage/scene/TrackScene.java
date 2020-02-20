package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.ExtendedRegistrar;
import com.basketbandit.rizumu.drawable.Registrar;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.input.MouseMovementListener;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapInitJob;
import com.basketbandit.rizumu.score.Statistics;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Cursors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackScene implements Scene {
    private TrackRenderer renderObject = new TrackRenderer();
    private TrackTicker tickObject = new TrackTicker();

    private PauseMenu pauseMenu = new PauseMenu();
    private AudioPlayer audioPlayer;
    private Statistics statistics;

    protected Track track;
    protected Beatmap beatmap;
    private List<Note> notes;
    private Registrar registrar = new Registrar();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    public TrackScene initScene(Track track, Beatmap beatmap) {
        this.statistics = new Statistics();
        this.audioPlayer = AudioPlayerController.getAudioPlayer("beatmap");
        this.notes = new CopyOnWriteArrayList<>(); // Use this type of ArrayList to overcome concurrent modification exceptions. (it's costly, is this method suitable)
        this.track = track;
        this.beatmap = beatmap;
        ScheduleHandler.registerUniqueJob(new BeatmapInitJob(this)); // Will load beatmap notes, start audio, etc.
        return this;
    }

    @Override
    public Scene init() {
        MouseListeners.setMouseListener("track", null);
        return this;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public Track getTrack() {
        return track;
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
            // background
            g.setColor(Colours.DARK_GREY_100);
            g.fillRect((Configuration.getContentWidth()/2) - (50*beatmap.getKeys()/2) - 5, 0, (beatmap.getKeys()*50)+10, Configuration.getContentHeight());

            g.setColor(extendedRegistrar.getColor());
            g.fill(extendedRegistrar);

            g.setColor(registrar.getColor());
            g.fill(registrar);

            // mid-ground
            for(Note note: notes) {
                g.setColor(note.getColor());
                g.fill(note);
                g.setColor(Color.BLACK);
                g.drawRect(note.x, note.y, note.width, note.height);
                if(note.getNoteType().equals("single_long")) {
                    g.drawRect(note.x, note.y + (note.height - 25), note.width, 25);
                }
            }

            // foreground
            g.setColor(Color.lightGray);
            g.fillRect(0, 0, Configuration.getContentWidth(), 50);

            g.setFont(fonts[368].deriveFont(Font.PLAIN,12));
            g.setColor(Color.GRAY);
            g.drawString("Hit: " + statistics.getHitNotes() + " | Missed: " + statistics.getMissedNotes() + " | %: " + statistics.getAccuracy(), 10, 40);
        }
    }

    private class TrackTicker implements TickObject {
        @Override
        public void tick() {
            // if the secondary render object is NOT null (implies pause menu is open)
            if(!Rizumu.secondaryRenderObjectIsNull()) {
                return;
            }

            if(KeyInput.wasPressed(KeyEvent.VK_ESCAPE)) {
                Rizumu.setSecondaryScene(pauseMenu.init());
                audioPlayer.pause();
                ScheduleHandler.pauseExecution(); // Still possible to slightly dsync audio by spamming pause. (need to investigate)
                return;
            }

            audioPlayer.resume();

            for(Note note: notes) {
                note.translate(0, Configuration.getNoteSpeedScale()); // translate each note in positive y

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

    /**
     * PauseMenu subclass, not given a separate class file due to it only being used in the context of a TrackScene.
     */
    public class PauseMenu implements Scene {
        private PauseMenuRenderer renderObject = new PauseMenuRenderer();
        private PauseMenuTicker tickObject = new PauseMenuTicker();

        private PauseMenuMouseListener pauseMenuMouseListener = new PauseMenuMouseListener();

        private HashMap<String, Button> buttons = new HashMap<>();

        PauseMenu() {
            buttons.put("resumeButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) - 25, 400, 75));
            buttons.put("restartButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 60, 400, 75));
            buttons.put("quitButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 145, 400, 75));
        }

        @Override
        public PauseMenu init() {
            MouseListeners.setMouseListener("pause", pauseMenuMouseListener);
            return this;
        }

        @Override
        public RenderObject getRenderObject() {
            return renderObject;
        }

        @Override
        public TickObject getTickObject() {
            return tickObject;
        }

        private class PauseMenuRenderer implements RenderObject {
            @Override
            public void render(Graphics2D g) {
                g.setColor(Colours.DARK_GREY_90);
                g.fillRect(0, 0, Configuration.getContentWidth(), Configuration.getContentHeight());

                g.setColor(Color.DARK_GRAY);
                buttons.values().forEach(g::fill);
                g.setColor(Color.BLACK);
                buttons.values().forEach(g::draw);

                g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
                g.setColor(Color.WHITE);
                g.drawString("Resume", (int)buttons.get("resumeButton").getCenterX(), (int)buttons.get("resumeButton").getCenterY());
                g.drawString("Restart", (int)buttons.get("restartButton").getCenterX(), (int)buttons.get("restartButton").getCenterY());
                g.drawString("Quit", (int)buttons.get("quitButton").getCenterX(), (int)buttons.get("quitButton").getCenterY());
            }
        }

        private class PauseMenuTicker implements TickObject {
            @Override
            public void tick() {
                for(Button button: buttons.values()) {
                    if(button.getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        Rizumu.getFrame().setCursor(Cursors.HAND_CURSOR);
                        break;
                    }
                }
            }
        }

        private class PauseMenuMouseListener extends MouseAdapter {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    // 'resume' button, close the pause menu
                    if(buttons.get("resumeButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        TrackScene.this.init();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.resumeExecution();
                        return;
                    }

                    // 'restart' button, restart the beatmap
                    if(buttons.get("restartButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        Rizumu.setSecondaryScene(null);
                        audioPlayer.stop();
                        ScheduleHandler.cancelExecution();

                        Track trakc = Rizumu.getTrackParser().parseTrack(track.getFile()); // forgive me for the horrible variable naming...
                        for(Beatmap baetmap: trakc.getBeatmaps()) {
                            if(baetmap.getName().equals(beatmap.getName())) {
                                Rizumu.setPrimaryScene(((TrackScene) Rizumu.getStaticScene(Scenes.TRACK)).initScene(trakc, baetmap).init());
                                return;
                            }
                        }
                        return;
                    }

                    // 'quit' button, go back to the main menu
                    if(buttons.get("quitButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        audioPlayer.stop();
                        ScheduleHandler.cancelExecution();
                        Rizumu.setSecondaryScene(null);
                        Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
                    }
                }
            }
        }
    }


}
