package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsScene extends Scene {

    public SettingsScene() {
        renderObject = new SettingsRenderer();
        tickObject = new SettingsTicker();
        mouseAdapter = new SettingsMouseAdapter();
        keyAdapter = new SettingsKeyAdapter();

        buttons.put("frameRateButton", new Button(Configuration.getWidth() - 120, Configuration.getHeight() - 70, 100, 50));
        buttons.put("menu", new Button(Configuration.getContentWidth() - 200, Configuration.getContentHeight() - 150, 100, 50));
    }

    @Override
    public SettingsScene init(Object... object) {
        MouseAdapters.setMouseAdapter("settings", mouseAdapter);
        KeyAdapters.setKeyAdapter("settings", keyAdapter);
        return this;
    }

    private class SettingsRenderer implements RenderObject {

        @Override
        public void render(Graphics2D g) {
            // background
            g.setFont(Fonts.default12);

            g.setColor(Color.DARK_GRAY);
            g.fill(buttons.get("menu"));
            g.fill(buttons.get("frameRateButton"));
            //g.fill(buttons.get("volumeUpButton"));
            //g.fill(buttons.get("volumeDownButton"));

            g.setColor(Color.WHITE);
            g.drawString("Menu", (int)buttons.get("menu").getMinX(), (int)buttons.get("menu").getCenterY());
            g.drawString("Cap Framerate", (int)buttons.get("frameRateButton").getMinX()+12, (int)buttons.get("frameRateButton").getCenterY()+2);
            //g.drawString("Vol +0.1db", (int)buttons.get("volumeUpButton").getMinX(), (int)buttons.get("volumeUpButton").getCenterY());
            //g.drawString("Vol -0.1db", (int)buttons.get("volumeDownButton").getMinX(), (int)buttons.get("volumeDownButton").getCenterY());
        }
    }

    private class SettingsTicker implements TickObject {
        @Override
        public void tick() {
        }
    }

    private class SettingsMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("menu").isHovered()) {
                    Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
                    return;
                }

                if(buttons.get("frameRateButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

                if(buttons.get("volumeUpButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                    return;
                }

                if(buttons.get("volumeDownButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    if(audioPlayer.getGain()+0.1 < 6.0206) {
                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
                    }
                }
            }
        }
    }

    public class SettingsKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.SPLASH).init());
            }
        }
    }
}

