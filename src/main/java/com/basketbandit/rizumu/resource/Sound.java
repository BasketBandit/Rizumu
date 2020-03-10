package com.basketbandit.rizumu.resource;

import javafx.scene.media.Media;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class Sound {
    private static final Logger log = LoggerFactory.getLogger(Sound.class);
    private static HashMap<String, InputStream> inputStreams = new HashMap<>();
    private static HashMap<String, Media> mediaFiles = new HashMap<>();

    public Sound() {
        inputStreams.put("menu-music", getClass().getResourceAsStream("/assets/sound/ffxiiimenu.mp3"));
        inputStreams.put("menu-click", getClass().getResourceAsStream("/assets/sound/click.mp3"));
        inputStreams.put("menu-click2", getClass().getResourceAsStream("/assets/sound/clickc6.mp3"));
        inputStreams.put("menu-click3", getClass().getResourceAsStream("/assets/sound/clickg6.mp3"));
        inputStreams.put("menu-click4", getClass().getResourceAsStream("/assets/sound/clickc7.mp3"));
        inputStreams.put("menu-select", getClass().getResourceAsStream("/assets/sound/select.mp3"));
        inputStreams.put("menu-select2", getClass().getResourceAsStream("/assets/sound/selectc5g5c6.mp3"));
        inputStreams.put("track-hit", getClass().getResourceAsStream("/assets/sound/hit.mp3"));
        inputStreams.put("track-combobreak", getClass().getResourceAsStream("/assets/sound/combobreak.mp3"));

        for(String identifier: inputStreams.keySet()) {
            try {
                File file = File.createTempFile(identifier, ".mp3");
                file.deleteOnExit();
                FileUtils.copyToFile(inputStreams.get(identifier), file);
                mediaFiles.put(identifier, new Media(file.toURI().toString()));
            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", Sound.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

    public static Media getMedia(String identifier) {
        return mediaFiles.get(identifier);
    }
}
