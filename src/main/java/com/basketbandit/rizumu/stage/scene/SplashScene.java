package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;

public class SplashScene implements Scene {
    private SplashRenderer renderObject = new SplashRenderer();
    private SplashTicker tickObject = new SplashTicker();

    private SplashMouseListener splashMouseListener = new SplashMouseListener();

    private BufferedImage logo;
    private float x = 0;

    public SplashScene() {
        try {
            // loads the master logo, uses AffineTransform to scale the image down for usage on float translations (smooth movement)
            BufferedImage masterLogo = ImageIO.read(new File("src/main/resources/assets/logo.png"));
            logo = new BufferedImage(masterLogo.getWidth()/2, masterLogo.getHeight()/2, BufferedImage.TYPE_INT_ARGB);
            AffineTransform scaleHalf = new AffineTransform();
            scaleHalf.scale(.5, .5);
            logo = new AffineTransformOp(scaleHalf, AffineTransformOp.TYPE_BILINEAR).filter(masterLogo, logo);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        AudioPlayer audioPlayer = AudioPlayerController.getAudioPlayer("menu");
        audioPlayer.changeTrack("src/main/resources/assets/menu.wav");
        audioPlayer.loop(-1);
        audioPlayer.play();
    }

    @Override
    public SplashScene init() {
        MouseListeners.setMouseListener("splash", splashMouseListener);
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

    private class SplashRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.drawRenderedImage(logo, AffineTransform.getTranslateInstance(Configuration.getContentWidth()/2.0 - logo.getWidth()/2.0, (Configuration.getContentHeight()/2.0) - (logo.getHeight()/2.0) + Math.sin(x)*3));
        }
    }

    private class SplashTicker implements TickObject {
        @Override
        public void tick() {
            x = x + 0.1f; // sine wave animation
        }
    }

    private class SplashMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
            }
        }
    }
}