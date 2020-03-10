package com.basketbandit.rizumu;

import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.TrackParser;
import com.basketbandit.rizumu.database.Connection;
import com.basketbandit.rizumu.resource.Image;
import com.basketbandit.rizumu.resource.Sound;
import com.basketbandit.rizumu.stage.Scenes;
import com.basketbandit.rizumu.stage.object.RenderObject;
import com.basketbandit.rizumu.stage.scene.*;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.util.HashMap;

public class Rizumu extends Application {
    private static final Logger log = LoggerFactory.getLogger(Rizumu.class);
    private static boolean debug;
    public static Engine engine = new Engine();
    private static HashMap<Scenes, Scene> staticScenes = new HashMap<>();
    private static TrackParser trackParser;

    //TODO Login menu
    //TODO Track select auto-scroll

    public static void main(String[] args) {
        if(args.length > 0) {
            debug = Boolean.parseBoolean(args[0]);
        }

        System.setProperty("sun.java2d.opengl", "true"); // OpenGL
        //System.setProperty("sun.java2d.3d3", args[1]); // DirectX

        // initialises system configs
        new Configuration();

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
        staticScenes.put(Scenes.SETTINGS, new SettingsScene());
        staticScenes.put(Scenes.RESULTS, new ResultsScene());

        // start the engine early so TrackParser doesn't
        engine.setPrimaryScene(getStaticScene(Scenes.SPLASH).init());
        engine.start();

        // loads and parses beatmaps
        trackParser = new TrackParser(Configuration.getTracksPath());

        // these scenes require track parser to finish
        staticScenes.put(Scenes.MENU, new MenuScene());
        staticScenes.put(Scenes.TRACK, new TrackScene());
    }

    @Override
    public void start(Stage stage) throws Exception {
    }

    public static boolean isDebug() {
        return debug;
    }

    public static JFrame getFrame() {
        return engine.getFrame();
    }

    /**
     * @param adapter {@link MouseAdapter}
     */
    public static void addMouseAdapter(MouseAdapter adapter) {
        engine.addMouseAdapter(adapter);
    }

    /**
     * @param adapter {@link MouseAdapter}
     */
    public static void removeMouseAdapter(MouseAdapter adapter) {
        engine.removeMouseAdapter(adapter);
    }

    /**
     * @param adapter {@link KeyAdapter}
     */
    public static void addKeyAdapter(KeyAdapter adapter) {
        engine.addKeyAdapter(adapter);
    }

    /**
     * @param adapter {@link KeyAdapter}
     */
    public static void removeKeyAdapter(KeyAdapter adapter) {
        engine.removeKeyAdapter(adapter);
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
     * @return {@link Scene}
     */
    public static Scene getPrimaryScene() {
        return engine.getPrimaryScene();
    }

    /**
     * @param scene {@link Scene}
     */
    public static void setSecondaryScene(Scene scene) {
        engine.setSecondaryScene(scene);
    }

    /**
     * @return {@link Scene}
     */
    public static Scene getSecondaryScene() {
        return engine.getSecondaryScene();
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

    /**
     * @return {@link TrackParser}
     */
    public static TrackParser getTrackParser() {
        return trackParser;
    }
}
