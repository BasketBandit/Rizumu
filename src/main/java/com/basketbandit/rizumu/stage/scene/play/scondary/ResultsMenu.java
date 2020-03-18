package com.basketbandit.rizumu.stage.scene.play.scondary;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.database.Database;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.drawable.Container;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.input.KeyAdapters;
import com.basketbandit.rizumu.input.MouseAdapters;
import com.basketbandit.rizumu.score.Score;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Colours;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;

public class ResultsMenu extends Scene {
    private Score score;
    private boolean uploaded = false;
    private FontMetrics metrics12;
    private FontMetrics metrics16;
    private FontMetrics metrics24;
    private FontMetrics metrics36;
    private FontMetrics metrics72;

    Container hitContainer = new Container((Configuration.getWidth()/2) - 300, (Configuration.getHeight()/3) + 75,600, 100);

    public ResultsMenu() {
        renderObject = new ResultsRenderer();
        tickObject = null;
        mouseAdapter = new ResultsMouseAdapter();

        buttons.put("track_select", new Button(Configuration.getWidth() - 145, Configuration.getHeight() - 70, 125, 50).setButtonText("Menu"));
    }

    @Override
    public ResultsMenu init(Object... object) {
        MouseAdapters.setMouseAdapter("results", mouseAdapter);
        KeyAdapters.setKeyAdapter("results", null);

        this.score = (Score) object[0];
        if(Configuration.getUserId() != -1) {
            uploaded = Database.uploadScore(score);
        }
        return this;
    }

    private class ResultsRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            if(metrics16 == null) {
                metrics12 = g.getFontMetrics(Fonts.default12);
                metrics16 = g.getFontMetrics(Fonts.default16);
                metrics24 = g.getFontMetrics(Fonts.default24);
                metrics36 = g.getFontMetrics(Fonts.default36);
                metrics72 = g.getFontMetrics(Fonts.default72);
            }

            g.setColor(Colours.DARK_GREY_90);
            g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());

            g.setFont(Fonts.default24);
            if(Configuration.getUserId() == -1) {
                g.setColor(Colours.GOLD_75);
                g.fillRect(0, 0, 500, 50);
                g.setColor(Colours.WHITE);
                centerBoth = Alignment.centerBoth("Login to submit scores!", metrics24, 0, 0, 500, 50);
                g.drawString("Login to submit scores!", centerBoth[0], centerBoth[1]);
            } else {
                if(uploaded) {
                    g.setColor(Colours.GREEN_75);
                    g.fillRect(0, 0, 500, 50);
                    g.setColor(Colours.WHITE);
                    centerBoth = Alignment.centerBoth("Score submitted successfully!", metrics24, 0, 0, 500, 50);
                    g.drawString("Score submitted successfully!", centerBoth[0], centerBoth[1]);
                } else {
                    g.setColor(Colours.CRIMSON_75);
                    g.fillRect(0, 0, 500, 50);
                    g.setColor(Colours.WHITE);
                    centerBoth = Alignment.centerBoth("Score submission unsuccessful...", metrics24, 0, 0, 500, 50);
                    g.drawString("Score submission unsuccessful...", centerBoth[0], centerBoth[1]);
                }
            }


            // score
            g.setColor(Colours.WHITE);
            g.setFont(Fonts.default72);
            centerBoth = Alignment.centerBoth(NumberFormat.getNumberInstance(Locale.UK).format(score.getScore()), metrics72, 0, 0, Configuration.getWidth(), Configuration.getHeight());
            g.drawString(NumberFormat.getNumberInstance(Locale.UK).format(score.getScore()), centerBoth[0], centerBoth[1] - 150);

            // accuracy
            g.setFont(Fonts.default36);
            centerBoth = Alignment.centerBoth(score.getAccuracy() + "%", metrics36, 0, 0, Configuration.getWidth(), Configuration.getHeight());
            g.drawString(score.getAccuracy() + "%", centerBoth[0], centerBoth[1] - 80);

            // mx
            centerBoth = Alignment.centerBoth("MX", metrics36, hitContainer.x, hitContainer.y, 150, 50);
            g.setColor(Colours.GOLD);
            g.drawString("MX", centerBoth[0], centerBoth[1]);
            centerBoth = Alignment.centerBoth("" + score.getMxHit(), metrics36, hitContainer.x, hitContainer.y, 150, 50);
            g.setColor(Color.WHITE);
            g.drawString("" + score.getMxHit(), centerBoth[0], centerBoth[1] + 50);

            // ex
            centerBoth = Alignment.centerBoth("EX", metrics36, hitContainer.x + 150, hitContainer.y, 150, 50);
            g.setColor(Colours.GREEN);
            g.drawString("EX", centerBoth[0], centerBoth[1]);
            centerBoth = Alignment.centerBoth("" + score.getExHit(), metrics36, hitContainer.x + 150, hitContainer.y, 150, 50);
            g.setColor(Color.WHITE);
            g.drawString("" + score.getExHit(), centerBoth[0], centerBoth[1] + 50);

            // nm
            centerBoth = Alignment.centerBoth("NM", metrics36, hitContainer.x + 300, hitContainer.y, 150, 50);
            g.setColor(Colours.BLUE);
            g.drawString("NM", centerBoth[0], centerBoth[1]);
            centerBoth = Alignment.centerBoth("" + score.getNmHit(), metrics36, hitContainer.x + 300, hitContainer.y, 150, 50);
            g.setColor(Color.WHITE);
            g.drawString("" + score.getNmHit(), centerBoth[0], centerBoth[1] + 50);

            // x
            centerBoth = Alignment.centerBoth("X", metrics36, hitContainer.x + 450, hitContainer.y, 150, 50);
            g.setColor(Colours.CRIMSON);
            g.drawString("X", centerBoth[0], centerBoth[1]);
            centerBoth = Alignment.centerBoth("" + score.getMissedNotes(), metrics36, hitContainer.x + 450, hitContainer.y, 150, 50);
            g.setColor(Color.WHITE);
            g.drawString("" + score.getMissedNotes(), centerBoth[0], centerBoth[1] + 50);

            // menu-return
            g.setFont(Fonts.default12);
            g.setColor(Colours.MEDIUM_GREY_25);
            g.fill(buttons.get("track_select"));
            g.setColor(Colours.WHITE);
            centerBoth = Alignment.centerBoth(buttons.get("track_select").getButtonText(), metrics12, buttons.get("track_select"));
            g.drawString(buttons.get("track_select").getButtonText(), centerBoth[0], centerBoth[1]);
        }
    }

    private class ResultsMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(buttons.get("track_select").isHovered()) {
                    effectPlayer.play("menu-click");
                    Engine.setPrimaryScene(Engine.getStaticScene(Scenes.SELECT).init());
                    Engine.setSecondaryScene(null);
                }
            }
        }
    }
}
