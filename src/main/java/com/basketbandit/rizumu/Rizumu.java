package com.basketbandit.rizumu;

import com.basketbandit.rizumu.audio.AudioPlayerController;
import com.basketbandit.rizumu.beatmap.Beatmap;
import com.basketbandit.rizumu.beatmap.BeatmapParser;
import com.basketbandit.rizumu.scene.SplashScene;

import java.util.ArrayList;

public class Rizumu {
    public static Engine engine;
    private static ArrayList<Beatmap> beatmaps = new ArrayList<>();

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", args[0]); // OpenGL
        //System.setProperty("sun.java2d.3d3", args[0]); // DirectX

        new SystemConfiguration(); // Initialises system configs.
        loadTracks();
        new AudioPlayerController(); // Initialises the AudioPlayerController
        engine = new Engine();
        engine.changeScene(new SplashScene());
        engine.run();
    }

    private static void loadTracks() {
        beatmaps = new BeatmapParser(SystemConfiguration.getBeatmapResourcePath()).getBeatmaps();
    }

    public static ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}
