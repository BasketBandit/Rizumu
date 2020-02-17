package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.TrackButton;
import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.input.MouseInput;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuScene implements Scene {
    private MenuRenderer renderObject = new MenuRenderer();
    private MenuTicker tickObject = new MenuTicker();

    private AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("menu");

    private Button frameRateButton = new Button(80, 140, 100, 50);
    private Button volumeUpButton = new Button(220, 140, 100, 50);
    private Button volumeDownButton = new Button(340, 140, 100, 50);
    private static ArrayList<TrackButton> trackButtons = new ArrayList<>();

    public MenuScene() {
        AtomicInteger i = new AtomicInteger();
        Rizumu.getTrackParser().getTrackObjects().values().forEach(track -> track.getBeatmaps().forEach(beatmap -> {
            TrackButton trackButton = new TrackButton(80,250+(50*i.get()), 250, 25, track.getFileName(), beatmap.getName());
            trackButton.setButtonText(track.getArtist() + " - " + track.getName() + " > " + beatmap.getName());
            trackButtons.add(trackButton);
            i.getAndIncrement();
        }));
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
            g.fill(frameRateButton);
            g.fill(volumeUpButton);
            g.fill(volumeDownButton);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Toggle Framerate Capping", (int)frameRateButton.getMinX(), (int)frameRateButton.getCenterY());
            g.drawString("Volume+ 5db", (int)volumeUpButton.getMinX(), (int)volumeUpButton.getCenterY());
            g.drawString("Volume- 5db", (int)volumeDownButton.getMinX(), (int)volumeDownButton.getCenterY());

            // dynamic beatmap track buttons
            for(TrackButton trackButton: trackButtons) {
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
            }

            if(MouseInput.isPressed(MouseEvent.BUTTON1)) {
                // ArrayList of beatmap buttons
                for(TrackButton trackButton: trackButtons) {
                    if(trackButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                        audioPlayer.pause();
                        TrackScene trackScene = (TrackScene) Rizumu.getStaticScene(Scenes.TRACK);
                        Track track = Rizumu.getTrackParser().parseTrack(trackButton.getTrack());
                        for(Beatmap beatmap: track.getBeatmaps()) {
                            if(beatmap.getName().equals(trackButton.getBeatmap())) {
                                Rizumu.setPrimaryScene(trackScene.initScene(track, beatmap));
                                return;
                            }
                        }
                    }
                }

                if(frameRateButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(volumeUpButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }

                if(volumeDownButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                }
            }
        }
    }

}