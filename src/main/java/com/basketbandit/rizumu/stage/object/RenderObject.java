package com.basketbandit.rizumu.stage.object;

import java.awt.*;

public interface RenderObject {
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    Font[] fonts = ge.getAllFonts();

    void render(Graphics2D g);
}
