package com.basketbandit.rizumu;

import com.basketbandit.rizumu.input.MouseMovementListener;
import com.basketbandit.rizumu.stage.object.DefaultBackgroundRenderObject;
import com.basketbandit.rizumu.stage.object.DefaultSystemRenderObject;
import com.basketbandit.rizumu.stage.object.RenderObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Renderer extends Canvas {
    private JFrame frame;
    private RenderObject backgroundRenderObject = new DefaultBackgroundRenderObject();
    private RenderObject primaryRenderObject;
    private RenderObject secondaryRenderObject;
    private RenderObject systemRenderObject = new DefaultSystemRenderObject(); // used to render things such as framerate

    Renderer() {
        addMouseMotionListener(new MouseMovementListener());
        initFrame();
        this.frame.add(this);
    }

    private void initFrame() {
        this.frame = new JFrame("Rizumu");
        this.frame.getContentPane().setPreferredSize(new Dimension(1280, 720));
        this.frame.setSize(1280, 720);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setFocusable(true);
        this.frame.setUndecorated(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.requestFocus();
        this.frame.pack();

        Configuration.setContentBounds(this.frame.getContentPane().getWidth(), this.frame.getContentPane().getHeight());
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setBackgroundRenderObject(RenderObject renderObject) {
        this.backgroundRenderObject = renderObject;
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setPrimaryRenderObject(RenderObject renderObject) {
        this.primaryRenderObject = renderObject;
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setSecondaryRenderObject(RenderObject renderObject) {
        this.secondaryRenderObject = renderObject;
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setSystemRenderObject(RenderObject renderObject) {
        this.systemRenderObject = renderObject;
    }

    boolean secondaryRenderObjectIsNull() {
        return secondaryRenderObject == null;
    }

    JFrame getFrame() {
        return frame;
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

        if(systemRenderObject != null) {
            systemRenderObject.render(g);
        }

        g.dispose();
        bs.show();
    }

}
