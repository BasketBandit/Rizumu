package com.basketbandit.rizumu;

import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.beatmap.BeatmapParser;
import com.basketbandit.rizumu.scene.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Rizumu {
    private static boolean debug;
    public static Engine engine = new Engine();
    private static HashMap<Scenes, Scene> staticScenes = new HashMap<>();
    private static ArrayList<Beatmap> beatmaps = new ArrayList<>();

    public static void main(String[] args) {
        debug =  Boolean.parseBoolean(args[0]);

        System.setProperty("sun.java2d.opengl", args[1]); // OpenGL
        //System.setProperty("sun.java2d.3d3", args[1]); // DirectX

        // initialises system configs
        new SystemConfiguration();

        // loads and parses beatmaps
        beatmaps = new BeatmapParser(SystemConfiguration.getBeatmapResourcePath()).getBeatmaps();

        // initialises AudioPlayerController
        new AudioPlayerController();

        staticScenes.put(Scenes.SPLASH, new SplashScene());
        staticScenes.put(Scenes.MENU, new MenuScene());

        engine.setPrimaryScene(getStaticScene(Scenes.SPLASH));
        engine.start();
    }

    public static boolean isDebug() {
        return debug;
    }

    /**
     * @return {@link ArrayList<Beatmap>}
     */
    public static ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
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

    public static boolean secondaryRenderObjectIsNull() {
        return engine.secondaryRenderObjectIsNull();
    }
}
