package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.Container;
import com.basketbandit.rizumu.drawable.TrackButton;
import com.basketbandit.rizumu.input.KeyListeners;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.input.MouseMovementListener;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Cursors;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuScene implements Scene {
    private MenuRenderer renderObject = new MenuRenderer();
    private MenuTicker tickObject = new MenuTicker();

    private MenuMouseListener menuMouseListener = new MenuMouseListener();
    private MenuKeyListener menuKeyListener = new MenuKeyListener();

    private AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("music");
    private AudioPlayer effectPlayer = AudioPlayerController.getAudioPlayer("effect");

    private static HashMap<String, Button> buttons = new HashMap<>();
    private static HashMap<Integer, TrackButton> trackButtons = new HashMap<>(); // we use integer here to keep track of list ordering
    private Container container;

    private BufferedImage menuBackgroundImage;
    private TrackButton selectedButton;
    private String selectedBeatmap = "";

    public MenuScene() {
        container = new Container(0, 0, 500, Configuration.getContentHeight());

        buttons.put("frameRateButton", new Button(Configuration.getWidth() - 120, Configuration.getHeight() - 70, 100, 50));
        buttons.put("volumeUpButton", new Button(Configuration.getWidth() - 230, 20, 100, 50));
        buttons.put("volumeDownButton", new Button(Configuration.getWidth() - 340, 20, 100, 50));

        AtomicInteger i = new AtomicInteger();
        Rizumu.getTrackParser().getTrackObjects().values().forEach(track -> track.getBeatmaps().forEach(beatmap -> {
            TrackButton trackButton = new TrackButton(20,20+(85*i.get()), 460, 75, track, beatmap);
            trackButton.setButtonText(track.getArtist() + " - " + track.getName() + " > " + beatmap.getName());
            buttons.put(i.get() + "", trackButton);
            trackButtons.put(i.get(), trackButton);
            i.getAndIncrement();
        }));
    }

    @Override
    public MenuScene init() {
        MouseListeners.setMouseListener("menu", menuMouseListener);
        KeyListeners.setKeyListener("menu", menuKeyListener);

        // select random beatmap
        int rand = new Random().nextInt(trackButtons.size());
        Track buttonTrack = trackButtons.get(rand).getTrack();
        Beatmap buttonBeatmap = trackButtons.get(rand).getBeatmap();
        String trackName = buttonTrack.getArtist() + buttonTrack.getName() + buttonBeatmap.getName();
        menuBackgroundImage = buttonTrack.getImage();
        selectedButton = trackButtons.get(rand);
        selectedBeatmap = trackName;
        audioPlayer.hotChangeTrack(buttonTrack.getFilePath() + buttonTrack.getAudioFilename());
        audioPlayer.loop(-1);
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

    private class MenuRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            if(menuBackgroundImage != null) {
                g.drawImage(menuBackgroundImage, AffineTransform.getScaleInstance((Configuration.getWidth()+.0)/(menuBackgroundImage.getWidth()+.0), (Configuration.getHeight()+.0)/(menuBackgroundImage.getHeight()+.0)), null);
            }

            g.setColor(Colours.DARK_GREY_90);

            g.fill(buttons.get("frameRateButton"));
            //g.fill(buttons.get("volumeUpButton"));
            //g.fill(buttons.get("volumeDownButton"));

            g.setColor(Colours.DARK_GREY_75);
            g.fill(container);

            g.setFont(Fonts.default12);
            g.setColor(Color.WHITE);
            g.drawString("Cap Framerate", (int)buttons.get("frameRateButton").getMinX()+12, (int)buttons.get("frameRateButton").getCenterY()+2);
            //g.drawString("Vol +0.1db", (int)buttons.get("volumeUpButton").getMinX(), (int)buttons.get("volumeUpButton").getCenterY());
            //g.drawString("Vol -0.1db", (int)buttons.get("volumeDownButton").getMinX(), (int)buttons.get("volumeDownButton").getCenterY());

            // dynamic beatmap track buttons
            for(TrackButton trackButton: trackButtons.values()) {
                g.setColor(selectedButton == trackButton ? Colours.CRIMSON : Colours.DARK_GREY_90);
                g.fill(trackButton);
                g.setColor(Color.WHITE);
                g.drawString(trackButton.getButtonText(), (int)trackButton.getMinX(), (int)trackButton.getCenterY());
            }
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
            // dynamic cursor
            for(Button button: buttons.values()) {
                if(button.getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                    Rizumu.getFrame().setCursor(Cursors.HAND_CURSOR);
                    break;
                }
            }
        }
    }

    public class MenuMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                for(TrackButton trackButton : trackButtons.values()) {
                    if(trackButton.getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                        Track buttonTrack = trackButton.getTrack();
                        Beatmap buttonBeatmap = trackButton.getBeatmap();
                        String trackName = buttonTrack.getArtist() + buttonTrack.getName() + buttonBeatmap.getName();
                        if(selectedBeatmap.equals(trackName)) {
                            effectPlayer.play("menu-select2");
                            audioPlayer.stop();
                            Track track = Rizumu.getTrackParser().parseTrack(trackButton.getTrack().getFile()); // re-parse the map
                            for(Beatmap beatmap : track.getBeatmaps()) {
                                if(beatmap.getName().equals(trackButton.getBeatmap().getName())) {
                                    Rizumu.setPrimaryScene(((TrackScene) Rizumu.getStaticScene(Scenes.TRACK)).initScene(track, beatmap).init());
                                    return;
                                }
                            }
                        } else {
                            effectPlayer.play("menu-click");
                            audioPlayer.hotChangeTrack(buttonTrack.getFilePath() + buttonTrack.getAudioFilename());
                            menuBackgroundImage = buttonTrack.getImage();
                            selectedButton = trackButton;
                            selectedBeatmap = trackName;
                            return;
                        }
                    }
                }

                if(buttons.get("frameRateButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(buttons.get("volumeUpButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                    effectPlayer.play("menu-click");
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }

                if(buttons.get("volumeDownButton").getBounds().contains(MouseMovementListener.getX(), MouseMovementListener.getY())) {
                    effectPlayer.play("menu-click");
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // track list scrolling functionality
            if(container.getBounds().contains(e.getX(), e.getY())) {
                if(e.getWheelRotation() > 0) {
                    for(int i = trackButtons.size()-1; i > -1; i--) { // this specific loop needs to run in reverse to avoid a weird button creep bug?
                        if(trackButtons.get(0).getMinY() < 19) {
                            trackButtons.get(i).y += e.getUnitsToScroll() + 20; // speed boooost!
                        }
                    }
                } else if(e.getWheelRotation() < 0) {
                    for(int i = 0; i < trackButtons.size(); i++) {
                        if(trackButtons.get(trackButtons.size()-1).getMaxY() > Configuration.getHeight() - 19) {
                            trackButtons.get(i).y += e.getUnitsToScroll() - 20; // speed boooost!
                        }
                    }
                }
            }
        }
    }

    public class MenuKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                audioPlayer.stop();
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.SPLASH).init());
            }
        }
    }
}