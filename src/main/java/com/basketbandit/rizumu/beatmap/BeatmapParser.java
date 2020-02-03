package com.basketbandit.rizumu.beatmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class BeatmapParser {
    private static final Logger log = LoggerFactory.getLogger(BeatmapParser.class);

    private ArrayList<Beatmap> beatmaps = new ArrayList<>();

    public BeatmapParser(String path) {
        // Checks all the files in the directory of the given path for files ending in .yaml, then tries to parse them as beatmaps.
        try(Stream<Path> walk = Files.walk(Paths.get(path))) {
            walk.filter(Files::isRegularFile).filter(file -> file.toFile().getName().endsWith(".yaml")).forEach(s -> {
                try {
                    beatmaps.addAll(new ObjectMapper(new YAMLFactory()).readValue(new FileReader(s.toFile()), BeatmapContainer.class).getBeatmaps());
                } catch(IOException ex) {
                    log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
                }
            });
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        log.info(beatmaps.size() + " beatmap(s) parsed");
    }

    public ArrayList<Beatmap> getBeatmaps() {
        return beatmaps;
    }
}
