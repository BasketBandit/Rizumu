package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.score.Score;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResultsScene extends Scene {
    private Score score;
    private Image backgroundImage;
    private boolean uploaded = false;

    public ResultsScene() {
        renderObject = new ResultsRenderer();
        tickObject = new ResultsTicker();
        mouseAdapter = new ResultsMouseAdapter();

        buttons.put("menu", new Button(Configuration.getWidth() - 200, Configuration.getHeight() - 150, 100, 50));
    }

    @Override
    public ResultsScene init(Object... object) {
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
            if(backgroundImage != null) {
                g.drawImage(backgroundImage, null, null);
                g.setColor(Colours.DARK_GREY_75);
                g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            }

            g.setColor(Color.WHITE);
            if(Configuration.getUserId() == -1) {
                g.drawString("Login to submit scores!", 10, 20);
            } else {
                if(uploaded) {
                    g.drawString("Score submitted successfully!", 10, 20);
                } else {
                    g.drawString("Score submission unsuccessful...", 10, 20);
                }
            }

            g.setColor(Color.DARK_GRAY);
            g.fill(buttons.get("menu"));

            g.setFont(Fonts.default12);
            g.setColor(Color.WHITE);
            g.drawString("Exit!", (int)buttons.get("menu").getMinX(), (int)buttons.get("menu").getCenterY());

            g.setColor(Color.WHITE);
            g.drawString(score.getAccuracyString(), 80, 80);
            g.drawString("Highest Combo: " + score.getHighestCombo() + "!", 80, 100);
        }
    }

    private class ResultsTicker implements TickObject {
        @Override
        public void tick() {
        }
    }

    private class ResultsMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("menu").isHovered()) {
                    Engine.setPrimaryScene(Engine.getStaticScene(Scenes.MENU).init());
                }
            }
        }
    }
}
