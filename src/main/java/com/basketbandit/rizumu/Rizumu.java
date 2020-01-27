package com.basketbandit.rizumu;

import com.basketbandit.rizumu.scene.MenuScene;
import com.basketbandit.rizumu.track.Track;
import com.basketbandit.rizumu.track.TrackReader;

import java.util.HashMap;

public class Rizumu {
    private static SystemConfiguration sys = new SystemConfiguration();
    public static Engine engine;
    private static HashMap<String, Track> tracks = new HashMap<>();
    private static String tracksResource = "/tracks/testtrack.track";

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        loadTracks();
        engine = new Engine();
        engine.changeScene(new MenuScene());
        engine.run();
    }

    private static void loadTracks() {
        Track track = new TrackReader(tracksResource).build();
        tracks.put(track.getTitle(), track);
    }

    public static HashMap<String, Track> getTracks() {
        return tracks;
    }

}
