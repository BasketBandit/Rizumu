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
    private GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
    private JFrame frame;
    private RenderObject backgroundRenderObject = new DefaultBackgroundRenderObject();
    private RenderObject primaryRenderObject;
    private RenderObject secondaryRenderObject;
    private RenderObject systemRenderObject = new DefaultSystemRenderObject(); // used to render things such as framerate

    Renderer() {
        addMouseMotionListener(new MouseMovementAdapter());
        initFrame();
        frame.add(this);
    }

    private void initFrame() {
        frame = new JFrame("Rizumu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setPreferredSize(new Dimension(Configuration.getWidth(), Configuration.getHeight()));
        frame.setSize(Configuration.getWidth(), Configuration.getHeight());
        frame.setResizable(false);

        // https://stackoverflow.com/questions/16987937/remove-disable-override-swings-focus-traversal-keys
        Integer[] focusKeys = new Integer[] {
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS,
                KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS
        };

        // disable all focus keys, allowing custom implementation of TAB key, etc.
        for(Integer key: focusKeys) {
            frame.setFocusTraversalKeys(key, Collections.EMPTY_SET);
        }

        if(Configuration.isFullscreen()) {
            frame.setUndecorated(true);
            device.setFullScreenWindow(frame);
            device.setDisplayMode(new DisplayMode(Configuration.getWidth(), Configuration.getHeight(), 32, 60));
        }

        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.pack();
        frame.setVisible(true);
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
