package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BeatmapParser {
    private ArrayList<Beatmap> beatmaps = new ArrayList<>();

    public BeatmapParser(String path) {
        try {
            // Checks all the files in the directory of the given path for files ending in .yaml, then tries to parse them as beatmaps.
            Files.walk(Paths.get(path)).filter(file -> !file.getFileName().endsWith(".yaml")).forEach(s -> {
                try {
                    beatmaps.addAll(new ObjectMapper(new YAMLFactory()).readValue(new FileReader(s.toFile()), BeatmapContainer.class).getBeatmaps());
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}
