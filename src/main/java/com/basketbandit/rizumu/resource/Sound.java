package com.basketbandit.rizumu.resource;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class Sound {
    private static final Logger log = LoggerFactory.getLogger(Sound.class);
    private static HashMap<String, InputStream> audioStreams = new HashMap<>();
    private static HashMap<String, File> audioFiles = new HashMap<>();

    public Sound() {
        audioStreams.put("menu-music", getClass().getResourceAsStream("/assets/sound/ffxiiimenu.wav"));
        audioStreams.put("menu-click", getClass().getResourceAsStream("/assets/sound/click.wav"));
        audioStreams.put("menu-select", getClass().getResourceAsStream("/assets/sound/select.wav"));
        audioStreams.put("menu-click2", getClass().getResourceAsStream("/assets/sound/clickc6.wav"));
        audioStreams.put("menu-click3", getClass().getResourceAsStream("/assets/sound/clickg6.wav"));
        audioStreams.put("menu-click4", getClass().getResourceAsStream("/assets/sound/clickc7.wav"));
        audioStreams.put("menu-select2", getClass().getResourceAsStream("/assets/sound/selectc5g5c6.wav"));
        audioStreams.put("track-hit", getClass().getResourceAsStream("/assets/sound/hit.wav"));
        audioStreams.put("track-combobreak", getClass().getResourceAsStream("/assets/sound/combobreak.wav"));

        for(String identifier: audioStreams.keySet()) {
            try {
                File file = File.createTempFile(identifier, ".wav");
                file.deleteOnExit();
                FileUtils.copyToFile(audioStreams.get(identifier), file);
                audioFiles.put(identifier, file);
            } catch(Exception ex) {
                log.error("An error occurred while running the {} class, message: {}", Sound.class.getSimpleName(), ex.getMessage(), ex);
            }
        }
    }

    public static File getAudioFile(String identifier) {
        return audioFiles.get(identifier);
    }

    public static AudioInputStream getAudioInputStream(String identifier) {
        try {
            return AudioSystem.getAudioInputStream(audioFiles.get(identifier));
        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Sound.class.getSimpleName(), ex.getMessage(), ex);
            return null;
        }
    }
}
