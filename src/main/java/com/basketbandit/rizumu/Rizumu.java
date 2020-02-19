package com.basketbandit.rizumu;

import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.TrackParser;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.*;

import javax.swing.*;
import java.util.HashMap;

public class Rizumu {
    private static boolean debug;
    public static Engine engine = new Engine();
    private static HashMap<Scenes, Scene> staticScenes = new HashMap<>();
    private static TrackParser trackParser;

    public static void main(String[] args) {
        debug =  Boolean.parseBoolean(args[0]);

        System.setProperty("sun.java2d.opengl", args[1]); // OpenGL
        //System.setProperty("sun.java2d.3d3", args[1]); // DirectX

        // initialises system configs
        new Configuration();

        // loads and parses beatmaps
        trackParser = new TrackParser(Configuration.getBeatmapResourcePath());

        // initialises AudioPlayerController
        new AudioPlayerController();

        staticScenes.put(Scenes.SPLASH, new SplashScene());
        staticScenes.put(Scenes.MENU, new MenuScene());
        staticScenes.put(Scenes.TRACK, new TrackScene());
        staticScenes.put(Scenes.RESULTS, new ResultsScene());

        engine.setPrimaryScene(getStaticScene(Scenes.SPLASH));
        engine.start();
    }

    public static boolean isDebug() {
        return debug;
    }

    /**
     * @return {@link TrackParser}
     */
    public static TrackParser getTrackParser() {
        return trackParser;
    }

    public static JFrame getFrame() {
        return engine.getFrame();
    }

    /**
     * @param scene {@link Scenes}
     * @return {@link Scene}
     */
    public static Scene getStaticScene(Scenes scene) {
        return staticScenes.get(scene);
    }

    /**
     * @param scene {@link Scene}
     */
    public static void setPrimaryScene(Scene scene) {
        engine.setPrimaryScene(scene);
    }

    /**
     * @param scene {@link Scene}
     */
    public static void setSecondaryScene(Scene scene) {
        engine.setSecondaryScene(scene);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setBackgroundRenderObject(RenderObject renderObject) {
        engine.setBackgroundRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setPrimaryRenderObject(RenderObject renderObject) {
        engine.setPrimaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setSecondaryRenderObject(RenderObject renderObject) {
        engine.setSecondaryRenderObject(renderObject);
    }

    /**
     * @param renderObject {@link RenderObject}
     */
    public static void setSystemRenderObject(RenderObject renderObject) {
        engine.setSystemRenderObject(renderObject);
    }

    public static boolean secondaryRenderObjectIsNull() {
        return engine.secondaryRenderObjectIsNull();
    }
}
