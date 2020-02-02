package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScene implements Scene {
    private MenuRender renderObject = new MenuRender();
    private MenuTicker tickObject = new MenuTicker();

    AudioPlayer audioPlayer;
    private Button button = new Button(80, 80, 100, 50);
    private Button frameRateButton = new Button(80, 140, 100, 50);
    private Button volumeUpButton = new Button(220, 140, 100, 50);
    private Button volumeDownButton = new Button(340, 140, 100, 50);

    public MenuScene() {
        audioPlayer = AudioPlayerController.getAudioPlayer("menu");
        audioPlayer.resume();
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class MenuRender implements RenderObject {
        private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        private Font[] fonts = ge.getAllFonts();

        @Override
        public void render(Graphics2D g) {
            g.setColor(button.getColor());
            g.fill(button);
            g.fill(frameRateButton);
            g.fill(volumeUpButton);
            g.fill(volumeDownButton);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Play Song!", (int)button.getMinX(), (int)button.getCenterY());
            g.drawString("Toggle Framerate Capping", (int)frameRateButton.getMinX(), (int)frameRateButton.getCenterY());
            g.drawString("Volume+ 5db", (int)volumeUpButton.getMinX(), (int)volumeUpButton.getCenterY());
            g.drawString("Volume- 5db", (int)volumeDownButton.getMinX(), (int)volumeDownButton.getCenterY());
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && button.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                audioPlayer.pause();
                Rizumu.engine.changeScene(new TrackScene(Rizumu.getBeatmaps().get(1)));
            }
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && frameRateButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                SystemConfiguration.toggleUnlockedFramerate();
            }
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && volumeUpButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                if(audioPlayer.getGain()+0.1 < 6.0206) {
                    audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                }
            }
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && volumeDownButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                audioPlayer.setGain(audioPlayer.getGain()-0.1f);
            }
        }
    }

}