package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.score.Statistics;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResultsScene implements Scene {
    private ResultsRenderer renderObject = new ResultsRenderer();
    private ResultsTicker tickObject = new ResultsTicker();

    private ResultsMouseListener resultsMouseListener = new ResultsMouseListener();

    private Statistics statistics;

    private Button menuButton = new Button(Configuration.getContentWidth() - 200, Configuration.getContentHeight() - 150, 100, 50);

    public ResultsScene() {
        Rizumu.addMouseListener(new ResultsMouseListener());
    }

    @Override
    public ResultsScene init() {
        MouseListeners.setMouseListener("results", resultsMouseListener);
        return this;
    }

    public ResultsScene setStatistics(Statistics statistics) {
        this.statistics = statistics;
        return this;
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class ResultsRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.setColor(Color.DARK_GRAY);
            g.fill(menuButton);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Exit!", (int)menuButton.getMinX(), (int)menuButton.getCenterY());

            g.setColor(Color.DARK_GRAY);
            g.drawString(statistics.getAccuracyString(), 80, 80);
        }
    }

    private class ResultsTicker implements TickObject {
        @Override
        public void tick() {
        }
    }

    private class ResultsMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(menuButton.getBounds().contains(e.getX(), e.getY())) {
                    Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
                }
            }
        }
    }
}
