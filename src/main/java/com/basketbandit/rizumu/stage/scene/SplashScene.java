package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.TextLine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.input.MouseMovementAdapter;
import com.basketbandit.rizumu.resource.Image;
import com.basketbandit.rizumu.resource.Sound;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

public class SplashScene extends Scene {
    private LoginMenu loginMenu = new LoginMenu();
    private BufferedImage logo;
    private float x = 0;

    public SplashScene() {
        renderObject = new SplashRenderer();
        tickObject = new SplashTicker();
        mouseAdapter = new SplashMouseAdapter();

        buttons.put("loginButton", new Button(20, 20, 60, 30));
        buttons.put("logoutButton", new Button(20, 20, 60, 30));

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

            g.setColor(Color.BLACK);
            buttons.values().forEach(g::draw);
            g.setColor(Color.DARK_GRAY);
            buttons.values().forEach(g::fill);

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
        String alphaNum = Pattern.compile("^[a-zA-Z0-9_]*$").pattern();
        String alphaNumSpec = Pattern.compile("^[A-Za-z0-9!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]*$").pattern();

        TextLine username;
        TextLine password;
        TextLine selected;

        LoginMenu() {
            renderObject = new LoginMenuRenderer();
            tickObject = new LoginMenuTicker();
            mouseAdapter = new LoginMenuMouseAdapter();
            keyAdapter = new LoginMenuKeyAdapter();

            username = new TextLine((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) - 25, 400, 75, 10);
            password = new TextLine((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 60, 400, 75, 10);

            buttons.put("loginButton", new Button((Configuration.getContentWidth()/2) - 200, (Configuration.getContentHeight()/3) + 145, 400, 75));
        }

        @Override
        public Scene init(Object... object) {
            MouseAdapters.setMouseAdapter("login", mouseAdapter);
            KeyAdapters.setKeyAdapter("login", keyAdapter);

            username.setText("");
            password.setText("");
            selected = username;
            return this;
        }

        private class LoginMenuRenderer implements RenderObject {
            @Override
            public void render(Graphics2D g) {
                g.setColor(Colours.DARK_GREY_90);
                g.fillRect(0, 0, Configuration.getContentWidth(), Configuration.getContentHeight());

                g.setColor(Color.BLACK);
                buttons.values().forEach(g::draw);
                g.draw(username);
                g.draw(password);

                g.setColor(Color.DARK_GRAY);
                buttons.values().forEach(g::fill);

                g.setColor(selected != null && selected.equals(username) ? Colours.BLUE : Color.DARK_GRAY);
                g.fill(username);
                g.setColor(selected != null && selected.equals(password) ? Colours.BLUE : Color.DARK_GRAY);
                g.fill(password);

                g.setColor(Color.white);
                g.fill(username.getInnerBounds());
                g.fill(password.getInnerBounds());

                g.setFont(Fonts.default24);
                g.setColor(Color.WHITE);

                int[] center = Alignment.centerBoth("Login", g.getFontMetrics(Fonts.default24), buttons.get("loginButton"));
                g.drawString("Login", center[0], center[1]);

                // if username is blank, draw placeholder
                g.setColor(username.getText().isEmpty() ? Color.GRAY : Color.BLACK);
                center = Alignment.centerBoth(username.getText().isEmpty() ? "Password" : username.getText(), g.getFontMetrics(Fonts.default24), username.getBounds());
                g.drawString(username.getText().isEmpty() ? "Username" : username.getText(), center[0], center[1]);

                // if password is blank, draw placeholder
                g.setColor(password.getText().isEmpty() ? Color.GRAY : Color.BLACK);
                center = Alignment.centerBoth(password.getText().isEmpty() ? "Password" : "•".repeat(password.getText().length()), g.getFontMetrics(Fonts.default24), password.getBounds());
                g.drawString(password.getText().isEmpty() ? "Password" : "•".repeat(password.getText().length()), center[0], center[1]);
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
                    selected = null;

                    if(buttons.get("loginButton").isHovered()) {
                        if(Database.login(username.getText(), password.getText())) {
                            Configuration.setUser(username.getText());
                            log.info("successfully logged in as " + username.getText());
                        }

                        Rizumu.getPrimaryScene().init();
                        Rizumu.setSecondaryScene(null); // important to do this second to stop audio being interrupted
                    }

                    if(username.getBounds().contains(MouseMovementAdapter.getX(), MouseMovementAdapter.getY())) {
                        selected = username;
                    }

                    if(password.getBounds().contains(MouseMovementAdapter.getX(), MouseMovementAdapter.getY())) {
                        selected = password;
                    }
                }
            }
        }

        private class LoginMenuKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                // change which text line is selected with tab key
                if(e.getKeyChar() == KeyEvent.VK_TAB) {
                    selected = (selected == null || selected.equals(password)) ? username : password;
                    return;
                }

                // only modify username text line if it is focused
                if(selected.equals(username)) {
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        username.deleteChar();
                        return;
                    }
                    if(username.getText().length() < 17 && (e.getKeyChar()+"").matches(alphaNum)) {
                        username.append(e.getKeyChar() + "");
                    }
                }

                // only modify password text line if it is focused
                if(selected.equals(password)) {
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        password.deleteChar();
                        return;
                    }
                    if(password.getText().length() < 256 && (e.getKeyChar()+"").matches(alphaNumSpec)) {
                        password.append(e.getKeyChar() + "");
                    }
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    selected.deleteChar();
                }
            }
        }
    }

}