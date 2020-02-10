package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.input.MouseInput;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class SplashScene implements Scene {
    private SplashRenderer renderObject = new SplashRenderer();
    private SplashTicker tickObject = new SplashTicker();

    private BufferedImage logo;
    private int x = 0;

    public SplashScene() {
        try {
            logo = ImageIO.read(new File("src/main/resources/assets/logo.png"));
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("menu");
        audioPlayer.changeTrack("src/main/resources/assets/menu.wav");
        audioPlayer.loop(-1);
        audioPlayer.play();
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class SplashRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.drawImage(logo, (SystemConfiguration.getWidth()/2)-(logo.getWidth()/4)-(x/4), (SystemConfiguration.getHeight()/2)-(logo.getHeight()/4)-50-(x/2), (logo.getWidth()/2)+(x/2), (logo.getHeight()/2)+(x/2), null); // logo with pulsing (remove the additions relating to `x` to stop that)

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.BLACK);
            g.drawString("Click to start!", SystemConfiguration.getWidth()/2-50, SystemConfiguration.getHeight()/2+50);
        }
    }

    private class SplashTicker implements TickObject {
        @Override
        public void tick() {
            x = (x < 41) ? x+1 : 0; // logo pulse effect counter
            if(MouseInput.isPressed(MouseEvent.BUTTON1)) {
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU));
            }
        }
    }

}