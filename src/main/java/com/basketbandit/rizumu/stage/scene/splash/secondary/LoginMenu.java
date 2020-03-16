package com.basketbandit.rizumu.stage.scene.splash.secondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.TextLine;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.input.MouseMovementAdapter;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.regex.Pattern;

public class LoginMenu extends Scene {
    String alphaNum = Pattern.compile("^[a-zA-Z0-9_]*$").pattern();
    String alphaNumSpec = Pattern.compile("^[A-Za-z0-9!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]*$").pattern();

    TextLine username;
    TextLine password;
    TextLine selected;

    FontMetrics metrics16;

    public LoginMenu() {
        renderObject = new LoginMenuRenderer();
        tickObject = null;
        mouseAdapter = new LoginMenuMouseAdapter();
        keyAdapter = new LoginMenuKeyAdapter();

        username = new TextLine((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) - 25, 400, 75, 10);
        password = new TextLine((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 60, 400, 75, 10);

        buttons.put("loginButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 145, 400, 75));
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
            if(metrics16 == null) {
                metrics16 = g.getFontMetrics(Fonts.default16);
            }

            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

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

            g.setFont(Fonts.default16);
            g.setColor(Color.WHITE);

            int[] center = Alignment.centerBoth("Login", g.getFontMetrics(Fonts.default16), buttons.get("loginButton"));
            g.drawString("Login", center[0], center[1]);

            // if username is blank, draw placeholder
            g.setColor(username.getText().isEmpty() ? Color.GRAY : Color.BLACK);
            center = Alignment.centerBoth(username.getText().isEmpty() ? "Password" : username.getText(), metrics16, username.getBounds());
            g.drawString(username.getText().isEmpty() ? "Username" : username.getText(), center[0], center[1]);

            // if password is blank, draw placeholder
            g.setColor(password.getText().isEmpty() ? Color.GRAY : Color.BLACK);
            center = Alignment.centerBoth(password.getText().isEmpty() ? "Password" : "•".repeat(password.getText().length()), metrics16, password.getBounds());
            g.drawString(password.getText().isEmpty() ? "Password" : "•".repeat(password.getText().length()), center[0], center[1]);
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
                        log.info("Successfully logged in as: " + username.getText());
                    }

                    Engine.getPrimaryScene().init();
                    Engine.setSecondaryScene(null); // important to do this second to stop audio being interrupted
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
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Engine.getPrimaryScene().init();
                Engine.setSecondaryScene(null);
            }

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
