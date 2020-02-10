package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;

import java.awt.*;

public class DefaultBackgroundRenderObject implements RenderObject {
    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, SystemConfiguration.getWidth(), SystemConfiguration.getHeight());

        g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
        g.setColor(Color.GRAY);
        g.drawString(Rizumu.engine.getFps() + " FPS | " + Rizumu.engine.getTps() + " TPS", 10, 20);

        if(Rizumu.isDebug()) {
            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.GRAY);
            g.drawString("DEBUG MODE / " +
                    "RESOURCE PATH \"" + SystemConfiguration.getBeatmapResourcePath() + "\" / " +
                    "TICKRATE " + SystemConfiguration.getTickRate() + " / " +
                    "WIDTH " + SystemConfiguration.getWidth() + " / " +
                    "HEIGHT " + SystemConfiguration.getHeight(),
                    10, SystemConfiguration.getHeight() - 50);
        }
    }
}


