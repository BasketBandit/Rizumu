package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.Container;
import com.basketbandit.rizumu.drawable.CursorContainer;
import com.basketbandit.rizumu.drawable.TrackButton;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.input.MouseInput;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuScene implements Scene {
    private MenuRenderer renderObject = new MenuRenderer();
    private MenuTicker tickObject = new MenuTicker();

    private AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("menu");

    private static HashMap<String, Button> buttons = new HashMap<>();
    private static HashMap<Integer, TrackButton> trackButtons = new HashMap<>(); // we use integer here to keep track of list ordering
    private Container container;

    public MenuScene() {
        buttons.put("frameRateButton", new Button(Configuration.getContentWidth()-120, 20, 100, 50));
        buttons.put("volumeUpButton", new Button(Configuration.getContentWidth()-230, 20, 100, 50));
        buttons.put("volumeDownButton", new Button(Configuration.getContentWidth()-340, 20, 100, 50));

        AtomicInteger i = new AtomicInteger();
        Rizumu.getTrackParser().getTrackObjects().values().forEach(track -> track.getBeatmaps().forEach(beatmap -> {
            TrackButton trackButton = new TrackButton(20,20+(60*i.get()), 460, 50, track.getFileName(), beatmap.getName());
            trackButton.setButtonText(track.getArtist() + " - " + track.getName() + " > " + beatmap.getName());
            buttons.put(track.getName() + " > " + beatmap.getName(), trackButton);
            trackButtons.put(i.get(), trackButton);
            i.getAndIncrement();
        }));

        container = new Container(0, 0, 500, Configuration.getContentHeight());
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
            g.setColor(Color.DARK_GRAY);
            g.fill(buttons.get("frameRateButton"));
            g.fill(buttons.get("volumeUpButton"));
            g.fill(buttons.get("volumeDownButton"));
            g.fill(container);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Framerate Capping", (int)buttons.get("frameRateButton").getMinX(), (int)buttons.get("frameRateButton").getCenterY());
            g.drawString("Vol +0.1db", (int)buttons.get("volumeUpButton").getMinX(), (int)buttons.get("volumeUpButton").getCenterY());
            g.drawString("Vol -0.1db", (int)buttons.get("volumeDownButton").getMinX(), (int)buttons.get("volumeDownButton").getCenterY());

            // dynamic beatmap track buttons
            for(TrackButton trackButton: trackButtons.values()) {
                g.setColor(trackButton.getColor());
                g.fill(trackButton);
                g.setColor(Color.WHITE);
                g.drawString(trackButton.getButtonText(), (int)trackButton.getMinX(), (int)trackButton.getCenterY());
            }
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
            audioPlayer.resume();

            if(KeyInput.wasPressed(KeyEvent.VK_ESCAPE)) {
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.SPLASH));
                return;
            }

            // dynamic cursor
            for(Button button: buttons.values()) {
                if(button.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    Rizumu.getFrame().setCursor(CursorContainer.HAND_CURSOR);
                    break;
                }
            }

            // track list scrolling functionality
            if(container.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                if(MouseInput.getMouseWheelRotation() > 0) {
                    for(int i = trackButtons.size()-1; i > -1; i--) { // this specific loop needs to run in reverse to avoid a weird button creep bug?
                        if(trackButtons.get(0).getMinY() < 19) {
                            trackButtons.get(i).y += MouseInput.getMouseWheelScrollUnits();
                        }
                    }
                } else if(MouseInput.getMouseWheelRotation() < 0) {
                    for(int i = 0; i < trackButtons.size(); i++) {
                        if(trackButtons.get(trackButtons.size()-1).getMinY() > 19) {
                            trackButtons.get(i).y += MouseInput.getMouseWheelScrollUnits();
                        }
                    }
                }
            }

            if(MouseInput.isPressed(MouseEvent.BUTTON1)) {
                if(buttons.get("frameRateButton").getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(buttons.get("volumeUpButton").getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }

                if(buttons.get("volumeDownButton").getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }
            }

            if(MouseInput.isPressed(MouseEvent.BUTTON1)) {
                // ArrayList of beatmap buttons
                for(TrackButton trackButton : trackButtons.values()) {
                    if(trackButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                        audioPlayer.pause();
                        TrackScene trackScene = (TrackScene) Rizumu.getStaticScene(Scenes.TRACK);
                        Track track = Rizumu.getTrackParser().parseTrack(trackButton.getTrack());
                        for(Beatmap beatmap : track.getBeatmaps()) {
                            if(beatmap.getName().equals(trackButton.getBeatmap())) {
                                Rizumu.setPrimaryScene(trackScene.initScene(track, beatmap));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

}