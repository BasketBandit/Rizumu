package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.resource.Image;
import com.basketbandit.rizumu.resource.Sound;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class SplashScene extends Scene {
    private LoginMenu loginMenu = new LoginMenu();
    private BufferedImage logo;
    private float x = 0;

    public SplashScene() {
        renderObject = new SplashRenderer();
        tickObject = new SplashTicker();
        mouseAdapter = new SplashMouseAdapter();

        buttons.put("loginButton", new Button(20, 20, 75, 30));
        buttons.put("logoutButton", new Button(20, 20, 75, 30));

        try {
            // loads the master logo, uses AffineTransform to scale the image down for usage on float translations (smooth movement)
            BufferedImage masterLogo = Image.getBufferedImage("logo");
            logo = new BufferedImage(masterLogo.getWidth()/2, masterLogo.getHeight()/2, BufferedImage.TYPE_INT_ARGB);
            AffineTransform scaleHalf = new AffineTransform();
            scaleHalf.scale(.5, .5);
            logo = new AffineTransformOp(scaleHalf, AffineTransformOp.TYPE_BILINEAR).filter(masterLogo, logo);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }
    }

    @Override
    public SplashScene init(Object... objects) {
        MouseAdapters.setMouseAdapter("splash", mouseAdapter);
        KeyAdapters.setKeyAdapter("splash", null);

        if(Rizumu.getSecondaryScene() == null) {
            audioPlayer.changeTrack(Sound.getAudioFile("menu-music").getAbsolutePath());
            audioPlayer.loop(-1);
            audioPlayer.play();
        }
        return this;
    }

    private class SplashRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.drawRenderedImage(logo, AffineTransform.getTranslateInstance(Configuration.getContentWidth()/2.0 - logo.getWidth()/2.0, (Configuration.getContentHeight()/2.0) - (logo.getHeight()/2.0) + Math.sin(x)*3));

            for(Button button: buttons.values()) {
                g.setColor(button.getColor());
                g.fill(button);
            }

            g.setColor(Color.WHITE);
            if(Configuration.getUser() == null) {
                g.drawString("Login", Alignment.center("Login", g.getFontMetrics(Fonts.default12), buttons.get("loginButton")), buttons.get("loginButton").y + buttons.get("loginButton").height/2 + 4);
            } else {
                g.drawString("Logout", Alignment.center("Logout", g.getFontMetrics(Fonts.default12), buttons.get("logoutButton")), buttons.get("logoutButton").y + buttons.get("logoutButton").height/2 + 4);
            }
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
                if(Configuration.getUser() == null && buttons.get("loginButton").isHovered()) {
                    Rizumu.setSecondaryScene(loginMenu.init());
                    return;
                }

                if(Configuration.getUser() != null && buttons.get("logoutButton").isHovered()) {
                    Configuration.setUser(null);
                    return;
                }

                effectPlayer.play("menu-select2");
                audioPlayer.stop();
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
            }
        }
    }

    public class LoginMenu extends Scene {
        LoginMenu() {
            renderObject = new LoginMenuRenderer();
            tickObject = new LoginMenuTicker();
            mouseAdapter = new LoginMenuMouseAdapter();

            buttons.put("loginButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 145, 400, 75));
        }

        @Override
        public Scene init(Object... object) {
            MouseAdapters.setMouseAdapter("login", mouseAdapter);
            KeyAdapters.setKeyAdapter("login", null);
            return this;
        }

        private class LoginMenuRenderer implements RenderObject {
            @Override
            public void render(Graphics2D g) {
                g.setColor(Colours.DARK_GREY_90);
                g.fillRect(0, 0, Configuration.getContentWidth(), Configuration.getContentHeight());

                g.setColor(Color.DARK_GRAY);
                buttons.values().forEach(g::fill);
                g.setColor(Color.BLACK);
                buttons.values().forEach(g::draw);

                g.setFont(Fonts.default12);
                g.setColor(Color.WHITE);
                g.drawString("Login", Alignment.center("Login", g.getFontMetrics(Fonts.default12), buttons.get("loginButton")), (int) buttons.get("loginButton").getCenterY() + 4);
            }
        }

        private class LoginMenuTicker implements TickObject {
            @Override
            public void tick() {
            }
        }

        private class LoginMenuMouseAdapter extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if(buttons.get("loginButton").isHovered()) {
                        if(Database.login("", "")) {
                            Configuration.setUser("");
                        }
                        Rizumu.getPrimaryScene().init();
                        Rizumu.setSecondaryScene(null); // important to do this second to stop audio being interrupted
                    }
                }
            }
        }
    }

}