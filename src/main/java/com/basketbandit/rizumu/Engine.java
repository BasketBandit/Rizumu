package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.Scene;

public class Engine {
    private SystemConfiguration sys;
    private Renderer renderer;
    private Ticker ticker;

    private boolean isRunning;

    private int tps, fps = 0;
    private int frames, ticks = 0;

    public Engine(SystemConfiguration sys) {
        this.sys = sys;
        this.renderer = new Renderer(sys);
        this.ticker = new Ticker(sys);
    }

    public void run() {
        long time = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double unprocessed = 0.0;
        boolean canRender;

        while(isRunning = true) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / sys.getTickRate();
            lastTime = now;

            if(unprocessed >= 1.0) {
                ticker.tick();
                tps++;
                unprocessed--;
                canRender = true;
            } else {
                canRender = true;
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

    public void changeScene(Scene scene) {
        this.renderer.setRenderObject(scene.getRenderObject());
        this.ticker.setTickObject(scene.getTickObject());
    }

}
