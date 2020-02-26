package com.basketbandit.rizumu.stage.scene;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.MouseListeners;
import com.basketbandit.rizumu.score.Statistics;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.object.TickObject;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;

public class ResultsScene implements Scene {
    private ResultsRenderer renderObject = new ResultsRenderer();
    private ResultsTicker tickObject = new ResultsTicker();

    private ResultsMouseListener resultsMouseListener = new ResultsMouseListener();

    private Statistics statistics;

    private Image backgroundImage;
    private Button menuButton = new Button(Configuration.getContentWidth() - 200, Configuration.getContentHeight() - 150, 100, 50);

    public ResultsScene() {
        Rizumu.addMouseListener(new ResultsMouseListener());
    }

    @Override
    public ResultsScene init() {
        MouseListeners.setMouseListener("results", resultsMouseListener);
        return this;
    }

    public ResultsScene initScene(Statistics statistics) {
        this.statistics = statistics;
        this.backgroundImage = statistics.getImage();
        renderObject.backgroundImageTransform = AffineTransform.getScaleInstance((Configuration.getWidth()+.0)/(backgroundImage.getWidth(null)+.0), (Configuration.getHeight()+.0)/(backgroundImage.getHeight(null)+.0));
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
        AffineTransform backgroundImageTransform;

        @Override
        public void render(Graphics2D g) {
            // background
            if(backgroundImage != null) {
                g.drawImage(backgroundImage, backgroundImageTransform, null);
                g.setColor(Colours.DARK_GREY_75);
                g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
            }

            g.setColor(Color.DARK_GRAY);
            g.fill(menuButton);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Exit!", (int)menuButton.getMinX(), (int)menuButton.getCenterY());

            g.setColor(Color.WHITE);
            g.drawString(statistics.getAccuracyString(), 80, 80);
            g.drawString("Highest Combo: " + statistics.getHighestCombo() + "!", 80, 100);
        }
    }

    private class ResultsTicker implements TickObject {
        @Override
        public void tick() {
        }
    }

    private class ResultsMouseListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                if(menuButton.getBounds().contains(e.getX(), e.getY())) {
                    Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU).init());
                }
            }
        }
    }
}
