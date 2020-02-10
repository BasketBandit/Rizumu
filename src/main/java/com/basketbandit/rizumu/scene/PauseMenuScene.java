package com.basketbandit.rizumu.scene;

import com.basketbandit.rizumu.Rizumu;
import com.basketbandit.rizumu.SystemConfiguration;
import com.basketbandit.rizumu.drawable.Button;
import com.basketbandit.rizumu.input.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;


public class PauseMenuScene implements Scene {
    private PauseMenuRenderer renderObject = new PauseMenuRenderer();
    private PauseMenuTicker tickObject = new PauseMenuTicker();

    private Color transGray = new Color(50,50,50, 235);

    private Button resumeButton = new Button(80, 80, 100, 50);
    private Button quitButton = new Button(80, 140, 100, 50);

    public PauseMenuScene() {
    }

    @Override
    public RenderObject getRenderObject() {
        return renderObject;
    }

    @Override
    public TickObject getTickObject() {
        return tickObject;
    }

    private class PauseMenuRenderer implements RenderObject {
        @Override
        public void render(Graphics2D g) {
            g.setColor(transGray);
            g.fillRect(0, 0, SystemConfiguration.getWidth(), SystemConfiguration.getHeight());

            g.setColor(resumeButton.getColor());
            g.fill(resumeButton);
            g.fill(quitButton);

            g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
            g.setColor(Color.WHITE);
            g.drawString("Resume!", (int)resumeButton.getMinX(), (int)resumeButton.getCenterY());
            g.drawString("Quit!", (int)quitButton.getMinX(), (int)quitButton.getCenterY());
        }
    }

    private class PauseMenuTicker implements TickObject {
        @Override
        public void tick() {
            // if left-click is pushed and the cursor is in the bounds of the 'resume' button, close the pause menu
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && resumeButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                Rizumu.setSecondaryScene(null);
            }

            // if left-click is pushed and the cursor is in the bounds of the 'quit' button, got back to the main menu
            if(MouseInput.isPressed(MouseEvent.BUTTON1) && quitButton.getBounds().contains(MouseInput.getX(), MouseInput.getY())) {
                Rizumu.setSecondaryScene(null);
                Rizumu.setPrimaryScene(Rizumu.getStaticScene(Scenes.MENU));
            }
        }
    }
}
