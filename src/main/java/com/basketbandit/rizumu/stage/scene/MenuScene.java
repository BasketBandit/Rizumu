package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.Container;
import com.basketbandit.rizumu.drawable.TrackButton;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuScene extends Scene {
    private static HashMap<Integer, TrackButton> trackButtons = new HashMap<>(); // we use integer here to keep track of list ordering
    private Container container;

    private BufferedImage menuBackgroundImage;
    private TrackButton selectedButton;
    private String selectedBeatmap = "";

    public MenuScene() {
        renderObject = new MenuRenderer();
        tickObject = new MenuTicker();
        mouseAdapter = new MenuMouseAdapter();
        keyAdapter = new MenuKeyAdapter();

        container = new Container(0, 0, 500, Configuration.getContentHeight());

        buttons.put("frameRateButton", new Button(Configuration.getWidth() - 120, Configuration.getHeight() - 70, 100, 50));
        buttons.put("volumeUpButton", new Button(Configuration.getWidth() - 230, 20, 100, 50));
        buttons.put("volumeDownButton", new Button(Configuration.getWidth() - 340, 20, 100, 50));

        AtomicInteger i = new AtomicInteger();
        Rizumu.getTrackParser().getTrackObjects().values().forEach(track -> track.getBeatmaps().forEach(beatmap -> {
            TrackButton t = new TrackButton(20,20+(85*i.get()), 460, 75, track, beatmap);
            t.setButtonText(track.getArtist() + " - " + track.getName());
            buttons.put(i.get() + "", t);
            trackButtons.put(i.get(), t);
            i.getAndIncrement();
        }));
    }

    @Override
    public MenuScene init(Object... object) {
        MouseAdapters.setMouseAdapter("menu", mouseAdapter);
        KeyAdapters.setKeyAdapter("menu", keyAdapter);

        // select random beatmap
        int rand = new Random().nextInt(trackButtons.size());
        Track buttonTrack = trackButtons.get(rand).getTrack();
        Beatmap buttonBeatmap = trackButtons.get(rand).getBeatmap();
        String trackName = buttonTrack.getArtist() + buttonTrack.getName() + buttonBeatmap.getName();
        menuBackgroundImage = buttonTrack.getImage();
        selectedButton = trackButtons.get(rand);
        selectedBeatmap = trackName;
        audioPlayer.hotChangeTrack(buttonTrack.getAudioFilePath());
        audioPlayer.loop(-1);
        return this;
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
            FontMetrics metrics = g.getFontMetrics(Fonts.default12);
            for(TrackButton t: trackButtons.values()) {
                int[] center = Alignment.centerBoth(t.getButtonText(), metrics, t);

                g.setColor(selectedButton == t ? Colours.CRIMSON : Colours.DARK_GREY_90);
                g.fill(t);
                g.setColor(Color.WHITE);
                g.drawString(t.getButtonText(), center[0], center[1]); // draw track title/artist
                g.drawString(t.getBeatmap().getName(), Alignment.right(t.getBeatmap().getName(), metrics, t) - 10, (int)t.getMinY() + 20); // draw beatmap difficulty
                g.drawString(t.getBeatmap().getKeys() + "K", Alignment.right(t.getBeatmap().getKeys() + "K", metrics, t) - 10, (int)t.getMaxY() - 10); // draw key count
                g.drawString(t.getTrack().getFormattedTrackLength() + "", t.x + 10, (int)t.getMaxY() - 10);
            }
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
        }
    }

    public class MenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                for(TrackButton trackButton : trackButtons.values()) {
                    if(trackButton.isHovered()) {
                        Track buttonTrack = trackButton.getTrack();
                        Beatmap buttonBeatmap = trackButton.getBeatmap();
                        String trackName = buttonTrack.getArtist() + buttonTrack.getName() + buttonBeatmap.getName();
                        if(selectedBeatmap.equals(trackName)) {
                            effectPlayer.play("menu-select2");
                            audioPlayer.stop();
                            Track track = Rizumu.getTrackParser().parseTrack(trackButton.getTrack().getFile()); // re-parse the map
                            for(Beatmap beatmap : track.getBeatmaps()) {
                                if(beatmap.getName().equals(trackButton.getBeatmap().getName())) {
                                    Rizumu.setPrimaryScene((Rizumu.getStaticScene(Scenes.TRACK)).init(track, beatmap).init());
                                    return;
                                }
                            }
                        } else {
                            effectPlayer.play("menu-click");
                            audioPlayer.hotChangeTrack(buttonTrack.getAudioFilePath());
                            menuBackgroundImage = buttonTrack.getImage();
                            selectedButton = trackButton;
                            selectedBeatmap = trackName;
                            return;
                        }
                    }
                }

                if(buttons.get("frameRateButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(buttons.get("volumeUpButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }

                if(buttons.get("volumeDownButton").isHovered()) {
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

    public class MenuKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                audioPlayer.stop();
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.SPLASH).init());
            }
        }
    }
}