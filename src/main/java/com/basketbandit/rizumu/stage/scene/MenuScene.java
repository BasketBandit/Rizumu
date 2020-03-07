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
import com.basketbandit.rizumu.resource.Image;
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
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuScene extends Scene {
    private static HashMap<Integer, TrackButton> trackButtons = new HashMap<>(); // we use integer here to keep track of list ordering
    private Container trackContainer;

    private BufferedImage prevBackgroundImage;
    private BufferedImage menuBackgroundImage;
    private float menuBackgroundOpacity;
    private TrackButton selectedButton;

    public MenuScene() {
        renderObject = new MenuRenderer();
        tickObject = new MenuTicker();
        mouseAdapter = new MenuMouseAdapter();
        keyAdapter = new MenuKeyAdapter();

        trackContainer = new Container(0, 0, 490, Configuration.getHeight()).setColor(Colours.TRANSPARENT);
        AtomicInteger i = new AtomicInteger();
        Rizumu.getTrackParser().getTrackObjects().values().forEach(track -> track.getBeatmaps().forEach(beatmap -> {
            TrackButton t = (TrackButton) new TrackButton(0,2 + (67*i.get()), 460, 65, i.get(), track, beatmap).setColor(Colours.DARK_GREY_90).setButtonText(track.getArtist() + " - " + track.getName());
            buttons.put(i.get() + "", t);
            trackButtons.put(i.get(), t);
            i.getAndIncrement();
        }));

        buttons.put("directoryButton", new Button(Configuration.getWidth() - 170, Configuration.getHeight()- 70, 150, 50).setButtonText("Open Track Directory"));
    }

    @Override
    public MenuScene init(Object... object) {
        MouseAdapters.setMouseAdapter("menu", mouseAdapter);
        KeyAdapters.setKeyAdapter("menu", keyAdapter);

        menuBackgroundOpacity = 1.0f;

        // select middle track (5)
        if(trackButtons.size() > 0) {
            selectedButton = trackButtons.get(5);
            menuBackgroundImage = selectedButton.getTrack().getImage();
            audioPlayer.hotChangeTrack(selectedButton.getTrack().getAudioFilePath());
            audioPlayer.loop(-1);
        }
        return this;
    }

    private class MenuRenderer implements RenderObject {
        BufferedImage noSong = Image.getBufferedImage("no-song");

        @Override
        public void render(Graphics2D g) {
            if(prevBackgroundImage != null && menuBackgroundOpacity < 1.0f) {
                g.drawImage(prevBackgroundImage, null, null); // draw the previous background image to help achieve fade-in effect (white flashes without this)
            }

            if(menuBackgroundImage != null) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, menuBackgroundOpacity));
                g.drawImage(menuBackgroundImage, null, null);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }

            // draw if there are no parsed tracks
            if(trackButtons.size() == 0) {
                g.drawImage(noSong, AffineTransform.getTranslateInstance(Configuration.getWidth()/2.0 - noSong.getWidth()/2.0, (Configuration.getHeight()/2.0) - (noSong.getHeight()/2.0)), null);
            }

            int[] center = Alignment.centerBoth(buttons.get("directoryButton").getButtonText(), g.getFontMetrics(Fonts.default12), buttons.get("directoryButton"));
            g.setColor(buttons.get("directoryButton").getColor());
            g.fill(buttons.get("directoryButton"));
            g.setColor(Color.WHITE);
            g.drawString(buttons.get("directoryButton").getButtonText(), center[0], center[1]);

            // dynamic beatmap track buttons
            FontMetrics metrics = g.getFontMetrics(Fonts.default12);
            for(TrackButton t: trackButtons.values()) {
                t.setSize(t == selectedButton ? 480 : t.isHovered() ? 470 : 460, t.height); // width based on selected, hovered or normal button states
                center = Alignment.centerBoth(t.getButtonText(), metrics, t.x, t.y, t == selectedButton ? t.width + 20 : t.isHovered() ? t.width + 10 : t.width, t.height); // artificially increase t.width to exaggerate the movement of the text

                g.setColor((t == selectedButton) ? Colours.BLUE_75 : t.getColor());
                g.fillRect(t.x, t.y, t.width, t.height);
                g.setColor((t == selectedButton) ? Colours.BLUE : t.getColor());
                g.drawRect(t.x, t.y, t.width, t.height);

                g.setColor(Color.WHITE);
                g.drawString(t.getButtonText(), center[0], center[1]); // track title/artist
                g.drawString(t.getBeatmap().getName(), Alignment.right(t.getBeatmap().getName(), metrics, t.x, t.width) - 10, (int) t.getMinY() + 20); // beatmap difficulty
                g.drawString(t.getBeatmap().getKeys() + "K", Alignment.right(t.getBeatmap().getKeys() + "K", metrics, t.x, t.width) - 10, (int) t.getMaxY() - 10); // key count
            }

            if(selectedButton != null) {
                g.setColor(Colours.DARK_GREY_75);
                g.fillRect(Configuration.getWidth() - 200, 50, 200, 35);
                g.setColor(Color.WHITE);
                g.drawString(selectedButton.getTrack().getFormattedTrackLength() + "", Alignment.right(selectedButton.getTrack().getFormattedTrackLength() + "", metrics, 0, Configuration.getWidth()) - 10, 70); // track length
            }
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
            menuBackgroundOpacity += (menuBackgroundOpacity + 0.05f <= 1.0f) ? 0.05f : 1 - menuBackgroundOpacity; // background fade-in on selection
        }
    }

    public class MenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("directoryButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    try {
                        Desktop.getDesktop().open(new File(Configuration.getTracksPath()));
                    } catch(IOException ex) {
                        log.error("An error occurred trying to open song directory, message: {}", ex.getMessage(), ex);
                    }
                    return;
                }

                for(TrackButton t : trackButtons.values()) {
                    if(t.isHovered()) {
                        if(selectedButton.getId() == t.getId()) {
                            effectPlayer.play("menu-select2");
                            audioPlayer.stop();
                            Track track = Rizumu.getTrackParser().parseTrack(t.getTrack().getFile()); // re-parse the map
                            for(Beatmap beatmap : track.getBeatmaps()) {
                                if(beatmap.getName().equals(t.getBeatmap().getName())) {
                                    Rizumu.setPrimaryScene((Rizumu.getStaticScene(Scenes.TRACK)).init(track, beatmap).init());
                                    return;
                                }
                            }
                        } else {
                            effectPlayer.play("menu-click");
                            audioPlayer.hotChangeTrack(t.getTrack().getAudioFilePath());
                            selectedButton = t;
                            menuBackgroundOpacity = 0.05f;
                            prevBackgroundImage = menuBackgroundImage;
                            menuBackgroundImage = t.getTrack().getImage();
                            return;
                        }
                    }
                }
            }
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            // track list scrolling functionality
            if(trackContainer.getBounds().contains(e.getX(), e.getY())) {
                if(e.getWheelRotation() > 0) {
                    for(int i = trackButtons.size()-1; i > -1; i--) { // this specific loop needs to run in reverse to avoid a weird button creep bug?
                        if(trackButtons.get(0).getMinY() < 2) {
                            trackButtons.get(i).y += e.getUnitsToScroll() + 20; // speed boooost!
                        }
                    }
                } else if(e.getWheelRotation() < 0) {
                    for(int i = 0; i < trackButtons.size(); i++) {
                        if(trackButtons.get(trackButtons.size()-1).getMaxY() > Configuration.getHeight() - 2) {
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