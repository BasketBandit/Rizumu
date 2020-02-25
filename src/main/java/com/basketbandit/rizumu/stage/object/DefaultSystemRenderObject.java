package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.input.MouseMovementListener;

import java.awt.*;

public class DefaultSystemRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString(Rizumu.engine.getFps() + " FPS | " + Rizumu.engine.getTps() + " TPS", 10, 20);

        if(Rizumu.isDebug()) {
            g.fillRect(Configuration.getContentWidth()/2, 0, 2, Configuration.getContentHeight());
            g.fillRect(0, Configuration.getContentHeight()/2, Configuration.getContentWidth(), 2);
            g.fillRect(250, 0, 1, Configuration.getContentHeight());

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.RED);
            g.drawString("DEBUG MODE / " +
                            "RESOURCE PATH \"" + Configuration.getBeatmapResourcePath() + "\" / " +
                            "TICKRATE " + Configuration.getTickRateNs() + " / " +
                            "WIDTH " + Configuration.getWidth() + " / " +
                            "HEIGHT " + Configuration.getHeight() + " / " +
                            "CONTENT WIDTH " + Configuration.getContentWidth() + " / " +
                            "CONTENT HEIGHT " + Configuration.getContentHeight() + " / " +
                            "MOUSE-X " + MouseMovementListener.getX() + " / MOUSE-Y " + MouseMovementListener.getY(),
                    10, Configuration.getContentHeight() - 10);
        }
    }
}
