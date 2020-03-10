package com.basketbandit.rizumu.beatmap;

import com.basketbandit.rizumu.audio.AudioPlayer;
import com.basketbandit.rizumu.beatmap.core.Track;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class TrackParser {
    private static final Logger log = LoggerFactory.getLogger(TrackParser.class);

    private HashMap<String, File> trackFiles = new HashMap<>();
    private HashMap<String, Track> trackObjects = new HashMap<>();
    private static boolean finished;

    public TrackParser(String path) {
        finished = false;

        // Checks all the files in the directory of the given path for files ending in .yaml, then tries to parse them as beatmaps.
        try(Stream<Path> walk = Files.walk(Paths.get(path))) {
            walk.filter(Files::isRegularFile).filter(file -> file.toFile().getName().endsWith(".yaml")).forEach(s -> {
                File file = s.toFile();
                Track track = parseTrack(file);
                String name = track.getArtist()+track.getName();
                trackFiles.put(name, file);
                trackObjects.put(name, track);
            });
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
        }

        finished = true;
        log.info(trackFiles.size() + " track file(s) located");
    }

    public Track parseTrack(File file) {
        try {
            Track track = new ObjectMapper(new YAMLFactory()).readValue(new FileReader(file), Track.class);
            track.setTrackInfo(file.getParent(), file.getName(), file).setTrackLength(AudioPlayer.getTrackLength(track.getAudioFilePath()));

            // all of this to determine the length of the track...
            AtomicInteger a = new AtomicInteger();
            MediaPlayer m = new MediaPlayer(new Media(new File(track.getAudioFilePath()).toURI().toString()));
            m.setOnReady(() -> a.set((int) m.getMedia().getDuration().toSeconds()));
            while(a.get() == 0) {
                System.currentTimeMillis();
            }
            track.setTrackLength(a.get());

            return track;
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", this.getClass().getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }

    public HashMap<String, Track> getTrackObjects() {
        return trackObjects;
    }

    public static boolean isFinished() {
        return finished;
    }
}
