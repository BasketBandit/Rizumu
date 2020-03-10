package com.basketbandit.rizumu;

import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public class Engine extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Engine.class);

    private Renderer renderer;
    private Ticker ticker;

    private Scene primaryScene;
    private Scene secondaryScene;

    private boolean isRunning = true;

    private int tps, fps = 0;
    private int frames, ticks = 0;

    Engine() {
        this.setName("Engine");
        this.renderer = new Renderer();
        this.ticker = new Ticker();
    }

    public void run() {
        long time = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double unprocessed = 0.0;
        boolean canRender;

        while(isRunning) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / Configuration.getTickRateNs();
            lastTime = now;

            if(unprocessed >= 1.0) {
                ticker.tick();
                tps++;
                unprocessed--;
                canRender = true;
            } else {
                canRender = Configuration.isFrameLock();
            }

            if(canRender) {
                renderer.render();
                fps++;
            }

            if(System.currentTimeMillis() - 1000 > time) {
                time += 1000;
                frames = fps;
                ticks = tps;
                fps = 0;
                tps = 0;
            }
        }
    }

    public int getFps() {
        return frames;
    }

    public int getTps() {
        return ticks;
    }

    JFrame getFrame() {
        return renderer.getFrame();
    }

    void addMouseAdapter(MouseAdapter adapter) {
        renderer.addMouseListener(adapter);
        renderer.addMouseWheelListener(adapter);
    }

    void removeMouseAdapter(MouseAdapter adapter) {
        renderer.removeMouseListener(adapter);
        renderer.removeMouseListener(adapter);
    }

    void addKeyAdapter(KeyAdapter adapter) {
        renderer.addKeyListener(adapter);
    }

    void removeKeyAdapter(KeyAdapter adapter) {
        renderer.removeKeyListener(adapter);
    }

    void setPrimaryScene(Scene scene) {
        this.primaryScene = scene;
        this.renderer.setPrimaryRenderObject(scene.getRenderObject());
        this.ticker.setPrimaryTickObject(scene.getTickObject());
        log.info("Primary render/tick objects changed: " + scene.getRenderObject().getClass().getSimpleName() + ";" + scene.getTickObject().getClass().getSimpleName());
    }

    public Scene getPrimaryScene() {
        return primaryScene;
    }

    void setSecondaryScene(Scene scene) {
        this.secondaryScene = scene;

        if(scene != null) {
            this.renderer.setSecondaryRenderObject(scene.getRenderObject());
            this.ticker.setSecondaryTickObject(scene.getTickObject());
            log.info("Secondary render/tick objects changed: " + scene.getRenderObject().getClass().getSimpleName() + ";" + scene.getTickObject().getClass().getSimpleName());
            return;
        }

        this.renderer.setSecondaryRenderObject(null);
        this.ticker.setSecondaryTickObject(null);
        log.info("Secondary render/tick objects removed.");
    }

    public Scene getSecondaryScene() {
        return secondaryScene;
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setBackgroundRenderObject(RenderObject renderObject) {
        this.renderer.setBackgroundRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setPrimaryRenderObject(RenderObject renderObject) {
        this.renderer.setPrimaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setSecondaryRenderObject(RenderObject renderObject) {
        this.renderer.setSecondaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    void setSystemRenderObject(RenderObject renderObject) {
        this.renderer.setSystemRenderObject(renderObject);
    }

    boolean secondaryRenderObjectIsNull() {
        return this.renderer.secondaryRenderObjectIsNull();
    }

}
