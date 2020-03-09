package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.beatmap.NoteParser;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Note;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.Container;
import com.basketbandit.rizumu.drawable.track.*;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.scheduler.jobs.BeatmapInitJob;
import com.basketbandit.rizumu.score.Score;
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

    private Score score;

    protected Track track;
    protected Beatmap beatmap;
    private CopyOnWriteArrayList<Note> notes;

    private Container progressBar = new Container(0, 0, 0, 50).setColor(Colours.BLUE_50);
    private List<KeyFlash> hitKeyFlashes;
    private AccuracyLabel accuracyLabel;

    private RegistrarMx registrarMx = new RegistrarMx();
    private RegistrarEx registrarEx = new RegistrarEx();
    private RegistrarNm registrarNm = new RegistrarNm();
    private ExtendedRegistrar extendedRegistrar = new ExtendedRegistrar();

    private boolean menuCooldownWarning = false;
    private long menuCooldown = System.currentTimeMillis();

    public TrackScene() {
        renderObject = new TrackRenderer();
        tickObject = new TrackTicker();
        keyAdapter = new TrackKeyAdapter();
    }

    @Override
    public Scene init(Object... object) {
        MouseAdapters.setMouseAdapter("track", null);
        KeyAdapters.setKeyAdapter("track", keyAdapter);

        if(object.length > 0) {
            this.track = (Track) object[0];
            this.beatmap = (Beatmap) object[1];
            this.notes = new CopyOnWriteArrayList<>(); // Use this type of ArrayList to overcome concurrent modification exceptions. (it's costly, is this method suitable)
            this.score = new Score(track, beatmap);

            this.hitKeyFlashes = new ArrayList<>();
            for(int i = 0; i < beatmap.getKeys(); i++) {
                hitKeyFlashes.add(new KeyFlash(NoteParser.getKey(i)));
            }

            this.accuracyLabel = new AccuracyLabel();
            this.progressBar = new Container(0, 0, 0, 50).setColor(Colours.BLUE_50);

            this.accuracyLabel.width = this.registrarMx.width = this.registrarEx.width = this.registrarNm.width = this.extendedRegistrar.width = (beatmap.getKeys() * 50) + (Configuration.getNoteGap() * beatmap.getKeys() - 1) + (Configuration.getNoteGap() * 4); // resize the extended(registrar) based on key count

            // do render object calculations on init rather that on the fly (this saves those precious cycles, right?)
            ((TrackRenderer) renderObject).backgroundImage = track.getImage();
            ((TrackRenderer) renderObject).beatmapContainer.x = Configuration.getDefaultBeatmapXPosition(); // beatmap background container horizontal position
            ((TrackRenderer) renderObject).beatmapContainer.width = (beatmap.getKeys() * 50) + (Configuration.getNoteGap() * beatmap.getKeys() - 1) + (Configuration.getNoteGap() * 4); // beatmap background container width
            ((TrackRenderer) renderObject).titleBar = new Container(0, 0, Configuration.getWidth(), 50);

            ScheduleHandler.registerUniqueJob(new BeatmapInitJob(this)); // load beatmap notes, start audio, etc.
        }

        return this;
    }

    public Score getScore() {
        return score;
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
        private Container beatmapContainer = new Container(0, 0, 0, Configuration.getHeight());
        private Container titleBar = new Container(0, 0, Configuration.getWidth(), 50);
        private FontMetrics metrics16;
        private FontMetrics metrics24;
        private FontMetrics metrics36;

        @Override
        public void render(Graphics2D g) {
            // system
            if(metrics24 == null) {
                metrics16 = g.getFontMetrics(Fonts.default16);
                metrics24 = g.getFontMetrics(Fonts.default24);
                metrics36 = g.getFontMetrics(Fonts.default36);
            }

            // background
            if(backgroundImage != null) {
                g.drawImage(backgroundImage, null, null);
                g.setColor(Colours.DARK_GREY_75);
                g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            }

            // beatmap track background
            g.setColor(Colours.DARK_GREY_75);
            g.fill(beatmapContainer);

            // registrar
            g.setColor(registrarMx.getColor());
            g.fill(registrarMx);

            // note channels
            g.setColor(Colours.MEDIUM_GREY_10);
            for(int i = 0; i <= beatmap.getKeys(); i++) {
                g.fillRect(beatmapContainer.x + (int)(Configuration.getNoteGap()*1.75) + ((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap())*i), 0, 2, registrarMx.y); // *1.75 with width 2 seems to be the sweet spot for channel positions
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
            for(int i = 0; i < hitKeyFlashes.size(); i++) {
                int imageXPos = (Configuration.getDefaultBeatmapXPosition() + (int)(Configuration.getNoteGap()*2.50) + ((Configuration.getDefaultNoteWidth()+Configuration.getNoteGap())*i));
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hitKeyFlashes.get(i).getOpacity()));
                g.drawImage(hitKeyFlashes.get(i).getImage(), imageXPos, (int) (registrarMx.getY() - 150), null);
            }
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); // make sure to reset opacity

            // sub-registrar decoration/shield
            g.setColor(Colours.BLUE_75);
            g.fillRect(extendedRegistrar.x, extendedRegistrar.y - 15, extendedRegistrar.width, extendedRegistrar.height);

            // top bar + score + track progress
            g.setColor(Colours.DARK_GREY_75);
            g.fill(titleBar);

            g.setColor(progressBar.getColor());
            g.fill(progressBar); // progress

            g.setColor(Color.WHITE);
            g.setFont(Fonts.default12);
            g.drawString(score.getScore() + " (x" + score.getMultiplier() + ")", 10, 20);
            g.drawString("%: " + BigDecimal.valueOf(score.getAccuracy()).setScale(2, RoundingMode.DOWN).doubleValue(), 10, 40); // score

            g.setFont(Fonts.default16);
            int[] center = Alignment.centerBoth(track.getName() + " - " + beatmap.getName(), metrics16, titleBar);
            g.drawString(track.getName() + " - " + beatmap.getName(), center[0], center[1]);

            // combo
            if(score.getCombo() >= 10) {
                g.setFont(Fonts.default36);
                g.setColor(Colours.CRIMSON);
                center = Alignment.centerBoth(score.getCombo() + "", metrics36, beatmapContainer);
                g.drawString(score.getCombo() + "", center[0], 150);
            }

            // accuracy label
            g.setFont(Fonts.default24);
            g.setColor(accuracyLabel.getColor());
            center = Alignment.centerBoth(accuracyLabel.getText(), metrics24, accuracyLabel);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, accuracyLabel.getOpacity()));
            g.drawString(accuracyLabel.getText(), center[0], center[1]);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1)); // make sure to reset opacity

            // menu cooldown
            if(menuCooldownWarning) {
                g.setFont(Fonts.default12);
                g.setColor(Color.WHITE);
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
            menuCooldownWarning = (System.currentTimeMillis() - menuCooldown) < 1000;

            // update progress bar
            progressBar.width = (int) ((Configuration.getWidth()/100.0) * audioPlayer.getClipPosition());

            // stuff related to opacity
            hitKeyFlashes.forEach(keyFlash -> {
                if(!((TrackKeyAdapter) keyAdapter).isDown(keyFlash.getKey())) {
                    keyFlash.decrementOpacity();
                }
            });
            accuracyLabel.decrementOpacity();

            notes.forEach(note -> {
                boolean missed = false;

                // translate all of the notes in positive Y
                note.translate(0, Configuration.getNoteSpeedScale());

                // check single notes and the tails of single_long notes
                if(note.getMinY() >= extendedRegistrar.getMinY()) {
                    missed = true;
                    notes.remove(note);
                }

                // using note.getMaxY() instead of note.getMinY() allows us to check the head of single_long...
                // ...with only the addition of !note.isMissed() and !note.isHeld() which prevents erroneous misses every tick for the duration of the single_long
                // This statement should never be reached by a single note and only single_long due to the previous statement.
                if((note.getMaxY() - Configuration.getDefaultNoteHeight()) >= extendedRegistrar.getMinY() && !note.isHeld() && !note.isMissed() ) {
                    missed = true;
                    note.setMissed();
                    note.setColor(Colours.DARK_GREY_50);
                }

                if(missed) {
                    if(score.getCombo() >= 50) {
                        effectPlayer.play("track-combobreak");
                    }
                    score.incrementMissed();
                    accuracyLabel.setState("MISSED", Colours.CRIMSON);
                }
            });
        }
    }

    public class TrackKeyAdapter extends KeyAdapter {
        private static final int numKeys = 256;
        private boolean[] keys = new boolean[numKeys];

        @Override
        public void keyPressed(KeyEvent e) {
            if(keys[e.getKeyCode()]) {
                return; // acts as a lock to prevent holding keys and hitting notes
            }

            keys[e.getKeyCode()] = true;

            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if(!menuCooldownWarning) {
                    effectPlayer.play("menu-click");
                    menuCooldownWarning = ((menuCooldown = System.currentTimeMillis()) != 0); // != because we always want this value to return true; this is a quick and dirty way of setting both fields at once.
                    ScheduleHandler.pauseExecution(); // Still possible to slightly dsync audio by spamming pause. (need to investigate)
                    Rizumu.setSecondaryScene(pauseMenu.init());
                    return;
                }
            }

            notes.stream().filter(note -> note.getKey() == e.getKeyCode() && !note.isHit()).forEach(note -> {
                // create a rectangle which represents the head of a single_long OR just a regular single
                Rectangle head = new Rectangle(note.x, (int) note.getMaxY() - Configuration.getDefaultNoteHeight(), note.width, Configuration.getDefaultNoteHeight());

                if(registrarNm.intersects(head) || registrarEx.intersects(head) || registrarMx.intersects(head)) {
                    effectPlayer.play("track-hit");

                    // decides the accuracy of the hit note, starts checking the most accurate first, since note height is larger than any individual accuracy zone...
                    // ...which means that if the checks were in reverse, you would only hit NM and never EX, or MX.
                    if(registrarMx.intersects(head)) {
                        score.incrementMxHit();
                        accuracyLabel.setState("MX", Colours.GOLD);
                    } else if(registrarEx.intersects(head)) {
                        score.incrementExHit();
                        accuracyLabel.setState("EX", Color.GREEN);
                    } else {
                        score.incrementNmHit();
                        accuracyLabel.setState("NM", Colours.BLUE);
                    }

                    // don't remove the single_long if you miss the head, because it's fairly jarring to see a huge note just disappear.
                    // Instead we grey it out and leave it alone until the tail reaches the extended registrar.
                    if(note.getNoteType().equals("single")) {
                        notes.remove(note);
                    } else {
                        note.setHit();
                        note.setHeld();
                    }
                }
            });

            // reset hit flashes if key is pressed
            hitKeyFlashes.stream().filter(keyFlash -> keys[keyFlash.getKey()]).forEach(KeyFlash::resetOpacity);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            keys[e.getKeyCode()] = false;

            // process the release of single_long
            notes.stream().filter(note -> note.getNoteType().equals("single_long") && note.getKey() == e.getKeyCode()).forEach(note -> {
                // create a rectangle which represents the tail of a single_long.
                Rectangle tail = new Rectangle(note.x, note.y, note.width, Configuration.getDefaultNoteHeight());

                if(note.isHeld() && !note.isMissed()) {
                    if((registrarNm.intersects(tail) || registrarEx.intersects(tail) || registrarMx.intersects(tail))) {
                        effectPlayer.play("track-hit");

                        if(registrarMx.intersects(tail)) {
                            score.incrementMxHit();
                            accuracyLabel.setState("MX", Colours.GOLD);
                        } else if(registrarEx.intersects(tail)) {
                            score.incrementExHit();
                            accuracyLabel.setState("EX", Color.GREEN);
                        } else {
                            score.incrementNmHit();
                            accuracyLabel.setState("NM", Colours.BLUE);
                        }

                        notes.remove(note);
                    } else {
                        // prevents releasing key on single_long notes and then re-pressing it at the end for a hit
                        note.setMissed();
                        note.setColor(Colours.DARK_GREY_50);
                    }
                }
            });
        }

        public boolean isDown(int keyCode) {
            return keys[keyCode];
        }
    }

    /**
     * PauseMenu subclass, not given a separate class file due to it only being used in the context of a TrackScene.
     */
    public class PauseMenu extends Scene {
        FontMetrics metrics16;

        PauseMenu() {
            renderObject = new PauseMenuRenderer();
            tickObject = new PauseMenuTicker();
            mouseAdapter = new PauseMenuMouseAdapter();

            buttons.put("resumeButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) - 25, 400, 75));
            buttons.put("restartButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 60, 400, 75));
            buttons.put("quitButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 145, 400, 75));
        }

        @Override
        public PauseMenu init(Object... objects) {
            MouseAdapters.setMouseAdapter("pause", mouseAdapter);
            KeyAdapters.setKeyAdapter("pause", null);

            audioPlayer.pause();
            return this;
        }

        private class PauseMenuRenderer implements RenderObject {
            @Override
            public void render(Graphics2D g) {
                if(metrics16 == null) {
                    metrics16 = g.getFontMetrics(Fonts.default16);
                }

                g.setColor(Colours.DARK_GREY_90);
                g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

                g.setColor(Color.BLACK);
                buttons.values().forEach(g::draw);
                g.setColor(Color.DARK_GRAY);
                buttons.values().forEach(g::fill);

                g.setFont(Fonts.default16);
                g.setColor(Color.WHITE);
                int[] center = Alignment.centerBoth("Resume", g.getFontMetrics(Fonts.default16), buttons.get("resumeButton"));
                g.drawString("Resume", center[0], center[1]);
                center = Alignment.centerBoth("Restart", g.getFontMetrics(Fonts.default16), buttons.get("restartButton"));
                g.drawString("Restart", center[0], center[1]);
                center = Alignment.centerBoth("Quit", g.getFontMetrics(Fonts.default16), buttons.get("quitButton"));
                g.drawString("Quit", center[0], center[1]);
            }
        }

        private class PauseMenuTicker implements TickObject {
            @Override
            public void tick() {
            }
        }

        private class PauseMenuMouseAdapter extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    // 'resume' button, close the pause menu
                    if(buttons.get("resumeButton").isHovered()) {
                        audioPlayer.resume();
                        effectPlayer.play("menu-click2");
                        TrackScene.this.init();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.resumeExecution();
                        menuCooldown = System.currentTimeMillis();
                        return;
                    }

                    // 'restart' button, restart the beatmap
                    if(buttons.get("restartButton").isHovered()) {
                        effectPlayer.play("menu-click3");
                        audioPlayer.stop();
                        Rizumu.setSecondaryScene(null);
                        ScheduleHandler.cancelExecution();

                        Track t = Rizumu.getTrackParser().parseTrack(track.getFile()); // forgive me for the horrible variable naming...
                        for(Beatmap b: t.getBeatmaps()) {
                            if(b.getName().equals(beatmap.getName())) {
                                Rizumu.setPrimaryScene((Rizumu.getStaticScene(Scenes.TRACK)).init(t, b));
                                return;
                            }
                        }
                        return;
                    }

                    // 'quit' button, go back to the main menu
                    if(buttons.get("quitButton").isHovered()) {
                        effectPlayer.play("menu-click4");
                        audioPlayer.stop();
                        ScheduleHandler.cancelExecution();
                        Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
                        Rizumu.setSecondaryScene(null);
                    }
                }
            }
        }
    }
}
