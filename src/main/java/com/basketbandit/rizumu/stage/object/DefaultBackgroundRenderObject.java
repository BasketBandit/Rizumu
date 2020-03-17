package com.basketbandit.rizumu.stage.object;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.utility.Colours;

import java.awt.*;

public class DefaultBackgroundRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setColor(Colours.WHITE);
        g.fillRect(0, 0, Configuration.getWidth(), Configuration.getHeight());
    }
}


