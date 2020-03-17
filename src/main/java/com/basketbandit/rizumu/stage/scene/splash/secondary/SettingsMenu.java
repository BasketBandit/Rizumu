package com.basketbandit.rizumu.stage.scene.splash.secondary;

import com.basketbandit.rizumu.Configuration;
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

public class SettingsMenu extends Scene {
    private String alphaNumSpec = Pattern.compile("^[A-Za-z0-9!\"#$%&'()*+,-./:;<=>?@\\[\\]^_`{|}~]*$").pattern();
    private String num = Pattern.compile("^[0-9]*$").pattern();
    private FontMetrics metrics16;
    private TextInput selected;

    public SettingsMenu() {
        renderObject = new SettingsMenuRenderer();
        tickObject = null;
        mouseAdapter = new SettingsMenuMouseAdapter();
        keyAdapter = new SettingsMenuKeyAdapter();

        buttons.put("frameRateButton", new Button((Configuration.getWidth()/2) - 250, (Configuration.getHeight()/3) + 90, 500, 50).setButtonText("Unlocked Framerate"));
        buttons.put("fullScreenButton", new Button((Configuration.getWidth()/2) - 250, (Configuration.getHeight()/3) + 170, 500, 50).setButtonText("FullScreen"));
        buttons.put("cancelButton", new Button((Configuration.getWidth()/2) - 250, (Configuration.getHeight()/3) + 250, 245, 50).setButtonText("Cancel"));
        buttons.put("saveButton", new Button((Configuration.getWidth()/2) + 5, (Configuration.getHeight()/3) + 250, 245, 50).setButtonText("Save"));

        textInputs.put("Width", new TextInput((Configuration.getWidth() / 2) - 250, (Configuration.getHeight() / 3) - 75, 245, 50, 5).setText("" + Configuration.getWidth()));
        textInputs.put("Height", new TextInput((Configuration.getWidth()/2) + 5, (Configuration.getHeight()/3) - 75, 245, 50, 5).setText("" + Configuration.getHeight()));
        textInputs.put("Track Directory", new TextInput((Configuration.getWidth()/2) - 250, (Configuration.getHeight()/3) + 10, 500, 50, 5).setText(Configuration.getTracksPath()));
    }

    @Override
    public SettingsMenu init(Object... object) {
        MouseAdapters.setMouseAdapter("settings", mouseAdapter);
        KeyAdapters.setKeyAdapter("settings", keyAdapter);

        textInputs.get("Width").setText("" + Configuration.getWidth());
        textInputs.get("Height").setText("" + Configuration.getHeight());
        textInputs.get("Track Directory").setText(Configuration.getTracksPath());
        return this;
    }

    private class SettingsMenuRenderer implements RenderObject {

        @Override
        public void render(Graphics2D g) {
            if(metrics16 == null) {
                metrics16 = g.getFontMetrics(Fonts.default16);
            }

            // background
            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            g.setFont(Fonts.default16);

            // draw all text inputs
            textInputs.forEach((key, value) -> {
                centerBoth = Alignment.centerBoth(value.getText(), metrics16, value.getBounds());
                g.setColor(selected != null && selected.getBounds().equals(value.getBounds()) ? Colours.BLUE : Color.DARK_GRAY);
                g.fill(value);
                g.setColor(Color.WHITE);
                g.drawString(key, value.x + 5, value.y - 10); // label
                g.fill(value.getInnerBounds());
                g.setColor(Color.BLACK);
                g.drawString(value.getText(), centerBoth[0], centerBoth[1]); // inner text
            });

            // fill buttons
            buttons.forEach((key, value) -> {
                g.setColor(Colours.DARK_GREY);
                g.fill(value);
            });

            // colour coordinate appropriate buttons
            g.setColor(Configuration.isFramerateUnlocked() ? Color.GREEN : Colours.CRIMSON);
            g.fill(buttons.get("frameRateButton"));
            g.setColor(Configuration.isFullscreen() ? Color.GREEN : Colours.CRIMSON);
            g.fill(buttons.get("fullScreenButton"));

            // draw text for all buttons
            buttons.forEach((key, value) -> {
                centerBoth = Alignment.centerBoth(value.getButtonText(), metrics16, value);
                g.setColor(Color.WHITE);
                g.drawString(value.getButtonText(), centerBoth[0], centerBoth[1]);
            });
        }
    }

    private class SettingsMenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                selected = null;

                if(buttons.get("frameRateButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(buttons.get("fullScreenButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleFullscreen();
                    return;
                }

                if(buttons.get("cancelButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Engine.getPrimaryScene().init();
                    Engine.setSecondaryScene(null);
                }

                if(buttons.get("saveButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    if(!textInputs.get("Width").getText().matches(num)) {
                        textInputs.get("Width").setText(""+Configuration.getWidth());
                        return;
                    }
                    if(!textInputs.get("Height").getText().matches(num)) {
                        textInputs.get("Height").setText(""+Configuration.getHeight());
                        return;
                    }
                    Configuration.setWidth(Integer.parseInt(textInputs.get("Width").getText()));
                    Configuration.setHeight(Integer.parseInt(textInputs.get("Height").getText()));
                    Configuration.setTracksPath(textInputs.get("Track Directory").getText());
                    Configuration.writeConfigurationFile();
                    System.exit(0);
                }

                // text inputs
                textInputs.forEach((key, value) -> {
                    if(value.isHovered()) {
                        effectPlayer.play("menu-click");
                        selected = value;
                    }
                });
            }
        }
    }

    private class SettingsMenuKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Engine.getPrimaryScene().init();
                Engine.setSecondaryScene(null);
            }

            // change which text line is selected with tab key
            if(e.getKeyChar() == KeyEvent.VK_TAB) {
                selected = (selected == null || selected.equals(textInputs.get("Track Directory"))) ? textInputs.get("Width") : selected.equals(textInputs.get("Width")) ? textInputs.get("Height") : textInputs.get("Track Directory");
                return;
            }

            // only modify text input if it is focused
            textInputs.forEach((key, value) -> {
                if(selected.equals(value)) {
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                        effectPlayer.play("key-click");
                        value.deleteChar();
                        return;
                    }
                    if(value.getText().length() < 256 && (e.getKeyChar()+"").matches(alphaNumSpec)) {
                        effectPlayer.play("key-click");
                        value.append(e.getKeyChar() + "");
                    }
                }
            });
        }
    }
}
