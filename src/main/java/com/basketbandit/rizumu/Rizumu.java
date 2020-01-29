package com.basketbandit.rizumu;

import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.scene.MenuScene;
import com.basketbandit.rizumu.beatmap.BeatmapParser;

import java.util.ArrayList;

public class Rizumu {
    private static SystemConfiguration sys = new SystemConfiguration();
    public static Engine engine;
    private static ArrayList<Beatmap> beatmaps = new ArrayList<>();
    private static String tracksResource = "src/main/resources/beatmaps/beatmap-schema_example.yaml";

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", args[0]); // OpenGL
        //System.setProperty("sun.java2d.3d3", args[0]); // DirectX
        loadTracks();
        engine = new Engine();
        engine.changeScene(new MenuScene());
        engine.run();
    }

    private static void loadTracks() {
        beatmaps = new BeatmapParser(tracksResource).getBeatmaps();
    }

    public static ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}
