package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.engine.Engine;
import com.basketbandit.rizumu.utility.Alignment;
import com.basketbandit.rizumu.utility.Fonts;

import java.awt.*;

public class DefaultSystemRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setFont(Fonts.default12);
        g.setColor(Color.GRAY);
        String frames = (Engine.getFps() + " FPS | " + Engine.getTps() + " TPS");
        g.drawString(frames, Alignment.right(frames, g.getFontMetrics(Fonts.default12), 0, Configuration.getWidth()) - 10, 20);
    }
}
