package com.basketbandit.rizumu.stage.scene.splash.secondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.TextInput;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
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
    private String alphaNumSpec = Pattern.compile("^[A-Za-z0-9!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]*$").pattern();

    private TextInput selected;

    private FontMetrics metrics16;

    public LoginMenu() {
        renderObject = new LoginMenuRenderer();
        tickObject = null;
        mouseAdapter = new LoginMenuMouseAdapter();
        keyAdapter = new LoginMenuKeyAdapter();

        textInputs.put("username", new TextInput((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) - 25, 400, 75, 10));
        textInputs.put("password", new TextInput((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 60, 400, 75, 10));

        buttons.put("loginButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 145, 400, 75));
    }

    @Override
    public Scene init(Object... object) {
        MouseAdapters.setMouseAdapter("login", mouseAdapter);
        KeyAdapters.setKeyAdapter("login", keyAdapter);

        textInputs.get("username").setText("");
        textInputs.get("password").setText("");
        selected = textInputs.get("username");
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
            textInputs.values().forEach(g::draw);

            g.setColor(Colours.DARK_GREY);
            buttons.values().forEach(g::fill);
            textInputs.forEach((key, value) -> {
                g.setColor(selected != null && selected.equals(value) ? Colours.BLUE : Color.DARK_GRAY);
                g.fill(value);
                g.setColor(Color.WHITE);
                g.fill(value.getInnerBounds());
            });

            g.setFont(Fonts.default16);
            g.setColor(Color.WHITE);

            int[] center = Alignment.centerBoth("Login", g.getFontMetrics(Fonts.default16), buttons.get("loginButton"));
            g.drawString("Login", center[0], center[1]);

            // if username is blank, draw placeholder
            g.setColor(textInputs.get("username").getText().isEmpty() ? Color.GRAY : Color.BLACK);
            center = Alignment.centerBoth(textInputs.get("username").getText().isEmpty() ? "Password" : textInputs.get("username").getText(), metrics16, textInputs.get("username").getBounds());
            g.drawString(textInputs.get("username").getText().isEmpty() ? "Username" : textInputs.get("username").getText(), center[0], center[1]);

            // if password is blank, draw placeholder
            g.setColor(textInputs.get("password").getText().isEmpty() ? Color.GRAY : Color.BLACK);
            center = Alignment.centerBoth(textInputs.get("password").getText().isEmpty() ? "Password" : "•".repeat(textInputs.get("password").getText().length()), metrics16, textInputs.get("password").getBounds());
            g.drawString(textInputs.get("password").getText().isEmpty() ? "Password" : "•".repeat(textInputs.get("password").getText().length()), center[0], center[1]);
        }
    }

    private class LoginMenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                selected = null;

                if(buttons.get("loginButton").isHovered()) {
                    if(Database.login(textInputs.get("username").getText(), textInputs.get("password").getText())) {
                        Configuration.setUser(textInputs.get("username").getText());
                        log.info("Successfully logged in as: " + textInputs.get("username").getText());
                    }

                    Engine.getPrimaryScene().init();
                    Engine.setSecondaryScene(null); // important to do this second to stop audio being interrupted
                }

                textInputs.forEach((key, value) -> {
                    if(value.isHovered()) {
                        selected = value;
                    }
                });
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
                selected = (selected == null || selected.equals(textInputs.get("password"))) ? textInputs.get("username") : textInputs.get("password");
                return;
            }

            // only modify text input if it is focused
            textInputs.forEach((key, value) -> {
                if(selected.equals(value)) {
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        value.deleteChar();
                        return;
                    }
                    if(value.getText().length() < 256 && (e.getKeyChar()+"").matches(alphaNumSpec)) {
                        value.append(e.getKeyChar() + "");
                    }
                }
            });
        }
    }
}
