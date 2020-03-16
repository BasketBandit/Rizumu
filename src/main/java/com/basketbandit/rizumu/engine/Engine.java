package com.basketbandit.rizumu.engine;

import com.basketbandit.rizumu.Configuration;
import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.TrackParser;
import com.basketbandit.rizumu.database.Connection;
import com.basketbandit.rizumu.media.Image;
import com.basketbandit.rizumu.media.Sound;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.*;
import com.basketbandit.rizumu.stage.scene.menu.MenuScene;
import com.basketbandit.rizumu.stage.scene.splash.SplashScene;
import com.basketbandit.rizumu.stage.scene.track.TrackScene;
import com.basketbandit.rizumu.stage.scene.track.scondary.ResultsScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class Engine extends Thread {
    private static final Logger log = LoggerFactory.getLogger(Engine.class);
    private static HashMap<Scenes, Scene> staticScenes = new HashMap<>();
    private static TrackParser trackParser;

    private static Renderer renderer;
    private static Ticker ticker;

    private static Scene primaryScene;
    private static Scene secondaryScene;

    private static boolean isRunning = true;

    private static int tps, fps = 0;
    private static int frames, ticks = 0;

    public Engine() {
        this.setName("Engine");
        // initialises system configs
        new Configuration();

        // initialises renderer
        renderer = new Renderer();

        // initialises ticker
        ticker = new Ticker();

        // initialise database connection
        try {
            new Connection();
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Connection.class.getSimpleName(), ex.getMessage(), ex);
        }

        // initialises AudioPlayerController
        new AudioPlayerController();

        // initialises Sound
        new Sound();

        // initialises Image
        new Image();

        staticScenes.put(Scenes.SPLASH, new SplashScene());
        staticScenes.put(Scenes.MENU, new MenuScene());
        staticScenes.put(Scenes.TRACK, new TrackScene());
        staticScenes.put(Scenes.RESULTS, new ResultsScene());

        setPrimaryScene(staticScenes.get(Scenes.SPLASH).init());
    }

    @Override
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
                canRender = Configuration.isFramerateUnlocked();
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

    public static int getFps() {
        return frames;
    }

    public static int getTps() {
        return ticks;
    }

    public static JFrame getFrame() {
        return renderer.getFrame();
    }

    public static void addMouseAdapter(MouseAdapter adapter) {
        renderer.addMouseListener(adapter);
        renderer.addMouseWheelListener(adapter);
    }

    public static void removeMouseAdapter(MouseAdapter adapter) {
        renderer.removeMouseListener(adapter);
        renderer.removeMouseListener(adapter);
    }

    public static void addKeyAdapter(KeyAdapter adapter) {
        renderer.addKeyListener(adapter);
    }

    public static void removeKeyAdapter(KeyAdapter adapter) {
        renderer.removeKeyListener(adapter);
    }

    public static Scene getStaticScene(Scenes scene) {
        return staticScenes.get(scene);
    }

    public static void setPrimaryScene(Scene scene) {
        primaryScene = scene;
        renderer.setPrimaryRenderObject(scene.getRenderObject());
        ticker.setPrimaryTickObject(scene.getTickObject());
        log.info("Primary render/tick objects changed: " + scene.getRenderObject().getClass().getSimpleName() + ";" + scene.getTickObject().getClass().getSimpleName());
    }

    public static Scene getPrimaryScene() {
        return primaryScene;
    }

    public static void setSecondaryScene(Scene scene) {
        secondaryScene = scene;

        if(scene != null) {
            renderer.setSecondaryRenderObject(scene.getRenderObject());
            ticker.setSecondaryTickObject(scene.getTickObject());
            log.info("Secondary render/tick objects changed: " + scene.getRenderObject().getClass().getSimpleName() + ";" + scene.getTickObject().getClass().getSimpleName());
            return;
        }

        renderer.setSecondaryRenderObject(null);
        ticker.setSecondaryTickObject(null);
        log.info("Secondary render/tick objects removed.");
    }

    public static Scene getSecondaryScene() {
        return secondaryScene;
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setBackgroundRenderObject(RenderObject renderObject) {
        renderer.setBackgroundRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setPrimaryRenderObject(RenderObject renderObject) {
        renderer.setPrimaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setSecondaryRenderObject(RenderObject renderObject) {
        renderer.setSecondaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setSystemRenderObject(RenderObject renderObject) {
        renderer.setSystemRenderObject(renderObject);
    }

    public static boolean secondaryRenderObjectIsNull() {
        return renderer.secondaryRenderObjectIsNull();
    }

    public static void setTrackParser(TrackParser parser) {
        trackParser = parser;
    }

    public static TrackParser getTrackParser() {
        return trackParser;
    }
}
