package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.Scene;

public class Engine {
    private Renderer renderer;
    private Ticker ticker;

    private boolean isRunning;

    private int tps, fps = 0;
    private int frames, ticks = 0;

    public Engine() {
        this.renderer = new Renderer();
        this.ticker = new Ticker();
    }

    public void run() {
        long time = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double unprocessed = 0.0;
        boolean canRender;

        while(isRunning = true) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / SystemConfiguration.getTickRate();
            lastTime = now;

            if(unprocessed >= 1.0) {
                ticker.tick();
                tps++;
                unprocessed--;
                canRender = true;
            } else {
                canRender = SystemConfiguration.isUnlockedFramerate(); // setting to true unlocked framerate but adds possibility for concurrent modification exceptions - need to fix this urgently
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

    int getFps() {
        return frames;
    }

    int getTps() {
        return ticks;
    }

    public void changeScene(Scene scene) {
        this.renderer.setRenderObject(scene.getRenderObject());
        this.ticker.setTickObject(scene.getTickObject());
    }

}
