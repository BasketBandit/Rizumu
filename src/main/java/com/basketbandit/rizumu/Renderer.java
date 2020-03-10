package com.basketbandit.rizumu;

import com.basketbandit.rizumu.input.MouseMovementAdapter;
import com.basketbandit.rizumu.stage.object.DefaultBackgroundRenderObject;
import com.basketbandit.rizumu.stage.object.DefaultSystemRenderObject;
import com.basketbandit.rizumu.stage.object.RenderObject;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Collections;

public class Renderer extends Canvas {
    private JFrame frame;
    private RenderObject backgroundRenderObject = new DefaultBackgroundRenderObject();
    private RenderObject primaryRenderObject;
    private RenderObject secondaryRenderObject;
    private RenderObject systemRenderObject = new DefaultSystemRenderObject(); // used to render things such as framerate

    Renderer() {
        addMouseMotionListener(new MouseMovementAdapter());
        initFrame();
        this.frame.add(this);
    }

    private void initFrame() {
        this.frame = new JFrame("Rizumu");
        this.frame.getContentPane().setPreferredSize(new Dimension(Configuration.getWidth(), Configuration.getHeight()));
        this.frame.setSize(Configuration.getWidth(), Configuration.getHeight());
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setResizable(false);
        this.frame.setFocusable(true);
        this.frame.setUndecorated(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.frame.requestFocus();
        this.frame.pack();

        // https://stackoverflow.com/questions/16987937/remove-disable-override-swings-focus-traversal-keys
        Integer[] focusKeys = new Integer[] {
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
                KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
        };

        // disable all focus keys, allowing custom implementation of TAB key, etc.
        for(Integer key: focusKeys) {
            this.frame.setFocusTraversalKeys(key, Collections.EMPTY_SET);
        }
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
