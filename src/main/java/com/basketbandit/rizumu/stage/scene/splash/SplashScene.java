package com.basketbandit.rizumu.stage.scene.splash;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.TrackParser;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.media.Image;
import com.basketbandit.rizumu.media.Sound;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.stage.scene.splash.secondary.LoginMenu;
import com.basketbandit.rizumu.stage.scene.splash.secondary.SettingsMenu;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;
import com.basketbandit.rizumu.utility.extension.AffineTransformEx;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class SplashScene extends Scene {
    private LoginMenu loginMenu = new LoginMenu();
    private SettingsMenu settingsMenu = new SettingsMenu();
    private BufferedImage logo;
    private java.awt.Image background;
    private java.awt.Image settingsIcon;
    private float x = 0;

    public SplashScene() {
        renderObject = new SplashRenderer();
        tickObject = new SplashTicker();
        mouseAdapter = new SplashMouseAdapter();

        buttons.put("loginButton", new Button(20, 20, 60, 30));
        buttons.put("logoutButton", new Button(20, 20, 60, 30));
        buttons.put("settingsButton", new Button(Configuration.getWidth() - 70, Configuration.getHeight() - 65, 50, 50).setColor(Colours.TRANSPARENT));

        try {
            // loads the master logo, uses AffineTransform to scale the image down for usage on float translations (smooth movement)
            BufferedImage masterLogo = Image.getBufferedImage("logo");
            logo = new BufferedImage(masterLogo.getWidth()/2, masterLogo.getHeight()/2, BufferedImage.TYPE_INT_ARGB);
            logo = new AffineTransformOp(new AffineTransformEx().inlineScale(.5, .5), AffineTransformOp.TYPE_BILINEAR).filter(masterLogo, logo);
            settingsIcon = Image.getBufferedImage("settings-icon").getScaledInstance(50, 50, 0);

            background = Image.getBufferedImage("splash-background").getScaledInstance(Configuration.getWidth(), Configuration.getHeight(), 0);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    @Override
    public SplashScene init(Object... objects) {
        MouseAdapters.setMouseAdapter("splash", mouseAdapter);
        KeyAdapters.setKeyAdapter("splash", null);

        if(Engine.getSecondaryScene() == null) {
            audioPlayer.load(Sound.getMedia("menu-music"));
            audioPlayer.loop();
            audioPlayer.play();
        }
        return this;
    }

    private class SplashRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.drawImage(background, null, null);
            g.setColor(Colours.WHITE_20);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            g.drawRenderedImage(logo, AffineTransform.getTranslateInstance(Configuration.getWidth()/2.0 - logo.getWidth()/2.0, (Configuration.getHeight()/2.0) - (logo.getHeight()/2.0) + Math.sin(x)*3));

            g.setFont(Fonts.default24);
            g.setColor(Color.BLACK);
            g.drawString(TrackParser.getLoadingTrack(), Alignment.center(TrackParser.getLoadingTrack(), g.getFontMetrics(Fonts.default24), 0, Configuration.getWidth()), (float) (Configuration.getHeight()/2.0 + 150));

            buttons.values().forEach(b -> {
                g.setColor(b.getColor());
                g.fill(b);
            });

            g.setColor(Color.WHITE);
            g.setFont(Fonts.default12);
            if(Configuration.getUser() == null) {
                g.drawString("Login", Alignment.center("Login", g.getFontMetrics(Fonts.default12), buttons.get("loginButton")), buttons.get("loginButton").y + buttons.get("loginButton").height/2 + 4);
            } else {
                g.drawString("Logout", Alignment.center("Logout", g.getFontMetrics(Fonts.default12), buttons.get("logoutButton")), buttons.get("logoutButton").y + buttons.get("logoutButton").height/2 + 4);
                g.setColor(Color.BLACK);
                g.drawString("Logged in as: " + Configuration.getUser(), (float) (buttons.get("logoutButton").getMaxX() + 10), buttons.get("logoutButton").y + buttons.get("logoutButton").height/2.0f + 4);
            }

            g.drawImage(settingsIcon, AffineTransform.getTranslateInstance(Configuration.getWidth() - 70, Configuration.getHeight() - 65), null);
        }
    }

    private class SplashTicker implements TickObject {
        @Override
        public void tick() {
            x = x + 0.1f; // sine wave animation
        }
    }

    private class SplashMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("settingsButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Engine.setSecondaryScene(settingsMenu.init());
                    return;
                }

                if(Configuration.getUser() == null && buttons.get("loginButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Engine.setSecondaryScene(loginMenu.init());
                    return;
                }

                if(Configuration.getUser() != null && buttons.get("logoutButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.setUser(null);
                    Configuration.setUserId(-1);
                    return;
                }

                // prevent access to menu before tracks are parsed
                if(TrackParser.isFinished()) {
                    effectPlayer.play("menu-select2");
                    audioPlayer.stop();
                    Engine.setPrimaryScene(Engine.getStaticScene(Scenes.SELECT).init());
                }
            }
        }
    }
}