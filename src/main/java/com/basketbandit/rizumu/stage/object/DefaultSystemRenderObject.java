package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.input.MouseMovementAdapter;
import com.basketbandit.rizumu.utility.Fonts;
import com.basketbandit.rizumu.utility.Alignment;

import java.awt.*;

public class DefaultSystemRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setFont(Fonts.default12);
        g.setColor(Color.GRAY);
        String frames = Rizumu.engine.getFps() + " FPS" + (Rizumu.isDebug() ? " | " + Rizumu.engine.getTps() + " TPS" : "");
        g.drawString(frames, Alignment.right(frames, g.getFontMetrics(Fonts.default12), 0, Configuration.getWidth()) - 10, 20);

        if(Rizumu.isDebug()) {
            g.fillRect(Configuration.getWidth()/2, 0, 2, Configuration.getHeight());
            g.fillRect(0, Configuration.getHeight()/2, Configuration.getWidth(), 2);
            g.fillRect(250, 0, 1, Configuration.getHeight());

            g.setFont(Fonts.default12);
            g.setColor(Color.RED);
            g.drawString("DEBUG MODE / " +
                            "RESOURCE PATH \"" + Configuration.getTracksPath() + "\" / " +
                            "TICKRATE " + Configuration.getTickRateNs() + " / " +
                            "WIDTH " + Configuration.getWidth() + " / " +
                            "HEIGHT " + Configuration.getHeight() + " / " +
                            "CONTENT WIDTH " + Configuration.getWidth() + " / " +
                            "CONTENT HEIGHT " + Configuration.getHeight() + " / " +
                            "MOUSE-X " + MouseMovementAdapter.getX() + " / MOUSE-Y " + MouseMovementAdapter.getY(),
                    10, Configuration.getHeight() - 10);
        }
    }
}
