package com.basketbandit.rizumu;

import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.input.MouseInput;
import com.basketbandit.rizumu.scene.RenderObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer extends Canvas {
    private JFrame frame;
    private RenderObject renderObject;

    private GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    private Font[] fonts = ge.getAllFonts();

    Renderer() {
        addMouseListener(new MouseInput());
        addKeyListener(new KeyInput());
        initFrame();
        this.frame.add(this);
    }

    private void initFrame() {
        this.frame = new JFrame("Rizumu");
        this.frame.setSize(1280, 720);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setFocusable(true);
        this.frame.setUndecorated(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.requestFocus();
    }

    public void setRenderObject(RenderObject renderObject) {
        this.renderObject = renderObject;
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();

        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics2D g = (Graphics2D)bs.getDrawGraphics();

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, SystemConfiguration.getWidth(), SystemConfiguration.getHeight());

        g.setFont(fonts[368].deriveFont(Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.drawString(Rizumu.engine.getFps() + " FPS | " + Rizumu.engine.getTps() + " TPS", 10, 20);

        renderObject.render(g);

        g.dispose();
        bs.show();
    }

}
