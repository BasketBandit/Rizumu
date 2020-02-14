package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.beatmap.core.Track;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Stream;

public class TrackParser {
    private static final Logger log = LoggerFactory.getLogger(TrackParser.class);

    private HashMap<String, File> trackFiles = new HashMap<>();

    public TrackParser(String path) {
        // Checks all the files in the directory of the given path for files ending in .yaml, then tries to parse them as beatmaps.
        try(Stream<Path> walk = Files.walk(Paths.get(path))) {
            walk.filter(Files::isRegularFile).filter(file -> file.toFile().getName().endsWith(".yaml")).forEach(s -> trackFiles.put(s.getFileName().toString(), s.toFile()));
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        log.info(trackFiles.size() + " track file(s) located");
    }

    public Track parseTrack(String name) {
        try {
            return new ObjectMapper(new YAMLFactory()).readValue(new FileReader(trackFiles.get(name)), Track.class);
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    public Set<String> getBeatmaps() {
        return trackFiles.keySet();
    }
}
