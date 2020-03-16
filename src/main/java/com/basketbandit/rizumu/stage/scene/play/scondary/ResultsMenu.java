package com.basketbandit.rizumu.stage.scene.play.scondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.score.Score;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResultsMenu extends Scene {
    private Score score;
    private Image backgroundImage;
    private boolean uploaded = false;

    public ResultsMenu() {
        renderObject = new ResultsRenderer();
        tickObject = null;
        mouseAdapter = new ResultsMouseAdapter();

        buttons.put("track_select", new Button(Configuration.getWidth() - 200, Configuration.getHeight() - 150, 100, 50));
    }

    @Override
    public ResultsMenu init(Object... object) {
        MouseAdapters.setMouseAdapter("results", mouseAdapter);
        KeyAdapters.setKeyAdapter("results", null);

        this.score = (Score) object[0];
        this.backgroundImage = score.getImage();
        if(Configuration.getUserId() != -1) {
            uploaded = Database.uploadScore(score);
        }
        return this;
    }

    private class ResultsRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

            g.setColor(Color.WHITE);
            if(Configuration.getUserId() == -1) {
                g.drawString("Login to submit scores!", 10, 30);
            } else {
                if(uploaded) {
                    g.drawString("Score submitted successfully!", 10, 30);
                } else {
                    g.drawString("Score submission unsuccessful...", 10, 30);
                }
            }

            g.setColor(Color.DARK_GRAY);
            g.fill(buttons.get("track_select"));

            g.setFont(Fonts.default12);
            g.setColor(Color.WHITE);
            g.drawString("Exit!", (int)buttons.get("track_select").getMinX(), (int)buttons.get("track_select").getCenterY());

            g.setColor(Color.WHITE);
            g.drawString(score.getAccuracyString(), 80, 80);
            g.drawString("Highest Combo: " + score.getHighestCombo() + "!", 80, 100);
        }
    }

    private class ResultsMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("track_select").isHovered()) {
                    Engine.setPrimaryScene(Engine.getStaticScene(Scenes.SELECT).init());
                    Engine.setSecondaryScene(null);
                }
            }
        }
    }
}
