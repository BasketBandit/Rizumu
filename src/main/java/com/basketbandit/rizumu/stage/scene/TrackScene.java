package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.NoteParser;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.ExtendedRegistrar;
import com.basketbandit.rizumu.drawable.KeyFlash;
import com.basketbandit.rizumu.drawable.Registrar;
import com.basketbandit.rizumu.input.KeyListeners;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.input.MouseMovementListener;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapInitJob;
import com.basketbandit.rizumu.score.Statistics;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Cursors;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackScene implements Scene {
    private TrackRenderer renderObject = new TrackRenderer();
    private TrackTicker tickObject = new TrackTicker();
    private PauseMenu pauseMenu = new PauseMenu();
    private TrackKeyListener trackKeyListener = new TrackKeyListener();

    private AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("music");
    private AudioPlayer effectPlayer = AudioPlayerController.getAudioPlayer("effect");

    private Statistics statistics;

    protected Track track;
    protected Beatmap beatmap;
    private List<Note> notes;

    private Registrar registrar = new Registrar();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    private List<KeyFlash> hitKeyFlashes;

    private boolean menuCooldownWarning = false;
    private long menuCooldown = System.currentTimeMillis();

    public TrackScene initScene(Track track, Beatmap beatmap) {
        this.notes = new CopyOnWriteArrayList<>(); // Use this type of ArrayList to overcome concurrent modification exceptions. (it's costly, is this method suitable)
        this.track = track;
        this.beatmap = beatmap;
        this.statistics = new Statistics(track.getImage());

        this.hitKeyFlashes = new ArrayList<>();
        for(int i = 0; i < beatmap.getKeys(); i++) {
            hitKeyFlashes.add(new KeyFlash(NoteParser.getKey(i)));
        }

        this.extendedRegistrar.x = this.registrar.x = Configuration.getDefaultBeatmapXPosition(); // center the extended(registrar) based on key count
        this.extendedRegistrar.width = this.registrar.width = (beatmap.getKeys()*50) + (Configuration.getNoteGap()*beatmap.getKeys()-1) + (Configuration.getNoteGap()*4); // resize the extended(registrar) based on key count

        // do render object calculations on init rather that on the fly (this saves those precious cycles, right?)
        renderObject.backgroundImage = track.getImage();
        renderObject.backgroundImageTransform = AffineTransform.getScaleInstance((Configuration.getWidth()+.0)/(renderObject.backgroundImage.getWidth()+.0), (Configuration.getHeight()+.0)/(renderObject.backgroundImage.getHeight()+.0));
        renderObject.beatmapContainerXPosition = Configuration.getDefaultBeatmapXPosition(); // beatmap background container horizontal position
        renderObject.beatmapContainerWidth = (beatmap.getKeys()*50) + (Configuration.getNoteGap()*beatmap.getKeys()-1) + (Configuration.getNoteGap()*4); // beatmap background container width

        ScheduleHandler.registerUniqueJob(new BeatmapInitJob(this)); // load beatmap notes, start audio, etc.
        return this;
    }

    @Override
    public Scene init() {
        MouseListeners.setMouseListener("track", null);
        KeyListeners.setKeyListener("track", trackKeyListener);
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
        private BufferedImage backgroundImage;
        private AffineTransform backgroundImageTransform;
        private int beatmapContainerXPosition;
        private int beatmapContainerWidth;

        @Override
        public void render(Graphics2D g) {
            // background
            if(backgroundImage != null) {
                g.drawImage(backgroundImage, backgroundImageTransform, null);
                g.setColor(Colours.DARK_GREY_75);
                g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            }

            // beatmap track background
            g.setColor(Colours.DARK_GREY_75);
            g.fillRect(beatmapContainerXPosition, 0, beatmapContainerWidth, Configuration.getContentHeight());

            // extended registrar
            g.setColor(extendedRegistrar.getColor());
            g.fill(extendedRegistrar);

            // registrar
            g.setColor(registrar.getColor());
            g.fill(registrar);

            // note channels
            g.setColor(Colours.MEDIUM_GREY_10);
            for(int i = 0; i <= beatmap.getKeys(); i++) {
                g.fillRect((int) (beatmapContainerXPosition + Configuration.getNoteGap()*1.75 + ((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap())*i)), 0, 2, registrar.y); // *1.75 with width 2 seems to be the sweet spot for channel positions
            }

            // notes
            notes.stream().filter(note -> note.getMaxY() > 0).forEach(note -> {
                if(note.getNoteType().equals("single_long")) {
                    g.drawImage(note.getBody(), AffineTransform.getTranslateInstance(note.x + 3, note.y), null); // body
                    g.drawImage(note.getHead(), AffineTransform.getTranslateInstance(note.x, note.y + (note.height - Configuration.getDefaultNoteHeight())), null); // tail
                }
                g.drawImage(note.getHead(), AffineTransform.getTranslateInstance(note.x, note.y), null); // head
            });

            // hit flashes
            for(int i = 0; i < beatmap.getKeys(); i++) {
                int imageXPos = (int) (Configuration.getDefaultBeatmapXPosition() + Configuration.getNoteGap()*2.75 + (((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap()))*i)) - 1;
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hitKeyFlashes.get(i).getOpacity()));
                g.drawImage(hitKeyFlashes.get(i).getImage(), AffineTransform.getTranslateInstance(imageXPos, registrar.getY() - 150), null);
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

            // top bar
            g.setColor(Colours.DARK_GREY_75);
            g.fillRect(0, 0, Configuration.getContentWidth(), 50);

            // combo
            if(statistics.getCombo() >= 25) {
                g.setFont(fonts[368].deriveFont(Font.PLAIN, 36));
                g.setColor(Colours.CRIMSON);
                g.drawString(statistics.getCombo() + "", beatmapContainerXPosition + beatmapContainerWidth/2, Configuration.getHeight()/2);
            }

            // score
            g.setFont(fonts[368].deriveFont(Font.PLAIN,12));
            g.setColor(Colours.MEDIUM_GREY_100);
            g.drawString("%: " + new BigDecimal(statistics.getAccuracy()).setScale(2, RoundingMode.DOWN), 10, 40);

            // menu cooldown
            if(menuCooldownWarning) {
                g.drawString("Cannot pause for " + Math.floor((1 - (System.currentTimeMillis() - menuCooldown) / 1000.0) * 1000) / 1000 + " seconds!", 10, 70);  // truncates timer to 3dp
            }
        }
    }

    private class TrackTicker implements TickObject {
        @Override
        public void tick() {
            // if the secondary render object is NOT null (implies pause menu is open)
            if(!Rizumu.secondaryRenderObjectIsNull()) {
                return;
            }

            audioPlayer.resume();

            hitKeyFlashes.forEach(keyFlash -> {
                if(!trackKeyListener.isDown(keyFlash.getKey())) {
                    keyFlash.decrementOpacity();
                }
            });
            notes.forEach(note -> note.translate(0, Configuration.getNoteSpeedScale())); // translate each note in positive y

            // single was hit (hit on time)

            notes.removeIf(Note::hit);
            notes.stream().filter(note -> !note.hit()).forEach(note -> {
                if(note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && trackKeyListener.isDown(note.getKey()) && !note.isHeld()) || ((note.getMaxY() >= registrar.getMaxY()+25) && !note.isHeld())) {
                    if(statistics.getCombo() >= 50) {
                        effectPlayer.play("track-combobreak");
                    }
                    statistics.incrementMissed();
                }
            });
            notes.removeIf(note -> note.getMinY() >= registrar.getMaxY()); // single has passed the registrar (hit too late or not at all)
            notes.removeIf(note -> extendedRegistrar.intersects(note) && trackKeyListener.isDown(note.getKey()) && !note.isHeld()); // single_long has passed the registrar for the length of a single and the single_long isn't being held (hit too late)
            notes.removeIf(note -> (note.getMaxY() >= registrar.getMaxY()+25) && !note.isHeld());
        }
    }

    public class TrackKeyListener extends KeyAdapter {
        private static final int numKeys = 256;
        private final boolean[] keys = new boolean[numKeys];

        @Override
        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                menuCooldownWarning = (System.currentTimeMillis() - menuCooldown) < 1000;
                if(!menuCooldownWarning) {
                    audioPlayer.pause();
                    effectPlayer.play("menu-click");
                    menuCooldown = System.currentTimeMillis();
                    Rizumu.setSecondaryScene(pauseMenu.init());
                    ScheduleHandler.pauseExecution(); // Still possible to slightly dsync audio by spamming pause. (need to investigate)
                    return;
                }
            }

            notes.stream().filter(note -> note.getKey() == e.getKeyCode()).forEach(note -> {
                if(registrar.intersects(note) && !note.hit() && note.getNoteType().equals("single")) {
                    effectPlayer.play("track-hit");
                    note.setHit();
                    statistics.incrementHit();
                }

                // we use .getMinY() here because Y = 0 starts at the top left of the shape and extends downwards. (negative height to reverse this isn't possible)
                // TODO: make missing single_long increase miss counter
                if(note.getNoteType().equals("single_long") && registrar.intersects(note)) {
                    if(e.getKeyCode() == note.getKey() && ((note.getMaxY() <= registrar.getMaxY() + 23) && !note.isHeld())) {
                        note.setHeld();
                    }
                    if(e.getKeyCode() != note.getKey() && note.isHeld()) {
                        note.setHit();
                    }
                    if(e.getKeyCode() == note.getKey() && note.isHeld()) {
                        effectPlayer.play("track-hit");
                        statistics.incrementHit();
                    }
                }

                if(e.getKeyCode() == note.getKey() && (note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && !registrar.intersects(note))) && !note.hit() || ((note.getMaxY() >= registrar.getMaxY()+25) && !note.isHeld())) {
                    if(statistics.getCombo() >= 50) {
                        effectPlayer.play("track-combobreak");
                    }
                    statistics.incrementMissed();
                }
            });

            // reset hit flashes if key is pressed
            hitKeyFlashes.stream().filter(keyFlash -> e.getKeyCode() == keyFlash.getKey()).forEach(KeyFlash::resetOpacity);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;
        }


        public boolean isDown(int keyCode) {
            return keys[keyCode];
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
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    // 'resume' button, close the pause menu
                    if(buttons.get("resumeButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        audioPlayer.resume();
                        effectPlayer.play("menu-click");
                        TrackScene.this.init();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.resumeExecution();
                        menuCooldown = System.currentTimeMillis();
                        return;
                    }

                    // 'restart' button, restart the beatmap
                    if(buttons.get("restartButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        effectPlayer.play("menu-click");
                        audioPlayer.stop();
                        Rizumu.setSecondaryScene(null);
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
                        effectPlayer.play("menu-click");
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
