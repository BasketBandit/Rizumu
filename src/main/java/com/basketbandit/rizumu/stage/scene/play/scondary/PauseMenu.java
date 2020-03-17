package com.basketbandit.rizumu.stage.scene.play.scondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.beatmap.core.Beatmap;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.scheduler.ScheduleHandler;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.stage.scene.play.PlayScene;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PauseMenu extends Scene {
    private PlayScene playScene;
    private FontMetrics metrics16;

    public PauseMenu() {
        renderObject = new PauseMenuRenderer();
        tickObject = null;
        mouseAdapter = new PauseMenuMouseAdapter();

        buttons.put("resumeButton", new com.basketbandit.rizumu.drawable.Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) - 25, 400, 75).setButtonText("Resume"));
        buttons.put("restartButton", new com.basketbandit.rizumu.drawable.Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 60, 400, 75).setButtonText("Restart"));
        buttons.put("quitButton", new Button((Configuration.getWidth()/2) - 200, (Configuration.getHeight()/3) + 145, 400, 75).setButtonText("Quit"));
    }

    @Override
    public PauseMenu init(Object... objects) {
        MouseAdapters.setMouseAdapter("pause", mouseAdapter);
        KeyAdapters.setKeyAdapter("pause", null);

        playScene = (PlayScene) objects[0];
        audioPlayer.pause();
        return this;
    }

    private class PauseMenuRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            if(metrics16 == null) {
                metrics16 = g.getFontMetrics(Fonts.default16);
            }

            g.setFont(Fonts.default16);
            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

            // draw all the buttons
            buttons.forEach((key, button) -> {
                g.setColor(Colours.BLACK);
                g.draw(button);
                g.setColor(Colours.DARK_GREY);
                g.fill(button);
                g.setColor(Colours.WHITE);
                centerBoth = Alignment.centerBoth(button.getButtonText(), metrics16, button);
                g.drawString(button.getButtonText(), centerBoth[0], centerBoth[1]);
            });
        }
    }

    private class PauseMenuMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                // 'resume' button, close the pause menu
                if(buttons.get("resumeButton").isHovered()) {
                    audioPlayer.resume();
                    effectPlayer.play("menu-click2");
                    Engine.getPrimaryScene().init();
                    Engine.setSecondaryScene(null);
                    ScheduleHandler.resumeExecution();
                    playScene.setMenuCooldown(System.currentTimeMillis());
                    return;
                }

                // 'restart' button, restart the beatmap
                if(buttons.get("restartButton").isHovered()) {
                    effectPlayer.play("menu-click3");
                    audioPlayer.stop();
                    Engine.setSecondaryScene(null);
                    ScheduleHandler.cancelExecution();

                    Track t = Engine.getTrackParser().parseTrack(playScene.getTrack().getFile()); // forgive me for the horrible variable naming...
                    for(Beatmap b: t.getBeatmaps()) {
                        if(b.getName().equals(playScene.getBeatmap().getName())) {
                            Engine.setPrimaryScene((Engine.getStaticScene(Scenes.PLAY)).init(t, b));
                            return;
                        }
                    }
                    return;
                }

                // 'quit' button, go back to the main menu
                if(buttons.get("quitButton").isHovered()) {
                    effectPlayer.play("menu-click4");
                    audioPlayer.stop();
                    ScheduleHandler.cancelExecution();
                    Engine.setPrimaryScene(Engine.getStaticScene(Scenes.SELECT).init());
                    Engine.setSecondaryScene(null);
                }
            }
        }
    }
}