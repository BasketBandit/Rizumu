package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.input.MouseMovementListener;
import com.basketbandit.rizumu.utility.Fonts;
import com.basketbandit.rizumu.utility.Alignment;

import java.awt.*;

public class DefaultSystemRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setFont(Fonts.default12);
        g.setColor(Color.GRAY);
        String frames = Rizumu.engine.getFps() + " FPS" + (Rizumu.isDebug() ? " | " + Rizumu.engine.getTps() + " TPS" : "");
        g.drawString(frames, Alignment.right(frames, g.getFontMetrics(Fonts.default12), 0, Configuration.getContentWidth()) - 10, 20);

        if(Rizumu.isDebug()) {
            g.fillRect(Configuration.getContentWidth()/2, 0, 2, Configuration.getContentHeight());
            g.fillRect(0, Configuration.getContentHeight()/2, Configuration.getContentWidth(), 2);
            g.fillRect(250, 0, 1, Configuration.getContentHeight());

            g.setFont(Fonts.default12);
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
