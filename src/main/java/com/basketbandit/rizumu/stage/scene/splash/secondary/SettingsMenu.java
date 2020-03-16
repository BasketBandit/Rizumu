package com.basketbandit.rizumu.stage.scene.splash.secondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsMenu extends Scene {
    private FontMetrics metrics16;

    public SettingsMenu() {
        renderObject = new SettingsMenuRenderer();
        tickObject = null;
        mouseAdapter = new SettingsMenuMouseAdapter();
        keyAdapter = new SettingsMenuKeyAdapter();

        buttons.put("frameRateButton", new Button(Configuration.getWidth() - 120, Configuration.getHeight() - 70, 100, 50));
    }

    @Override
    public SettingsMenu init(Object... object) {
        MouseAdapters.setMouseAdapter("settings", mouseAdapter);
        KeyAdapters.setKeyAdapter("settings", keyAdapter);
        return this;
    }

    private class SettingsMenuRenderer implements RenderObject {

        @Override
        public void render(Graphics2D g) {
            if(metrics16 == null) {
                metrics16 = g.getFontMetrics(Fonts.default16);
            }

            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

            // background
            g.setFont(Fonts.default12);

            g.setColor(Color.DARK_GRAY);
            g.fill(buttons.get("frameRateButton"));
            //g.fill(buttons.get("volumeUpButton"));
            //g.fill(buttons.get("volumeDownButton"));

            g.setColor(Color.WHITE);
            g.drawString("Cap Framerate", (int)buttons.get("frameRateButton").getMinX()+12, (int)buttons.get("frameRateButton").getCenterY()+2);
            //g.drawString("Vol +0.1db", (int)buttons.get("volumeUpButton").getMinX(), (int)buttons.get("volumeUpButton").getCenterY());
            //g.drawString("Vol -0.1db", (int)buttons.get("volumeDownButton").getMinX(), (int)buttons.get("volumeDownButton").getCenterY());
        }
    }

    private class SettingsMenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("frameRateButton").isHovered()) {
                    effectPlayer.play("menu-click");
                    Configuration.toggleUnlockedFramerate();
                    return;
                }

//                if(buttons.get("volumeUpButton").isHovered()) {
//                    effectPlayer.play("menu-click");
//                    if(audioPlayer.getGain()+0.1 < 6.0206) {
//                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
//                    }
//                    return;
//                }
//
//                if(buttons.get("volumeDownButton").isHovered()) {
//                    effectPlayer.play("menu-click");
//                    if(audioPlayer.getGain()+0.1 < 6.0206) {
//                        audioPlayer.setGain(audioPlayer.getGain()+0.1f);
//                    }
//                }
            }
        }
    }

    public class SettingsMenuKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                Engine.getPrimaryScene().init();
                Engine.setSecondaryScene(null);
            }
        }
    }
}
