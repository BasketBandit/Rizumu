package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileReader;
import java.util.ArrayList;

public class BeatmapParser {
    private ArrayList<Beatmap> beatmaps = new ArrayList<>();

    public BeatmapParser(String path) {
        try {
            beatmaps.addAll(new ObjectMapper(new YAMLFactory()).readValue(new FileReader(path), BeatmapContainer.class).getBeatmaps());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}
