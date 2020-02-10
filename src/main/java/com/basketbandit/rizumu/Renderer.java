package com.basketbandit.rizumu;

import com.basketbandit.rizumu.input.KeyInput;
import com.basketbandit.rizumu.input.MouseInput;
import com.basketbandit.rizumu.scene.DefaultBackgroundRenderObject;
import com.basketbandit.rizumu.scene.RenderObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer extends Canvas {
    private JFrame frame;
    private RenderObject backgroundRenderObject = new DefaultBackgroundRenderObject();
    private RenderObject primaryRenderObject;
    private RenderObject secondaryRenderObject;

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

    void setBackgroundRenderObject(RenderObject renderObject) {
        this.backgroundRenderObject = renderObject;
    }

    void setPrimaryRenderObject(RenderObject renderObject) {
        this.primaryRenderObject = renderObject;
    }

    void setSecondaryRenderObject(RenderObject renderObject) {
        this.secondaryRenderObject = renderObject;
    }

    void render() {
        BufferStrategy bs = getBufferStrategy();

        if(bs == null) {
            createBufferStrategy(2); // Double buffered - increasing this value will chunk framerate (if uncapped)
            return;
        }

        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        if(backgroundRenderObject != null) {
            backgroundRenderObject.render(g);
        }

        if(primaryRenderObject != null) {
            primaryRenderObject.render(g);
        }

        if(secondaryRenderObject != null) {
            secondaryRenderObject.render(g);
        }

        g.dispose();
        bs.show();
    }

}
