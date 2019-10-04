package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MenuScene implements Scene {
    private MenuRender renderObject = new MenuRender();
    private MenuTicker tickObject = new MenuTicker();

    private Button button = new Button(80, 80, 100, 50);

    public MenuScene() {
        Rizumu.engine.changeScene(this);
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class MenuRender implements RenderObject {
        private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        private Font[] fonts = ge.getAllFonts();

        @Override
        public void render(Graphics2D g) {
            g.setColor(button.getColor());
            g.fillRect(button.x, button.y, button.width, button.height);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);

            g.drawString("Play Song!", 102, 110);
        }
    }

    private class MenuTicker implements TickObject {
        @Override
        public void tick() {
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && button.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                Rizumu.engine.changeScene(new TrackScene(new SystemConfiguration(), Rizumu.getTracks().get("testtrack01")));
            }
        }
    }

}