package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.audio.AudioPlayer;
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
import java.util.stream.Stream;

public class TrackParser {
    private static final Logger log = LoggerFactory.getLogger(TrackParser.class);

    private HashMap<String, File> trackFiles = new HashMap<>();
    private HashMap<String, Track> trackObjects = new HashMap<>();

    public TrackParser(String path) {
        // Checks all the files in the directory of the given path for files ending in .yaml, then tries to parse them as beatmaps.
        try(Stream<Path> walk = Files.walk(Paths.get(path))) {
            walk.filter(Files::isRegularFile).filter(file -> file.toFile().getName().endsWith(".yaml")).forEach(s -> {
                File file = s.toFile();
                Track track = parseTrack(file);
                String name = track.getArtist()+track.getName();
                track.setTrackInfo(file.getParent(), name, file, AudioPlayer.getTrackLength(track.getAudioFilePath()));
                trackFiles.put(name, file);
                trackObjects.put(name, track);
            });
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        log.info(trackFiles.size() + " track file(s) located");
    }

    public Track parseTrack(String name) {
        try {
            Track track = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(trackFiles.get(name)), Track.class);
            track.setTrackInfo(trackFiles.get(name).getParent(), name, trackFiles.get(name), AudioPlayer.getTrackLength(track.getAudioFilePath()));
            return track;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    public Track parseTrack(File file) {
        try {
            Track track = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), Track.class);
            track.setTrackInfo(file.getParent(), file.getName(), file, AudioPlayer.getTrackLength(track.getAudioFilePath()));
            return track;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    public HashMap<String, Track> getTrackObjects() {
        return trackObjects;
    }
}
