package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
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
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrackScene extends Scene {
    private PauseMenu pauseMenu = new PauseMenu();

    private Statistics statistics;

    protected Track track;
    protected Beatmap beatmap;
    private CopyOnWriteArrayList<Note> notes;

    private Registrar registrar = new Registrar();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    private List<KeyFlash> hitKeyFlashes;

    private boolean menuCooldownWarning = false;
    private long menuCooldown = System.currentTimeMillis();

    public TrackScene() {
        renderObject = new TrackRenderer();
        tickObject = new TrackTicker();
        keyAdapter = new TrackKeyListener();
    }

    @Override
    public Scene init(Object... object) {
        MouseListeners.setMouseListener("track", null);
        KeyListeners.setKeyListener("track", keyAdapter);

        if(object.length > 0) {
            this.track = (Track) object[0];
            this.beatmap = (Beatmap) object[1];
            this.notes = new CopyOnWriteArrayList<>(); // Use this type of ArrayList to overcome concurrent modification exceptions. (it's costly, is this method suitable)
            this.statistics = new Statistics(track.getImage());

            this.hitKeyFlashes = new ArrayList<>();
            for(int i = 0; i < beatmap.getKeys(); i++) {
                hitKeyFlashes.add(new KeyFlash(NoteParser.getKey(i)));
            }

            this.extendedRegistrar.x = this.registrar.x = Configuration.getDefaultBeatmapXPosition(); // center the extended(registrar) based on key count
            this.extendedRegistrar.width = this.registrar.width = (beatmap.getKeys() * 50) + (Configuration.getNoteGap() * beatmap.getKeys() - 1) + (Configuration.getNoteGap() * 4); // resize the extended(registrar) based on key count

            // do render object calculations on init rather that on the fly (this saves those precious cycles, right?)
            ((TrackRenderer) renderObject).backgroundImage = track.getImage();
            ((TrackRenderer) renderObject).backgroundImageTransform = AffineTransform.getScaleInstance((Configuration.getWidth() + .0) / (((TrackRenderer) renderObject).backgroundImage.getWidth() + .0), (Configuration.getHeight() + .0) / (((TrackRenderer) renderObject).backgroundImage.getHeight() + .0));
            ((TrackRenderer) renderObject).beatmapContainer.x = Configuration.getDefaultBeatmapXPosition(); // beatmap background container horizontal position
            ((TrackRenderer) renderObject).beatmapContainer.width = (beatmap.getKeys() * 50) + (Configuration.getNoteGap() * beatmap.getKeys() - 1) + (Configuration.getNoteGap() * 4); // beatmap background container width

            ScheduleHandler.registerUniqueJob(new BeatmapInitJob(this)); // load beatmap notes, start audio, etc.
        }

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

    private class TrackRenderer implements RenderObject {
        private BufferedImage backgroundImage;
        private AffineTransform backgroundImageTransform;
        private Rectangle beatmapContainer = new Rectangle(0, Configuration.getHeight());

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
            g.fill(beatmapContainer);

            // extended registrar
            g.setColor(extendedRegistrar.getColor());
            g.fill(extendedRegistrar);

            // registrar
            g.setColor(registrar.getColor());
            g.fill(registrar);

            // note channels
            g.setColor(Colours.MEDIUM_GREY_10);
            for(int i = 0; i <= beatmap.getKeys(); i++) {
                g.fillRect(beatmapContainer.x + (int)(Configuration.getNoteGap()*1.75) + ((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap())*i), 0, 2, registrar.y); // *1.75 with width 2 seems to be the sweet spot for channel positions
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
                int imageXPos = (Configuration.getDefaultBeatmapXPosition() + (int)(Configuration.getNoteGap()*2.50) + ((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap())*i));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hitKeyFlashes.get(i).getOpacity()));
                g.drawImage(hitKeyFlashes.get(i).getImage(), AffineTransform.getTranslateInstance(imageXPos, registrar.getY() - 150), null);
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

            // top bar
            g.setColor(Colours.DARK_GREY_75);
            g.fillRect(0, 0, Configuration.getContentWidth(), 50);

            // combo
            if(statistics.getCombo() >= 10) {
                g.setFont(Fonts.default36);
                g.setColor(Colours.CRIMSON);
                g.drawString(statistics.getCombo() + "", Alignment.center(statistics.getCombo() + "", g.getFontMetrics(Fonts.default36), beatmapContainer), Configuration.getHeight()/2);
            }

            // score
            g.setFont(Fonts.default12);
            g.setColor(Colours.MEDIUM_GREY_100);
            g.drawString("%: " + new BigDecimal(statistics.getAccuracy()).setScale(2, RoundingMode.DOWN).doubleValue(), 10, 40);

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

            menuCooldownWarning = (System.currentTimeMillis() - menuCooldown) < 1000;

            audioPlayer.resume();

            hitKeyFlashes.forEach(keyFlash -> {
                if(!((TrackKeyListener) keyAdapter).isDown(keyFlash.getKey())) {
                    keyFlash.decrementOpacity();
                }
            });
            notes.forEach(note -> note.translate(0, Configuration.getNoteSpeedScale())); // translate each note in positive y

            // TODO: work on hit registration (something is off)
            // single was hit (hit on time)
            notes.removeIf(Note::hit);
            notes.stream().filter(note -> !note.hit()).forEach(note -> {
                if(note.getMinY() >= registrar.getMaxY() || (extendedRegistrar.intersects(note) && ((TrackKeyListener) keyAdapter).isDown(note.getKey()) && !note.isHeld()) || ((note.getMaxY() >= registrar.getMaxY()+25) && !note.isHeld())) {
                    if(statistics.getCombo() >= 50) {
                        effectPlayer.play("track-combobreak");
                    }
                    statistics.incrementMissed();
                }
            });
            notes.removeIf(note -> note.getMinY() >= registrar.getMaxY()); // single has passed the registrar (hit too late or not at all)
            notes.removeIf(note -> extendedRegistrar.intersects(note) && ((TrackKeyListener) keyAdapter).isDown(note.getKey()) && !note.isHeld()); // single_long has passed the registrar for the length of a single and the single_long isn't being held (hit too late)
            notes.removeIf(note -> (note.getMaxY() >= registrar.getMaxY()+25) && !note.isHeld());
        }
    }

    public class TrackKeyListener extends KeyAdapter {
        private static final int numKeys = 256;
        private final boolean[] keys = new boolean[numKeys];

        @Override
        public void keyPressed(KeyEvent e) {
            keys[e.getKeyCode()] = true;

            if(keys[KeyEvent.VK_ESCAPE]) {
                if(!menuCooldownWarning) {
                    audioPlayer.pause();
                    effectPlayer.play("menu-click");
                    menuCooldown = System.currentTimeMillis();
                    menuCooldownWarning = true;
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
                    if(keys[note.getKey()] && ((note.getMaxY() <= registrar.getMaxY() + 23) && !note.isHeld())) {
                        note.setHeld();
                    }
                    if(!keys[note.getKey()] && note.isHeld()) {
                        note.setHit();
                    }
                    if(keys[note.getKey()] && note.isHeld()) {
                        effectPlayer.play("track-hit");
                        statistics.incrementHit();
                    }
                }
            });

            // reset hit flashes if key is pressed
            hitKeyFlashes.stream().filter(keyFlash -> keys[keyFlash.getKey()]).forEach(KeyFlash::resetOpacity);
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
    public class PauseMenu extends Scene {
        PauseMenu() {
            renderObject = new PauseMenuRenderer();
            tickObject = new PauseMenuTicker();
            mouseAdapter = new PauseMenuMouseListener();

            buttons.put("resumeButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) - 25, 400, 75));
            buttons.put("restartButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 60, 400, 75));
            buttons.put("quitButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 145, 400, 75));
        }

        @Override
        public PauseMenu init(Object... objects) {
            MouseListeners.setMouseListener("pause", mouseAdapter);
            return this;
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

                g.setFont(Fonts.default12);
                g.setColor(Color.WHITE);
                g.drawString("Resume", Alignment.center("Resume", g.getFontMetrics(Fonts.default12), buttons.get("resumeButton")), (int)buttons.get("resumeButton").getCenterY()+4);
                g.drawString("Restart", Alignment.center("Restart", g.getFontMetrics(Fonts.default12), buttons.get("restartButton")), (int)buttons.get("restartButton").getCenterY()+4);
                g.drawString("Quit", Alignment.center("Quit", g.getFontMetrics(Fonts.default12), buttons.get("quitButton")), (int)buttons.get("quitButton").getCenterY()+4);
            }
        }

        private class PauseMenuTicker implements TickObject {
            @Override
            public void tick() {
            }
        }

        private class PauseMenuMouseListener extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    // 'resume' button, close the pause menu
                    if(buttons.get("resumeButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        audioPlayer.resume();
                        effectPlayer.play("menu-click2");
                        TrackScene.this.init();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.resumeExecution();
                        menuCooldown = System.currentTimeMillis();
                        return;
                    }

                    // 'restart' button, restart the beatmap
                    if(buttons.get("restartButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        effectPlayer.play("menu-click3");
                        audioPlayer.stop();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.cancelExecution();

                        Track trakc = Rizumu.getTrackParser().parseTrack(track.getFile()); // forgive me for the horrible variable naming...
                        for(Beatmap baetmap: trakc.getBeatmaps()) {
                            if(baetmap.getName().equals(beatmap.getName())) {
                                Rizumu.setPrimaryScene((Rizumu.getStaticScene(Scenes.TRACK)).init(trakc, baetmap).init());
                                return;
                            }
                        }
                        return;
                    }

                    // 'quit' button, go back to the main menu
                    if(buttons.get("quitButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        effectPlayer.play("menu-click4");
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
